package com.portone.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;
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
    public String paymentPay(@PathVariable String uid, Model model) throws JsonProcessingException {
        Payment payment;
        Map<String, String> paymentProps = new HashMap<>();
        String payProps = null;
        try {
            payment = paymentService.findByUid(uid);
            paymentProps.put("merchant_uid", payment.getUid());
            paymentProps.put("name", payment.getName());
            paymentProps.put("amount", String.valueOf(payment.getAmount()));
            payProps = new ObjectMapper().writeValueAsString(paymentProps);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "어떤 걸 주문할 것인지 선택하시지 않았습니다. 선택 후에 결제해주세요.");
            return "payment/paymentForm";
        } catch (JsonProcessingException e) {
            model.addAttribute("errorMessage", "서버 내부적으로 문제가 발생했습니다. 잠시 후에 시도해주세요.");
            return "payment/paymentForm";
        }
        model.addAttribute("payProps", payProps);
        model.addAttribute("verifyPage", "/payment/" + uid + "/check");
        return "payment/paymentPay";
    }

    @GetMapping("/payment/{uid}/check")
    public String paymentCheck(@PathVariable String uid, Model model) {
        return "";
    }
}
