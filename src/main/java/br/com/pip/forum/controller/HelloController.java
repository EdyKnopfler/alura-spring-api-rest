package br.com.pip.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	
	@RequestMapping
	@ResponseBody  // a String retornada já é a resposta!
	public String hello() {
		return "hellô paidégua!";
	}

}
