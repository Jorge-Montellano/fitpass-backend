package com.fitpass.controller;

import com.fitpass.dto.payment.PaymentRequest;
import com.fitpass.dto.payment.PaymentResponse;
import com.fitpass.entity.Payment;
import com.fitpass.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentResponse> findAll() {

        return paymentService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PaymentResponse findById(@PathVariable Long id) {

        return toResponse(
                paymentService.findById(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(
            @Valid @RequestBody PaymentRequest request) {

        Payment payment = new Payment();

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(request.paymentStatus());
        payment.setTransactionReference(
                request.transactionReference()
        );

        return toResponse(
                paymentService.save(payment)
        );
    }

    @PutMapping("/{id}")
    public PaymentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {

        Payment payment = new Payment();

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(request.paymentStatus());
        payment.setTransactionReference(
                request.transactionReference()
        );

        return toResponse(
                paymentService.update(id, payment)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        paymentService.delete(id);
    }

    private PaymentResponse toResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getUserMembership() != null
                        ? payment.getUserMembership().getId()
                        : null,
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionReference()
        );
    }
}