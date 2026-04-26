package com.vicky.staykation.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    // Simulated payment — in production use Stripe SDK
    public Map<String, Object> processPayment(BigDecimal amount,
                                              String currency,
                                              String modeOfPayment) {
        // Simulate payment processing
        String paymentId = "PAY-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();

        return Map.of(
                "paymentId", paymentId,
                "amount", amount,
                "currency", currency,
                "status", "SUCCESS",
                "modeOfPayment", modeOfPayment,
                "message", "Payment processed successfully"
        );
    }

    public Map<String, Object> refundPayment(String paymentId,
                                             BigDecimal amount) {
        String refundId = "REF-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();

        return Map.of(
                "refundId", refundId,
                "originalPaymentId", paymentId,
                "amount", amount,
                "status", "REFUNDED",
                "message", "Refund processed successfully"
        );
    }
}