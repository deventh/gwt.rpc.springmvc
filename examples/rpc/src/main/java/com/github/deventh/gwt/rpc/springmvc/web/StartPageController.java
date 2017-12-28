package com.github.deventh.gwt.rpc.springmvc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class StartPageController {
	@RequestMapping("/index")
	String index() {
		return "Application";
	}
}
