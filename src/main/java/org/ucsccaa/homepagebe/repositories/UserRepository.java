package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
