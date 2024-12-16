package com.portone.web.controller;

import com.portone.domain.common.PaymentStatus;
import com.portone.domain.dto.PaymentDto;
import com.portone.domain.entity.Payment;
import com.portone.web.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/payment")
    public String paymentForm(Model model) {
        model.addAttribute("paymentDto", new PaymentDto());
        return "payment/paymentForm";
    }

    @PostMapping("/payment")
    public String payment(@Validated @ModelAttribute PaymentDto paymentDto) {
        Payment payment = Payment.builder()
                .uid(UUID.randomUUID().toString())
                .name(paymentDto.getName())
                .amount(Integer.parseInt(paymentDto.getAmount()))
                .status(PaymentStatus.READY)
                .isPaidOk(false)
                .build();

        paymentService.makePayment(payment);
        return "redirect:/payment/" + payment.getUid() + "/pay";
    }

    @GetMapping("/payment/{uid}/pay")
    public String paymentPay(@PathVariable String uid, Model model) {
        Payment payment;
        try {
            payment = paymentService.findByUid(uid);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "어떤 걸 주문할 것인지 선택하시지 않았습니다. 선택 후에 결제해주세요.");
            model.addAttribute("paymentDto", new PaymentDto());
            return "payment/paymentForm";
        }
        model.addAttribute("paymentDto", payment.toPaymentDto());
        return "payment/paymentPay";
    }
}
