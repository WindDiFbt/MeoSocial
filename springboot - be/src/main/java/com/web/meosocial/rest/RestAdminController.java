package com.web.meosocial.rest;

import com.web.meosocial.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class RestAdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/all")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().body("Admin login successful");
    }
}
