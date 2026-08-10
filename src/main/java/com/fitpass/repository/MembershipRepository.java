package com.fitpass.repository;

import com.fitpass.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MembershipRepository extends JpaRepository<Membership, Long>{
}
