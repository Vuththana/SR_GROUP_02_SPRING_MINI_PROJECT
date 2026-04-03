package org.goros.habit_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.jwt.JwtService;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.request.AuthRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.model.response.AuthResponse;
import org.goros.habit_tracker.service.AppUserService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auths")
public class AuthController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticateManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public void authenticate(String identifier, String password) throws Exception {
        try {
            System.out.println("This is authenticate");
            authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
            System.out.println("Success");
        } catch (
                DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (
                BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getIdentifier(), request.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(request.getIdentifier());
        final String token = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);
        ApiResponse<AuthResponse> response = ResponseUtil.success(HttpStatus.OK, "Login successfully! Authentication token generated", authResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserResponse> register(@RequestBody AppUserRequest request){
        AppUserResponse response = modelMapper.map(appUserService.register(request), AppUserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{user-id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("user-id") UUID appUserId) {
        return ResponseEntity.status(HttpStatus.OK).body(appUserService.getUserById(appUserId));
    }
}
