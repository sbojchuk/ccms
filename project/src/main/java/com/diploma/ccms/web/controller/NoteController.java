package com.diploma.ccms.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.diploma.ccms.domain.Note;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/notes")
@Controller
public class NoteController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Note note, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, note);
            uiModel.addAttribute("menu", "NOTE");
            return "redirect:/notes";
        }
        uiModel.asMap().clear();
        note.setAuthor(Worker.getPrincipal());
        note.setDatetime(new Date());
        note.persist();
        uiModel.addAttribute("menu", "NOTE");
        return "redirect:/notes";// + encodeUrlPathSegment(note.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Note());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        uiModel.addAttribute("menu", "NOTE");
        return "notes/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("note", Note.findNote(id));
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("menu", "NOTE");
        return "notes/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        // if (page != null || size != null) {
        // int sizeNo = size == null ? 10 : size.intValue();
        // final int firstResult = page == null ? 0 : (page.intValue() - 1) *
        // sizeNo;
        // uiModel.addAttribute("notes", Note.findNoteEntries(firstResult,
        // sizeNo));
        // float nrOfPages = (float) Note.countNotes() / sizeNo;
        // uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
        // || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        // } else {
        uiModel.addAttribute("notes", Note.findNotesByAuthorEquals(Worker.getPrincipal()).getResultList());
        // }
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("menu", "NOTE");
        return "notes/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Note note, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, note);
            return "notes/update";
        }
        uiModel.asMap().clear();
        note.merge();
        uiModel.addAttribute("menu", "NOTE");
        return "redirect:/notes/" + encodeUrlPathSegment(note.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Note.findNote(id));
        uiModel.addAttribute("menu", "NOTE");
        return "notes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Note note = Note.findNote(id);
        note.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        uiModel.addAttribute("menu", "NOTE");
        return "redirect:/notes";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("note_datetime_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Note note) {
        uiModel.addAttribute("note", note);
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
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    @RequestMapping(params = { "find=ByAuthorEquals", "form" }, method = RequestMethod.GET)
    public String findNotesByAuthorEqualsForm(Model uiModel) {
        uiModel.addAttribute("workers", Worker.findAllWorkers());
        uiModel.addAttribute("menu", "NOTE");
        return "notes/findNotesByAuthorEquals";
    }

    @RequestMapping(params = "find=ByAuthorEquals", method = RequestMethod.GET)
    public String findNotesByAuthorEquals(@RequestParam("author") Worker author, Model uiModel) {
        uiModel.addAttribute("notes", Note.findNotesByAuthorEquals(author).getResultList());
        uiModel.addAttribute("menu", "NOTE");
        return "notes/list";
    }

    @RequestMapping(value = "/get_note", method = RequestMethod.GET)
    @ResponseBody
    public String getNote(@RequestParam("id") String id) {
        return Note.findNote(Long.valueOf(id)).getText();
    }

}
