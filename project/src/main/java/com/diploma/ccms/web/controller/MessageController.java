package com.diploma.ccms.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.diploma.ccms.domain.Message;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/messages")
@Controller


public class MessageController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Message message, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, message);
            return "messages/create";
        }
        uiModel.asMap().clear();
        message.persist();
        return "redirect:/messages/" + encodeUrlPathSegment(message.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Message());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "messages/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("message", Message.findMessage(id));
        uiModel.addAttribute("itemId", id);
        return "messages/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("messages", Message.findMessageEntries(firstResult, sizeNo));
            float nrOfPages = (float) Message.countMessages() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("messages", Message.findAllMessages());
        }
        addDateTimeFormatPatterns(uiModel);
        return "messages/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Message message, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, message);
            return "messages/update";
        }
        uiModel.asMap().clear();
        message.merge();
        return "redirect:/messages/" + encodeUrlPathSegment(message.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Message.findMessage(id));
        return "messages/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Message message = Message.findMessage(id);
        message.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/messages";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("message_datetime_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Message message) {
        uiModel.addAttribute("message", message);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("workers", Worker.findAllWorkers());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(params = { "find=ByFromWorkerEquals", "form" }, method = RequestMethod.GET)
    public String findMessagesByFromWorkerEqualsForm(Model uiModel) {
        uiModel.addAttribute("workers", Worker.findAllWorkers());
        return "messages/findMessagesByFromWorkerEquals";
    }

	@RequestMapping(params = "find=ByFromWorkerEquals", method = RequestMethod.GET)
    public String findMessagesByFromWorkerEquals(@RequestParam("fromWorker") Worker fromWorker, Model uiModel) {
        uiModel.addAttribute("messages", Message.findMessagesByFromWorkerEquals(fromWorker).getResultList());
        return "messages/list";
    }

	@RequestMapping(params = { "find=ByTextLike", "form" }, method = RequestMethod.GET)
    public String findMessagesByTextLikeForm(Model uiModel) {
        return "messages/findMessagesByTextLike";
    }

	@RequestMapping(params = "find=ByTextLike", method = RequestMethod.GET)
    public String findMessagesByTextLike(@RequestParam("text") String text, Model uiModel) {
        uiModel.addAttribute("messages", Message.findMessagesByTextLike(text).getResultList());
        return "messages/list";
    }

	@RequestMapping(params = { "find=ByTitleLike", "form" }, method = RequestMethod.GET)
    public String findMessagesByTitleLikeForm(Model uiModel) {
        return "messages/findMessagesByTitleLike";
    }

	@RequestMapping(params = "find=ByTitleLike", method = RequestMethod.GET)
    public String findMessagesByTitleLike(@RequestParam("title") String title, Model uiModel) {
        uiModel.addAttribute("messages", Message.findMessagesByTitleLike(title).getResultList());
        return "messages/list";
    }

	@RequestMapping(params = { "find=ByToWorkerEquals", "form" }, method = RequestMethod.GET)
    public String findMessagesByToWorkerEqualsForm(Model uiModel) {
        uiModel.addAttribute("workers", Worker.findAllWorkers());
        return "messages/findMessagesByToWorkerEquals";
    }

	@RequestMapping(params = "find=ByToWorkerEquals", method = RequestMethod.GET)
    public String findMessagesByToWorkerEquals(@RequestParam("toWorker") Worker toWorker, Model uiModel) {
        uiModel.addAttribute("messages", Message.findMessagesByToWorkerEquals(toWorker).getResultList());
        return "messages/list";
    }
}
