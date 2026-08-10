package com.fitpass.membership.service;

import com.fitpass.membership.entity.UserMembership;
import com.fitpass.membership.repository.UserMembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMembershipService {

    private final UserMembershipRepository userMembershipRepository;

    public UserMembershipService(
            UserMembershipRepository userMembershipRepository) {
        this.userMembershipRepository = userMembershipRepository;
    }

    public List<UserMembership> findAll() {
        return userMembershipRepository.findAll();
    }

    public UserMembership findById(Long id) {
        return userMembershipRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "UserMembership not found with id: " + id
                        ));
    }

    public UserMembership save(UserMembership userMembership) {
        return userMembershipRepository.save(userMembership);
    }

    public UserMembership update(
            Long id,
            UserMembership userMembership) {

        UserMembership existingUserMembership = findById(id);

        existingUserMembership.setUser(userMembership.getUser());
        existingUserMembership.setMembership(
                userMembership.getMembership()
        );
        existingUserMembership.setStartDate(
                userMembership.getStartDate()
        );
        existingUserMembership.setEndDate(
                userMembership.getEndDate()
        );
        existingUserMembership.setStatus(
                userMembership.getStatus()
        );

        return userMembershipRepository.save(existingUserMembership);
    }

    public void delete(Long id) {
        UserMembership userMembership = findById(id);
        userMembershipRepository.delete(userMembership);
    }
}