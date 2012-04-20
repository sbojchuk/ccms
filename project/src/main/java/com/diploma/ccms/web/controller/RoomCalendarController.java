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

import com.diploma.ccms.domain.Room;
import com.diploma.ccms.domain.RoomCalendar;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/roomcalendars")
@Controller

public class RoomCalendarController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid RoomCalendar roomCalendar, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, roomCalendar);
            return "roomcalendars/create";
        }
        uiModel.asMap().clear();
        roomCalendar.persist();
        return "redirect:/roomcalendars/" + encodeUrlPathSegment(roomCalendar.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new RoomCalendar());
        return "roomcalendars/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("roomcalendar", RoomCalendar.findRoomCalendar(id));
        uiModel.addAttribute("itemId", id);
        return "roomcalendars/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("roomcalendars", RoomCalendar.findRoomCalendarEntries(firstResult, sizeNo));
            float nrOfPages = (float) RoomCalendar.countRoomCalendars() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("roomcalendars", RoomCalendar.findAllRoomCalendars());
        }
        return "roomcalendars/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid RoomCalendar roomCalendar, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, roomCalendar);
            return "roomcalendars/update";
        }
        uiModel.asMap().clear();
        roomCalendar.merge();
        return "redirect:/roomcalendars/" + encodeUrlPathSegment(roomCalendar.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, RoomCalendar.findRoomCalendar(id));
        return "roomcalendars/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        RoomCalendar roomCalendar = RoomCalendar.findRoomCalendar(id);
        roomCalendar.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/roomcalendars";
    }

	void populateEditForm(Model uiModel, RoomCalendar roomCalendar) {
        uiModel.addAttribute("roomCalendar", roomCalendar);
        uiModel.addAttribute("rooms", Room.findAllRooms());
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
}
