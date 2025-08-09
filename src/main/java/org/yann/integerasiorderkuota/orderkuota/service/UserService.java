package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.client.login.LoginService;
import org.yann.integerasiorderkuota.orderkuota.client.otp.GenerateOTP;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.dto.*;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.exception.DuplicateUsername;
import org.yann.integerasiorderkuota.orderkuota.exception.UserNotFoundException;
import org.yann.integerasiorderkuota.orderkuota.repository.UserRepository;
import org.yann.integerasiorderkuota.orderkuota.security.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final QRService qRService;
	private final GenerateOTP generateOTP;
	private final LoginService loginService;

	@Transactional
	public void saveUsernameAndPassword(String username, String password) {
		Optional<User> byUsername = userRepository.findByUsername(username);
		if (byUsername.isPresent()) {
			throw new DuplicateUsername("Username Already Registered");
		}
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userRepository.save(User.builder()
						.username(username)
						.password(hashedPassword)
				.build());
	}
	@Transactional
	public void saveEmail(String email, String username) {
		userRepository.findByUsername(username).ifPresent(user -> {
			user.setEmail(email);
			userRepository.save(user);
		});
	}
	@Transactional
	public void saveToken(String token, String username) {
		userRepository.findByUsername(username).ifPresent(user -> {
			user.setToken(token);
			userRepository.save(user);
		});
	}
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	public User getUserDetailsByUUID(String uuid) {
		return userRepository.findById(uuid).orElse(null);
	}
	public User getUserDetailsByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}
	public boolean isValidToken(String token) {
		return userRepository.existsUserById(token);
	}
	@Transactional
	public void deleteByUsername(String username) {
		userRepository.deleteByUsername(username);
	}

	@Transactional
	public void updateUserString(SettlementDTO dto, String username) {
		byte[] qrisImage = getQrisImage(dto.getAccount().getResults().getQris());
		userRepository.findByUsername(username).ifPresent(user -> {
			user.setQrisImage(qrisImage);
			user.setQrisString(qRService.decode(qrisImage));
			userRepository.save(user);
		});
	}

	public byte[] getQrisImage(String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.IMAGE_PNG));
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<byte[]> responseEntity = new RestTemplateBuilder().build()
				.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
		return responseEntity.getBody();
	}
	@Transactional
	public void updateUser(String username, UpdateRequestUser requestUser) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User Not Found"));
		user.setUsername(user.getUsername());
		if (requestUser.getEmail() != null) {
			user.setEmail(requestUser.getEmail());
		}
		if (requestUser.getCallbackUrl() != null) {
			user.setCallbackUrl(requestUser.getCallbackUrl());
		}
		if (requestUser.getQrisString() != null) {
			user.setQrisString(requestUser.getQrisString());
		}
		if (requestUser.getQrisImage() != null) {
			user.setQrisImage(requestUser.getQrisImage());
		}
		userRepository.save(user);
	}

	@Transactional
	public RegisterDTO<?> registerUser(RequestRegisterDTO data) {
		deleteByUsername(data.getUsername());
		saveUsernameAndPassword(data.getUsername(), data.getPassword());

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

		User userDetails = getUserDetailsByUsername(request.getUsername());
		RegisterResponse registerResponse = new RegisterResponse(userDetails);

		return RegisterDTO.<RegisterResponse>builder()
				.status("OK")
				.data(registerResponse)
				.build();
	}


}
