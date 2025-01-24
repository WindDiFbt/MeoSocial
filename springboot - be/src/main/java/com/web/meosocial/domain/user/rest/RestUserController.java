package com.web.meosocial.domain.user.rest;

import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.domain.user.dto.UserRelationshipDto;
import com.web.meosocial.domain.user.service.UserInfoService;
import com.web.meosocial.domain.user.service.UserRelationshipService;
import com.web.meosocial.domain.user.service.UserService;
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
    @Autowired
    private UserRelationshipService userRelationshipService;

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

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.followUser(userRelationshipDto));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.unfollowUser(userRelationshipDto));
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.blockUser(userRelationshipDto));
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.unblockUser(userRelationshipDto));
    }
}
