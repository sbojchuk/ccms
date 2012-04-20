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

import com.diploma.ccms.domain.Region;
import com.diploma.ccms.domain.Team;
import com.diploma.ccms.domain.Worker;
import com.diploma.ccms.domain.WorkerJobType;
import com.diploma.ccms.domain.WorkerRole;

@RequestMapping("/workers")
@Controller


public class WorkerController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Worker worker, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, worker);
            return "workers/create";
        }
        uiModel.asMap().clear();
        worker.persist();
        return "redirect:/workers/" + encodeUrlPathSegment(worker.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Worker());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Region.countRegions() == 0) {
            dependencies.add(new String[] { "region", "regions" });
        }
        if (Team.countTeams() == 0) {
            dependencies.add(new String[] { "team", "teams" });
        }
        if (WorkerJobType.countWorkerJobTypes() == 0) {
            dependencies.add(new String[] { "workerjobtype", "workerjobtypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "workers/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("worker", Worker.findWorker(id));
        uiModel.addAttribute("itemId", id);
        return "workers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("workers", Worker.findWorkerEntries(firstResult, sizeNo));
            float nrOfPages = (float) Worker.countWorkers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("workers", Worker.findAllWorkers());
        }
        addDateTimeFormatPatterns(uiModel);
        return "workers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Worker worker, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, worker);
            return "workers/update";
        }
        uiModel.asMap().clear();
        worker.merge();
        return "redirect:/workers/" + encodeUrlPathSegment(worker.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Worker.findWorker(id));
        return "workers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Worker worker = Worker.findWorker(id);
        worker.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/workers";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("worker_birthday_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("worker_datehire_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Worker worker) {
        uiModel.addAttribute("worker", worker);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("regions", Region.findAllRegions());
        uiModel.addAttribute("teams", Team.findAllTeams());
        uiModel.addAttribute("workerjobtypes", WorkerJobType.findAllWorkerJobTypes());
        uiModel.addAttribute("workerroles", WorkerRole.findAllWorkerRoles());
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

	@RequestMapping(params = { "find=ByLoginEquals", "form" }, method = RequestMethod.GET)
    public String findWorkersByLoginEqualsForm(Model uiModel) {
        return "workers/findWorkersByLoginEquals";
    }

	@RequestMapping(params = "find=ByLoginEquals", method = RequestMethod.GET)
    public String findWorkersByLoginEquals(@RequestParam("login") String login, Model uiModel) {
        uiModel.addAttribute("workers", Worker.findWorkersByLoginEquals(login).getResultList());
        return "workers/list";
    }
}
