package com.diploma.ccms.web;

import com.diploma.ccms.domain.Messages;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/messageses")
@Controller
@RooWebScaffold(path = "messageses", formBackingObject = Messages.class)
public class MessagesController {
}
