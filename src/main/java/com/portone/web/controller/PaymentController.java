package com.portone.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portone.domain.common.PaymentStatus;
import com.portone.domain.dto.PaymentDto;
import com.portone.domain.entity.Payment;
import com.portone.web.client.PortoneClient;
import com.portone.web.service.PaymentService;
import com.portone.web.service.PortoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final PortoneService portoneService;

    @Value("${portone.pg.provider}")
    private String portonePgProvider;

    @Value("${portone.shop.id}")
    private String portoneShopId;

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
                .paymentStatus(PaymentStatus.READY)
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
            paymentProps.put("pg", portonePgProvider);
            payProps = new ObjectMapper().writeValueAsString(paymentProps);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "payment/paymentForm";
        } catch (JsonProcessingException e) {
            model.addAttribute("errorMessage", "서버 내부적으로 문제가 발생했습니다. 잠시 후에 시도해주세요.");
            return "payment/paymentForm";
        }
        model.addAttribute("payProps", payProps);
        model.addAttribute("verifyUrl", "/payment/" + uid + "/check"); // 결제 검증 API URL
        model.addAttribute("portoneShopId", portoneShopId); // 가맹점 아이디
        return "payment/paymentPay";
    }

    @PatchMapping("/payment/{uid}/check")
    public String paymentCheck(@PathVariable String uid, Model model) {
        try {
            portoneService.checkPayment(uid);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/payment/" + uid + "/detail";
    }

    @GetMapping("/payment/{uid}/detail")
    public String paymentDetail(@PathVariable String uid, Model model) {
        Payment payment = paymentService.findByUid(uid);
        model.addAttribute("payment", payment);
        return "payment/paymentDetail";
    }
}
