package org.yann.integerasiorderkuota;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yann.integerasiorderkuota.dto.RequestRegisterDTO;
import org.yann.integerasiorderkuota.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Register Ok")
    void getOtpOk() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("viandrastefani")
                .password("17April2002!")
                .build();
        mockMvc.perform(
                post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Success"));
    }
    @Test
    @DisplayName("Get OTP Failed")
    void getOtpFailUsernameOrPasswordNotOK() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("viandrastefa")
                .password("17April2002!")
                .build();
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Error"),
                        jsonPath("$.data.error").value("Username or password Wrong"));
    }
    @Test
    @DisplayName("Get OTP Failed Username Empty")
    void getOtpFailUsernameIsEmpty() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("")
                .password("17April2002!")
                .build();
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Error"),
                        jsonPath("$.data.username").value("Username Not Be Empty"));
    }
    @Test
    @DisplayName("Get OTP Failed Password Empty")
    void getOtpFailPasswordIsEmpty() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("jokoali")
                .password("")
                .build();
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Error"),
                        jsonPath("$.data.password").value("Password Not Be Empty"));
    }
    @Test
    @DisplayName("Get OTP Failed Password And Username Empty")
    void getOtpFailPasswordAndUsernameIsEmpty() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("")
                .password("")
                .build();
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Error"),
                        jsonPath("$.data.password").value("Password Not Be Empty"),
                        jsonPath("$.data.username").value("Username Not Be Empty"));
    }
    @Test
    @DisplayName("Get OTP Duplicate Username")
    void getOtpDuplicateUsername() throws Exception{
        RequestRegisterDTO dto = RequestRegisterDTO.builder()
                .username("viandrastefani")
                .password("17April2002!")
                .build();
        mockMvc.perform(
                post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status").value("Error"),
                        jsonPath("$.data.username").value("Username Already Registered"));
    }
}
