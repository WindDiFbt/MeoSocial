package com.web.meosocial.rest;

import com.web.meosocial.dto.ChangePasswordDto;
import com.web.meosocial.dto.UserDto;
import com.web.meosocial.dto.UserInfoDto;
import com.web.meosocial.service.UserInfoService;
import com.web.meosocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class RestUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return ResponseEntity.ok().body("User added successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("User login successful");
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok().body(userService.changePassword(changePasswordDto));
    }

    @PutMapping("/status/change/{id}")
    public ResponseEntity<?> changeStatusUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateStatus(id, userDto));
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        UserInfoDto userInfoDto = userInfoService.getUserInfo(id);
        if (userInfoDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userInfoDto);
    }

    @PatchMapping("/profiles/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.ok().body(userInfoService.updateInformationUser(userInfoDto));
    }

    @PostMapping("/profiles/{id}")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatar) {
        try {
            userInfoService.updateUserAvatar(id, avatar);
            return ResponseEntity.ok().body("Avatar updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload avatar: " + e.getMessage());
        }
    }
}
