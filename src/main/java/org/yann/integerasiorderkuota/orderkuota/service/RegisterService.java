package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.client.login.LoginService;
import org.yann.integerasiorderkuota.orderkuota.client.otp.GenerateOTP;
import org.yann.integerasiorderkuota.orderkuota.dto.GenerateOtpRequest;
import org.yann.integerasiorderkuota.orderkuota.dto.RegisterDTO;
import org.yann.integerasiorderkuota.orderkuota.dto.RegisterResponse;
import org.yann.integerasiorderkuota.orderkuota.dto.RequestRegisterDTO;
import org.yann.integerasiorderkuota.orderkuota.entity.User;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private static final Logger log = LoggerFactory.getLogger(RegisterService.class);
    private final UserService userService;
    private final LoginService loginService;
    private final GenerateOTP generateOTP;

    @Transactional
    public RegisterDTO<?> registerUser(RequestRegisterDTO data) {
        userService.deleteByUsername(data.getUsername());
        userService.saveUsernameAndPassword(data.getUsername(), data.getPassword());

        String otpEmail = generateOTP.generateOTP(data.getUsername(), data.getPassword());

        if (otpEmail == null || otpEmail.contains("Nama pengguna atau kata sandi tidak benar.")) {
            return RegisterDTO.<Map<String, String>>builder()
                    .status("Error")
                    .data(Map.of("error", "Username or password Wrong"))
                    .build();
        }

        return RegisterDTO.builder()
                .status("Success")
                .data(Map.of("email", otpEmail))
                .build();
    }

    public RegisterDTO<?> verifyOtp(GenerateOtpRequest request) {
        String session = loginService.loginOrkut(request.getUsername(), request.getOtp());
        if (session == null) {
            return RegisterDTO.<Map<String, String>>builder()
                    .status("Error")
                    .data(Map.of("error", "OTP was wrong"))
                    .build();
        }

        User userDetails = userService.getUserDetailsByUsername(request.getUsername());
        RegisterResponse registerResponse = new RegisterResponse(userDetails);
        log.info("User {} successfully registered", request.getUsername());

        return RegisterDTO.<RegisterResponse>builder()
                .status("OK")
                .data(registerResponse)
                .build();
    }
}
