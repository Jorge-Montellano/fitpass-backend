package com.fitpass.membership.service;

import com.fitpass.common.messaging.RabbitMQConfig;
import com.fitpass.membership.dto.MembershipPurchaseRequest;
import com.fitpass.membership.entity.Membership;
import com.fitpass.membership.entity.UserMembership;
import com.fitpass.membership.event.MembershipPurchaseRequestedEvent;
import com.fitpass.membership.repository.MembershipRepository;
import com.fitpass.membership.repository.UserMembershipRepository;
import com.fitpass.user.entity.User;
import com.fitpass.user.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMembershipService {

    private final UserMembershipRepository userMembershipRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final RabbitTemplate rabbitTemplate;

    public UserMembershipService(
            UserMembershipRepository userMembershipRepository,
            UserRepository userRepository,
            MembershipRepository membershipRepository,
            RabbitTemplate rabbitTemplate) {

        this.userMembershipRepository = userMembershipRepository;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.rabbitTemplate = rabbitTemplate;
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

    public UserMembership purchase(
            MembershipPurchaseRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: "
                                        + request.userId()
                        ));

        Membership membership =
                membershipRepository
                        .findById(request.membershipId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Membership not found with id: "
                                                + request.membershipId()
                                ));

        LocalDate startDate = LocalDate.now();

        LocalDate endDate =
                startDate.plusDays(membership.getDurationDays());

        UserMembership userMembership =
                new UserMembership();

        userMembership.setUser(user);
        userMembership.setMembership(membership);
        userMembership.setStartDate(startDate);
        userMembership.setEndDate(endDate);

        // 0 = PENDING
        userMembership.setStatus(0);

        UserMembership saved =
                userMembershipRepository.save(userMembership);

        MembershipPurchaseRequestedEvent event =
                new MembershipPurchaseRequestedEvent(
                        saved.getId(),
                        user.getId(),
                        membership.getId(),
                        membership.getPrice(),
                        LocalDateTime.now()
                );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.MEMBERSHIP_PURCHASE_ROUTING_KEY,
                event
        );

        return saved;
    }
}