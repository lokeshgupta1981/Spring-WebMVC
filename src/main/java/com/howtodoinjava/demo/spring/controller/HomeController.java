package com.howtodoinjava.demo.spring.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class HomeController 
{
	@GetMapping("/home")
	public String homeInit(Locale locale, Model model) {
		return "home";
	}
}
