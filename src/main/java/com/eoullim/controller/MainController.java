package com.eoullim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/chat")
    public String chatTest(Model model){
        return "chatBaseFormat";
    }
}