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
import com.diploma.ccms.domain.BlogComment;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/blogcomments")
@Controller
public class BlogCommentController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BlogComment blogComment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, blogComment);
            return "blogcomments/create";
        }
        uiModel.asMap().clear();
        blogComment.persist();
        return "redirect:/blogcomments/" + encodeUrlPathSegment(blogComment.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BlogComment());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (Blog.countBlogs() == 0) {
            dependencies.add(new String[] { "blog", "blogs" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "blogcomments/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("blogcomment", BlogComment.findBlogComment(id));
        uiModel.addAttribute("itemId", id);
        return "blogcomments/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("blogcomments", BlogComment.findBlogCommentEntries(firstResult, sizeNo));
            float nrOfPages = (float) BlogComment.countBlogComments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("blogcomments", BlogComment.findAllBlogComments());
        }
        addDateTimeFormatPatterns(uiModel);
        return "blogcomments/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BlogComment blogComment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, blogComment);
            return "blogcomments/update";
        }
        uiModel.asMap().clear();
        blogComment.merge();
        return "redirect:/blogcomments/" + encodeUrlPathSegment(blogComment.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BlogComment.findBlogComment(id));
        return "blogcomments/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BlogComment blogComment = BlogComment.findBlogComment(id);
        blogComment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/blogcomments";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("blogComment_enterdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, BlogComment blogComment) {
        uiModel.addAttribute("blogComment", blogComment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("blogs", Blog.findAllBlogs());
        uiModel.addAttribute("workers", Worker.findAllWorkers());
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
