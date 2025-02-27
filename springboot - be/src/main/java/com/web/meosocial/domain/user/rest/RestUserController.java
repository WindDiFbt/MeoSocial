package com.web.meosocial.domain.user.rest;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.user.dto.ChangePasswordDto;
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
    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/password/change")
    public ResponseEntity<?> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok().body(userService.changePassword(authUtils.getCurrentUserId(), changePasswordDto));
    }

    @PutMapping("/status/change/{status}")
    public ResponseEntity<?> changeStatusUser(@PathVariable Integer status) {
        return ResponseEntity.ok().body(userService.updateStatus(authUtils.getCurrentUserId(), status));
    }

    @GetMapping("/profiles")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok().body(userInfoService.getUserInfo(authUtils.getCurrentUserId()));
    }

    @PatchMapping("/profiles/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.ok().body(userInfoService.updateInformationUser(authUtils.getCurrentUserId(), userInfoDto));
    }

    @PostMapping("/profiles/update/avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return ResponseEntity.ok().body(userInfoService.updateUserAvatar(authUtils.getCurrentUserId(), avatar));
    }

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.followUser(authUtils.getCurrentUserId(), userRelationshipDto));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.unfollowUser(authUtils.getCurrentUserId(), userRelationshipDto));
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.blockUser(authUtils.getCurrentUserId(), userRelationshipDto));
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestBody UserRelationshipDto userRelationshipDto) {
        return ResponseEntity.ok().body(userRelationshipService.unblockUser(authUtils.getCurrentUserId(), userRelationshipDto));
    }
}
