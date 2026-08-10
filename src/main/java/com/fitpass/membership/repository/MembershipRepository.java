package com.fitpass.membership.repository;

import com.fitpass.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MembershipRepository extends JpaRepository<Membership, Long>{
}
