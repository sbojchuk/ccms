package com.diploma.ccms.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import sun.security.acl.WorldGroupImpl;

import com.diploma.ccms.domain.Calendar;
import com.diploma.ccms.domain.Message;
import com.diploma.ccms.domain.Todo;
import com.diploma.ccms.domain.TodoCategory;
import com.diploma.ccms.domain.Worker;

@RequestMapping("/todoes")
@Controller
public class TodoController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Todo todo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, todo);
            return "redirect:/todoes/";
        }
        uiModel.asMap().clear();
        todo.setAssignee(Worker.getPrincipal());
        todo.setStart(new Date());
        todo.setReporter(Worker.getPrincipal());
        todo.persist();
        uiModel.addAttribute("menu", "TODO");
        return "redirect:/todoes/";// + encodeUrlPathSegment(todo.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Todo());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (Worker.countWorkers() == 0) {
            dependencies.add(new String[] { "worker", "workers" });
        }
        if (TodoCategory.countTodoCategorys() == 0) {
            dependencies.add(new String[] { "todocategory", "todocategorys" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        uiModel.addAttribute("menu", "TODO");
        return "todoes/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("todo", Todo.findTodo(id));
        uiModel.addAttribute("itemId", id);
        return "todoes/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        if (page != null || size != null) {
//            int sizeNo = size == null ? 10 : size.intValue();
//            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//            uiModel.addAttribute("todoes", Todo.findTodoEntries(firstResult, sizeNo));
//            float nrOfPages = (float) Todo.countTodoes() / sizeNo;
//            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
//        } else {
        uiModel.addAttribute("todoes", Todo.findAllTodoes()); 
//        }
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("menu", "TODO");
        return "todoes/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Todo todo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, todo);
            return "todoes/update";
        }
        uiModel.asMap().clear();
        todo.merge();
        uiModel.addAttribute("menu", "TODO");
        return "redirect:/todoes/" + encodeUrlPathSegment(todo.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Todo.findTodo(id));
        return "todoes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Todo todo = Todo.findTodo(id);
        todo.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        uiModel.addAttribute("menu", "TODO");
        return "redirect:/todoes";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("todo_enterdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("todo_duedate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Todo todo) {
        uiModel.addAttribute("todo", todo);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("todocategorys", TodoCategory.findAllTodoCategorys());
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
    
    
    @RequestMapping(value = "/get_todo", method = RequestMethod.GET)
    @ResponseBody
    public String getTodo(@RequestParam("id") String id) {
        Todo todo = Todo.findTodo(Long.valueOf(id));
        return todo.getText();
    }
    
    /**mark todo in done or not done mode
     * @param id
     */
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String done(@RequestParam("id") String id) {
        Todo todo = Todo.findTodo(Long.valueOf(id));
        todo.setDone(!todo.getDone());
        todo.merge();
        return "redirect:/todoes/";
    }
    
    
    
    @RequestMapping(value = "/get_tasks_json", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        List<Todo> todos = Todo.findAllTodoes();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Todo.toJsonArray(todos), headers, HttpStatus.OK);
    }
    
}
