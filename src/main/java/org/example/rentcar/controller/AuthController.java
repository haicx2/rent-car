package org.example.rentcar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.event.RegistrationCompleteEvent;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.model.VerificationToken;
import org.example.rentcar.request.LoginRequest;
import org.example.rentcar.request.PasswordResetRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.response.JwtResponse;
import org.example.rentcar.security.jwt.JwtUtils;
import org.example.rentcar.security.user.UPCUserDetails;
import org.example.rentcar.service.password.PasswordResetService;
import org.example.rentcar.service.token.VerificationTokenService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(UrlMapping.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final VerificationTokenService tokenService;
    private final PasswordResetService passwordResetService;
    private final ApplicationEventPublisher publisher;

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

    @GetMapping(UrlMapping.VERIFY_EMAIL)
    public ResponseEntity<APIResponse> verifyEmail(@RequestParam("token")    String token) {
        String result =   tokenService.validateToken(token);
        return  switch (result){
            case "VALID" -> ResponseEntity.ok(new APIResponse(FeedBackMessage.VALID_VERIFICATION_TOKEN, null));
            case "VERIFIED" -> ResponseEntity.ok(new APIResponse(FeedBackMessage.TOKEN_ALREADY_VERIFIED, null));
            case "EXPIRED" ->
                    ResponseEntity.status(HttpStatus.GONE).body(new APIResponse(FeedBackMessage.EXPIRED_TOKEN, null));
            case "INVALID" ->
                    ResponseEntity.status(HttpStatus.GONE).body(new APIResponse(FeedBackMessage.INVALID_VERIFICATION_TOKEN, null));
            default -> ResponseEntity.internalServerError().body(new APIResponse(FeedBackMessage.ERROR, null));

        } ;
    }


    @PutMapping(UrlMapping.RESEND_VERIFICATION_TOKEN)
    public ResponseEntity<APIResponse> resendVerificationToken(@RequestParam("token") String oldToken) {
        try {
            VerificationToken verificationToken = tokenService.generateNewVerificationToken(oldToken);
            User theUser = verificationToken.getUser();
            publisher.publishEvent(new RegistrationCompleteEvent(theUser));
            return ResponseEntity.ok(new APIResponse(FeedBackMessage.NEW_VERIFICATION_TOKEN_SENT, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping(UrlMapping.REQUEST_PASSWORD_RESET)
    public ResponseEntity<APIResponse> requestPasswordReset(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        if (email == null || email.trim().isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new APIResponse(FeedBackMessage.INVALID_EMAIL, null));
        }
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.
                    ok(new APIResponse(FeedBackMessage.PASSWORD_RESET_EMAIL_SENT, null));
        }  catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new APIResponse(ex.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping(UrlMapping.RESET_PASSWORD)
    public  ResponseEntity<APIResponse> resetPassword(@RequestBody PasswordResetRequest request){
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        if (token == null || token.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()){
            return  ResponseEntity.badRequest().body(new APIResponse(FeedBackMessage.MISSING_PASSWORD, null));
        }
        Optional<User> theUser = passwordResetService.findUserByPasswordResetToken(token);
        if (theUser.isEmpty()){
            return  ResponseEntity.badRequest().body(new APIResponse(FeedBackMessage.INVALID_RESET_TOKEN, null));
        }
        User user = theUser.get();
        String message = passwordResetService.resetPassword(newPassword, user) ;
        return ResponseEntity.ok(new APIResponse(message, null));
    }
}
