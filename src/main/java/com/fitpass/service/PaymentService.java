package com.fitpass.service;

import com.fitpass.dto.payment.PaymentRequest;
import com.fitpass.entity.Payment;
import com.fitpass.entity.UserMembership;
import com.fitpass.repository.PaymentRepository;
import com.fitpass.repository.UserMembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserMembershipRepository userMembershipRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            UserMembershipRepository userMembershipRepository) {

        this.paymentRepository = paymentRepository;
        this.userMembershipRepository = userMembershipRepository;
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

        return paymentRepository.save(payment);
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