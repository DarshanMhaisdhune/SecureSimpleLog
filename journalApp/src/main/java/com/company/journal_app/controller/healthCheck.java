package com.company.journal_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthCheck {
    @GetMapping("/health-Check")
    public String healthCheck(){
        return "ok " ;
    }


}
