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

import com.diploma.ccms.domain.Blog;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/blogs")
@Controller


public class BlogController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Blog blog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, blog);
            return "blogs/create";
        }
        uiModel.asMap().clear();
        blog.persist();
        return "redirect:/blogs/" + encodeUrlPathSegment(blog.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Blog());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "blogs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("blog", Blog.findBlog(id));
        uiModel.addAttribute("itemId", id);
        return "blogs/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("blogs", Blog.findBlogEntries(firstResult, sizeNo));
            float nrOfPages = (float) Blog.countBlogs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("blogs", Blog.findAllBlogs());
        }
        addDateTimeFormatPatterns(uiModel);
        return "blogs/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Blog blog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, blog);
            return "blogs/update";
        }
        uiModel.asMap().clear();
        blog.merge();
        return "redirect:/blogs/" + encodeUrlPathSegment(blog.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Blog.findBlog(id));
        return "blogs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Blog blog = Blog.findBlog(id);
        blog.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/blogs";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("blog_enterdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Blog blog) {
        uiModel.addAttribute("blog", blog);
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

	@RequestMapping(params = { "find=ByBodyLike", "form" }, method = RequestMethod.GET)
    public String findBlogsByBodyLikeForm(Model uiModel) {
        return "blogs/findBlogsByBodyLike";
    }

	@RequestMapping(params = "find=ByBodyLike", method = RequestMethod.GET)
    public String findBlogsByBodyLike(@RequestParam("body") String body, Model uiModel) {
        uiModel.addAttribute("blogs", Blog.findBlogsByBodyLike(body).getResultList());
        return "blogs/list";
    }

	@RequestMapping(params = { "find=ByTitleLike", "form" }, method = RequestMethod.GET)
    public String findBlogsByTitleLikeForm(Model uiModel) {
        return "blogs/findBlogsByTitleLike";
    }

	@RequestMapping(params = "find=ByTitleLike", method = RequestMethod.GET)
    public String findBlogsByTitleLike(@RequestParam("title") String title, Model uiModel) {
        uiModel.addAttribute("blogs", Blog.findBlogsByTitleLike(title).getResultList());
        return "blogs/list";
    }
}
