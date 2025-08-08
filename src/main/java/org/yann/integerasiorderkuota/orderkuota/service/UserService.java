package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.dto.UpdateRequestUser;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.exception.DuplicateUsername;
import org.yann.integerasiorderkuota.orderkuota.exception.UserNotFoundException;
import org.yann.integerasiorderkuota.orderkuota.repository.UserRepository;
import org.yann.integerasiorderkuota.orderkuota.security.BCrypt;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final QRService qRService;

	@Transactional
	public void saveUsernameAndPassword(String username, String password) {
		Optional<User> byUsername = userRepository.findByUsername(username);
		if (byUsername.isPresent()) {
			throw new DuplicateUsername("Username Already Registered");
		}
		String hashpwed = BCrypt.hashpw(password, BCrypt.gensalt());
		userRepository.save(User.builder()
						.username(username)
						.password(hashpwed)
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

}
