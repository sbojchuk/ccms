package com.diploma.ccms.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import com.diploma.ccms.domain.Document;
import com.diploma.ccms.domain.DocumentCategory;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/documents")
@Controller
public class DocumentController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Document document, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, document);
            return "documents/create";
        }
        uiModel.asMap().clear();
        document.persist();
        return "redirect:/documents/" + encodeUrlPathSegment(document.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Document());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (DocumentCategory.countDocumentCategorys() == 0) {
            dependencies.add(new String[] { "documentcategory", "documentcategorys" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "documents/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("document", Document.findDocument(id));
        uiModel.addAttribute("itemId", id);
        return "documents/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("documents", Document.findDocumentEntries(firstResult, sizeNo));
            float nrOfPages = (float) Document.countDocuments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("documents", Document.findAllDocuments());
        }
        return "documents/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Document document, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, document);
            return "documents/update";
        }
        uiModel.asMap().clear();
        document.merge();
        return "redirect:/documents/" + encodeUrlPathSegment(document.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Document.findDocument(id));
        return "documents/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Document document = Document.findDocument(id);
        document.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/documents";
    }

    void populateEditForm(Model uiModel, Document document) {
        uiModel.addAttribute("document", document);
        uiModel.addAttribute("documentcategorys", DocumentCategory.findAllDocumentCategorys());
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

    @RequestMapping(params = { "find=ByDescriptionLike", "form" }, method = RequestMethod.GET)
    public String findDocumentsByDescriptionLikeForm(Model uiModel) {
        return "documents/findDocumentsByDescriptionLike";
    }

    @RequestMapping(params = "find=ByDescriptionLike", method = RequestMethod.GET)
    public String findDocumentsByDescriptionLike(@RequestParam("description") String description, Model uiModel) {
        uiModel.addAttribute("documents", Document.findDocumentsByDescriptionLike(description).getResultList());
        return "documents/list";
    }

    @RequestMapping(params = { "find=ByTitleLike", "form" }, method = RequestMethod.GET)
    public String findDocumentsByTitleLikeForm(Model uiModel) {
        return "documents/findDocumentsByTitleLike";
    }

    @RequestMapping(params = "find=ByTitleLike", method = RequestMethod.GET)
    public String findDocumentsByTitleLike(@RequestParam("title") String title, Model uiModel) {
        uiModel.addAttribute("documents", Document.findDocumentsByTitleLike(title).getResultList());
        return "documents/list";
    }
}
