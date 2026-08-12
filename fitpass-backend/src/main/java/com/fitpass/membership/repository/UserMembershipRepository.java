package com.fitpass.membership.repository;

import com.fitpass.membership.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long>{
}
