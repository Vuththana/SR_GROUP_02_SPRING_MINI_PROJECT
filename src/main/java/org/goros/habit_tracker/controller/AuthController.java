package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.jwt.JwtService;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.request.AuthRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.model.response.ApiResponseVoid;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.model.response.AuthResponse;
import org.goros.habit_tracker.service.AppUserService;
import org.goros.habit_tracker.service.OtpService;
import org.goros.habit_tracker.service.RedisUserCacheService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auths")
@Validated
public class AuthController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticateManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final OtpService otpService;
    private final RedisUserCacheService redisUserCacheService;

    public void authenticate(String identifier, String password) throws Exception {
        try {
            authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username, email, or password. Please check your credentials and try again.", e);
        }
    }
    @PostMapping("/resend")
    @Operation(summary = "Resend verification OTP")
    public ResponseEntity<ApiResponseVoid> sendOtp(@RequestParam String email) throws Exception {
        String verificationKey = "verify:" + email;
        AppUser cachedUser = redisUserCacheService.getUser(verificationKey);
        if (cachedUser == null) {
            throw new RuntimeException("User not found or already verified.");
        }
        otpService.generateAndSendOtp(email, verificationKey);
        ApiResponseVoid response = ResponseUtil.successVoid(HttpStatus.OK, "If an account with this email exists, an OTP has been sent. Please check your inbox.");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<ApiResponseVoid> verifyOtp(@RequestParam String email,
                                                     @RequestParam String otpCode) {
        boolean valid = otpService.verifyOtp(email, otpCode);
        ApiResponseVoid response = valid ? ResponseUtil.successVoid(HttpStatus.OK, "OTP verified, you can now login.")
                : ResponseUtil.errorVoid(HttpStatus.BAD_REQUEST, "Wrong OTP code, please try again");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getIdentifier(), request.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(request.getIdentifier());
        final String token = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);
        ApiResponse<AuthResponse> response = ResponseUtil.success(HttpStatus.OK, "Login successfully! Authentication token generated", authResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AppUserResponse>> register(@Valid @RequestBody AppUserRequest request) throws Exception {
        AppUserResponse cachedUserResponse = appUserService.register(request);
        String verificationKey = "verify:" + request.getEmail();
        otpService.generateAndSendOtp(request.getEmail(), verificationKey);

        ApiResponse<AppUserResponse> response = ResponseUtil.success(HttpStatus.OK, "Registered successfully, you will need to verify your email to login", cachedUserResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
