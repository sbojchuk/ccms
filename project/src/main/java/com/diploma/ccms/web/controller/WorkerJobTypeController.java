package com.diploma.ccms.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.diploma.ccms.domain.WorkerJobType;

@RequestMapping("/workerjobtypes")
@Controller

public class WorkerJobTypeController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid WorkerJobType workerJobType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, workerJobType);
            return "workerjobtypes/create";
        }
        uiModel.asMap().clear();
        workerJobType.persist();
        return "redirect:/workerjobtypes/" + encodeUrlPathSegment(workerJobType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new WorkerJobType());
        return "workerjobtypes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("workerjobtype", WorkerJobType.findWorkerJobType(id));
        uiModel.addAttribute("itemId", id);
        return "workerjobtypes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("workerjobtypes", WorkerJobType.findWorkerJobTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) WorkerJobType.countWorkerJobTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("workerjobtypes", WorkerJobType.findAllWorkerJobTypes());
        }
        return "workerjobtypes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid WorkerJobType workerJobType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, workerJobType);
            return "workerjobtypes/update";
        }
        uiModel.asMap().clear();
        workerJobType.merge();
        return "redirect:/workerjobtypes/" + encodeUrlPathSegment(workerJobType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, WorkerJobType.findWorkerJobType(id));
        return "workerjobtypes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        WorkerJobType workerJobType = WorkerJobType.findWorkerJobType(id);
        workerJobType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/workerjobtypes";
    }

	void populateEditForm(Model uiModel, WorkerJobType workerJobType) {
        uiModel.addAttribute("workerJobType", workerJobType);
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
}
