package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.exception.DuplicateUsername;
import org.yann.integerasiorderkuota.orderkuota.repository.UserRepository;
import org.yann.integerasiorderkuota.orderkuota.security.BCrypt;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

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

}
