package com.fitpass.membership.service;

import com.fitpass.membership.entity.Membership;
import com.fitpass.membership.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    public Membership findById(Long id) {
        return membershipRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Membership not found with id: " + id));
    }

    public Membership save(Membership membership) {
        return membershipRepository.save(membership);
    }

    public Membership update(Long id, Membership membership) {

        Membership existingMembership = findById(id);

        existingMembership.setName(membership.getName());
        existingMembership.setDescription(membership.getDescription());
        existingMembership.setPrice(membership.getPrice());
        existingMembership.setDurationDays(membership.getDurationDays());
        existingMembership.setStatus(membership.getStatus());

        return membershipRepository.save(existingMembership);
    }

    public void delete(Long id) {
        Membership membership = findById(id);
        membershipRepository.delete(membership);
    }
}