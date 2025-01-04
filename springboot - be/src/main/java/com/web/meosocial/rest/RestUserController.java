package com.web.meosocial.rest;

import com.web.meosocial.dto.ChangePasswordDto;
import com.web.meosocial.dto.UserDto;
import com.web.meosocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class RestUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return ResponseEntity.ok().body("User added successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("User login successful");
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok().body(userService.changePassword(changePasswordDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStatusUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateStatus(id, userDto));
    }
}
