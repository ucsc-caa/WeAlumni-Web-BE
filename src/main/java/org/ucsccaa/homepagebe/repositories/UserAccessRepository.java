package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.UserAccess;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, String> {
    @Query(value = "SELECT isAdmin FROM UserAccess WHERE email = ?1")
    Boolean isAdminAccess(String email);
}
