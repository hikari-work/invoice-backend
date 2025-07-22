package org.yann.integerasiorderkuota.orderkuota.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yann.integerasiorderkuota.orderkuota.client.login.LoginService;
import org.yann.integerasiorderkuota.orderkuota.client.otp.GenerateOTP;
import org.yann.integerasiorderkuota.orderkuota.dto.GenerateOtpRequest;
import org.yann.integerasiorderkuota.orderkuota.dto.RegisterDTO;
import org.yann.integerasiorderkuota.orderkuota.dto.RegisterResponse;
import org.yann.integerasiorderkuota.orderkuota.dto.RequestRegisterDTO;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final LoginService loginService;
    private final GenerateOTP generateOTP;
    private final UserService userService;

    public RegisterController(LoginService loginService, GenerateOTP generateOTP, UserService userService) {
        this.loginService = loginService;
        this.generateOTP = generateOTP;
        this.userService = userService;
    }

    @PostMapping(value = "/auth",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterDTO<?>> register(@Valid @RequestBody RequestRegisterDTO register) {
        userService.deleteByUsername(register.getUsername());
        userService.saveUsernameAndPassword(register.getUsername(), register.getPassword());
        String s = generateOTP.generateOTP(register.getUsername(), register.getPassword());
        if (s == null || s.contains("Nama pengguna atau kata sandi tidak benar.")) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Username or password Wrong");
            return ResponseEntity.badRequest().body(RegisterDTO.<Map<String, String>>builder()
                    .status("Error")
                    .data(map)
                    .build());
        }
        return ResponseEntity.ok(RegisterDTO.builder().status("Success")
                .data(Map.of("email", s))
                .build());
    }
    @PostMapping(value = "/auth/otp", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterDTO<?>> registerOtp(@Valid @RequestBody GenerateOtpRequest otpRequest) {
        String s = loginService.loginOrkut(otpRequest.getUsername(), otpRequest.getOtp());
        if (s == null) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "OTP was wrong");
            return ResponseEntity.badRequest().body(RegisterDTO.<Map<String, String>>builder()
                    .status("Error")
                    .data(map)
                    .build());
        }
        User userDetailsByUsername = userService.getUserDetailsByUsername(otpRequest.getUsername());
        RegisterResponse registerResponse = new RegisterResponse(userDetailsByUsername);
        return ResponseEntity.ok(RegisterDTO.<RegisterResponse>builder()
                        .status("OK")
                        .data(registerResponse)
                .build());
    }
}
