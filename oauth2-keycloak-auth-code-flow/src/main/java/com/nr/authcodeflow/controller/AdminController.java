package com.nr.authcodeflow.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_Admin')")
public class AdminController {

    @GetMapping
    public String hello() {
        return "Hello from Admin Controller";
    }
}
