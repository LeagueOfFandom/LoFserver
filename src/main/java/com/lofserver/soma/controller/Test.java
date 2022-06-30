package com.lofserver.soma.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Test {

    @GetMapping("/test")
    public String test(){
        return "okay~";
    }
}
