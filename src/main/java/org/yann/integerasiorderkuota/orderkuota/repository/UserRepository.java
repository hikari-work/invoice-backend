package org.yann.integerasiorderkuota.orderkuota.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.orderkuota.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsUserById(String id);
}
