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

import com.diploma.ccms.domain.Wiki;
import com.diploma.ccms.domain.WikiCategory;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/wikis")
@Controller


public class WikiController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Wiki wiki, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, wiki);
            return "wikis/create";
        }
        uiModel.asMap().clear();
        wiki.persist();
        return "redirect:/wikis/" + encodeUrlPathSegment(wiki.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Wiki());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (WikiCategory.countWikiCategorys() == 0) {
            dependencies.add(new String[] { "wikicategory", "wikicategorys" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "wikis/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("wiki", Wiki.findWiki(id));
        uiModel.addAttribute("itemId", id);
        return "wikis/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("wikis", Wiki.findWikiEntries(firstResult, sizeNo));
            float nrOfPages = (float) Wiki.countWikis() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("wikis", Wiki.findAllWikis());
        }
        addDateTimeFormatPatterns(uiModel);
        return "wikis/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Wiki wiki, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, wiki);
            return "wikis/update";
        }
        uiModel.asMap().clear();
        wiki.merge();
        return "redirect:/wikis/" + encodeUrlPathSegment(wiki.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Wiki.findWiki(id));
        return "wikis/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Wiki wiki = Wiki.findWiki(id);
        wiki.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/wikis";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("wiki_enterdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Wiki wiki) {
        uiModel.addAttribute("wiki", wiki);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("wikicategorys", WikiCategory.findAllWikiCategorys());
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

	@RequestMapping(params = { "find=ByCategory", "form" }, method = RequestMethod.GET)
    public String findWikisByCategoryForm(Model uiModel) {
        uiModel.addAttribute("wikicategorys", WikiCategory.findAllWikiCategorys());
        return "wikis/findWikisByCategory";
    }

	@RequestMapping(params = "find=ByCategory", method = RequestMethod.GET)
    public String findWikisByCategory(@RequestParam("category") WikiCategory category, Model uiModel) {
        uiModel.addAttribute("wikis", Wiki.findWikisByCategory(category).getResultList());
        return "wikis/list";
    }

	@RequestMapping(params = { "find=ByTitleLike", "form" }, method = RequestMethod.GET)
    public String findWikisByTitleLikeForm(Model uiModel) {
        return "wikis/findWikisByTitleLike";
    }

	@RequestMapping(params = "find=ByTitleLike", method = RequestMethod.GET)
    public String findWikisByTitleLike(@RequestParam("title") String title, Model uiModel) {
        uiModel.addAttribute("wikis", Wiki.findWikisByTitleLike(title).getResultList());
        return "wikis/list";
    }
}
