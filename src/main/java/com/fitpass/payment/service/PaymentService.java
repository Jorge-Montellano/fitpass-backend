package com.fitpass.payment.service;

import com.fitpass.membership.entity.UserMembership;
import com.fitpass.membership.repository.UserMembershipRepository;
import com.fitpass.payment.dto.PaymentRequest;
import com.fitpass.payment.entity.Payment;
import com.fitpass.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.fitpass.payment.event.PaymentCompletedEvent;
import org.springframework.context.ApplicationEventPublisher;

import com.fitpass.common.messaging.RabbitMQConfig;
import com.fitpass.payment.event.PaymentCompletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final RabbitTemplate rabbitTemplate;


    public PaymentService(
            PaymentRepository paymentRepository,
            UserMembershipRepository userMembershipRepository,
            RabbitTemplate rabbitTemplate) {

        this.paymentRepository = paymentRepository;
        this.userMembershipRepository = userMembershipRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with id: " + id
                        ));
    }

    public Payment create(PaymentRequest request) {

        UserMembership userMembership =
                userMembershipRepository
                        .findById(request.userMembershipId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "UserMembership not found with id: "
                                                + request.userMembershipId()
                                ));

        Payment payment = new Payment();

        payment.setUserMembership(userMembership);
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(request.paymentStatus());
        payment.setTransactionReference(
                request.transactionReference()
        );

        Payment savedPayment = paymentRepository.save(payment);

        PaymentCompletedEvent event =
                new PaymentCompletedEvent(
                        savedPayment.getId(),
                        savedPayment.getUserMembership().getId(),
                        savedPayment.getAmount(),
                        savedPayment.getPaymentMethod(),
                        LocalDateTime.now()
                );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                event
        );

        return savedPayment;
    }

    public Payment update(
            Long id,
            PaymentRequest request) {

        Payment existingPayment = findById(id);

        UserMembership userMembership =
                userMembershipRepository
                        .findById(request.userMembershipId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "UserMembership not found with id: "
                                                + request.userMembershipId()
                                ));

        existingPayment.setUserMembership(userMembership);
        existingPayment.setAmount(request.amount());
        existingPayment.setPaymentDate(request.paymentDate());
        existingPayment.setPaymentMethod(request.paymentMethod());
        existingPayment.setPaymentStatus(request.paymentStatus());
        existingPayment.setTransactionReference(
                request.transactionReference()
        );

        return paymentRepository.save(existingPayment);
    }

    public void delete(Long id) {

        Payment payment = findById(id);

        paymentRepository.delete(payment);
    }
}