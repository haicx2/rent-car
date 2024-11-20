package org.example.rentcar.controller;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.dto.UserDto;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.request.ChangePasswordRequest;
import org.example.rentcar.request.RegisterRequest;
import org.example.rentcar.request.UpdateUserRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.service.password.ChangePasswordService;
import org.example.rentcar.service.user.UserService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.USER)
public class UserController {
    private final UserService userService;
    private final ChangePasswordService changePasswordService;
    @GetMapping(UrlMapping.GET_BY_ID)
    public ResponseEntity<APIResponse> getUserById(@PathVariable long id) {
        UserDto user = userService.getUserDetails(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, user));
    }

    @PostMapping(UrlMapping.REGISTER)
    public ResponseEntity<APIResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, user));
    }

    @PutMapping(UrlMapping.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUserById(id, updateUserRequest);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.UPDATE_SUCCESS, user));
    }

    @DeleteMapping(UrlMapping.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.DELETE_SUCCESS, null));
    }

    @GetMapping(UrlMapping.GET_BY_EMAIL)
    public ResponseEntity<APIResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, user));
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, userDtos));
    }

    @PutMapping(UrlMapping.CHANGE_PASSWORD)
    public ResponseEntity<APIResponse> changePassword(@PathVariable Long userId,
                                                      @RequestBody ChangePasswordRequest request) {
        try {
            changePasswordService.changePassword(userId, request);
            return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, null));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), null));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }
}
