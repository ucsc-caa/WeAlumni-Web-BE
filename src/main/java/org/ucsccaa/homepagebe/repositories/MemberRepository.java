package org.ucsccaa.homepagebe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{
    Optional<Member> findByEmail(String email);
    Boolean existsByMemberId(Integer memberId);
    void deleteByMemberId(Integer memberId);
}
