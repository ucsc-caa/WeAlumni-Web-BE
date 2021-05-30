package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT COALESCE(MAX(uid), 0) + 1 FROM User")
    Integer getNextUid();
    Optional<User> findByUid(Integer uid);
}
