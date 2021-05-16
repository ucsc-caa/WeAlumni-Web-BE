package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUid(Integer uid);
    Optional<User> findByEmail(String email);
}
