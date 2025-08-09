package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.*;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.RegisterService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/users")
public class UserController {

    private final UserService userService;
    private final RegisterService registerService;

    public UserController(UserService userService, RegisterService registerService) {
        this.userService = userService;
        this.registerService = registerService;
    }

    @PostMapping(value = "/auth",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterDTO<?>> register(@Valid @RequestBody RequestRegisterDTO register) {
        RegisterDTO<?> registerDTO = registerService.registerUser(register);
        return ResponseEntity.ok(RegisterDTO.builder().status("Success")
                .data(Map.of("email", registerDTO))
                .build());
    }

    @PostMapping(value = "/auth/otp",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterDTO<?>> registerOtp(@Valid @RequestBody GenerateOtpRequest request) {
        RegisterDTO<?> response = registerService.verifyOtp(request);
        return ResponseEntity.status(
                "Error".equalsIgnoreCase(response.getStatus()) ? HttpStatus.BAD_REQUEST : HttpStatus.OK
        ).body(response);
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<UserDetailsResponse> isValidUser(@PathVariable("username") String username) {
        User userDetailsByUsername = userService.getUserDetailsByUsername(username);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(userDetailsByUsername);
        return ResponseEntity.ok(userDetailsResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDetailsResponse> updateUser(@Valid @RequestBody UpdateRequestUser updateRequestUser) {
        String username = updateRequestUser.getUsername();
        userService.updateUser(username, updateRequestUser);
        User userDetailsByUsername = userService.getUserDetailsByUsername(username);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(userDetailsByUsername);
        return ResponseEntity.ok(userDetailsResponse);
    }

}
