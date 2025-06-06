package com.bookchain.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/chat.html")
    public String chatPage() {
        return "chat";
    }
}
