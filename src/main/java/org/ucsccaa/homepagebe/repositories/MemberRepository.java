package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Member;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Integer>{

}
