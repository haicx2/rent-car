package org.example.rentcar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.request.LoginRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.response.JwtResponse;
import org.example.rentcar.security.jwt.JwtUtils;
import org.example.rentcar.security.user.UPCUserDetails;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(UrlMapping.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping(UrlMapping.LOGIN)
    public ResponseEntity<APIResponse> login(@Valid @RequestBody LoginRequest request) {
            Authentication authentication =
                    authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            UPCUserDetails userDetails = (UPCUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(new APIResponse(FeedBackMessage.AUTHENTICATION_SUCCESS, jwtResponse));
    }
}
