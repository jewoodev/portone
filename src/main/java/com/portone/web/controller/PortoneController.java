package com.portone.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portone.web.controller.validator.PortoneIpValidator;
import com.portone.web.service.PortoneService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PortoneController {
    private final PortoneService portoneService;

    private static final List<String> ALLOWED_IPS = List.of("52.78.100.19", "52.78.48.223" ,"52.78.5.241");

    @ResponseBody
    @PostMapping("/webhook")
    public ResponseEntity<String> portoneWebhook(HttpServletRequest request) {
        String clientIp = PortoneIpValidator.getClientIp(request);
        if (!PortoneIpValidator.isAllowedIp(clientIp, ALLOWED_IPS)) {
            log.info("IP 거부");
            return ResponseEntity.status(BAD_REQUEST).body("허용되지 않은 IP에서의 요청입니다: " + clientIp);
        }

        String contentType = request.getHeader("Content-Type");
        String merchantUid = null;

        if ("application/json".equalsIgnoreCase(contentType)) {
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                Map<String, Object> data = new ObjectMapper().readValue(requestBody.toString(), Map.class);
                merchantUid = (String) data.get("merchant_uid");
            } catch (IOException e) {
                log.info("요청 바디 파싱 에러");
                return ResponseEntity.status(BAD_REQUEST).body("요청 Body를 읽을 수 없습니다.");
            }
        } else {
            merchantUid = request.getParameter("merchant_uid");
        }

        if (merchantUid == null || merchantUid.isEmpty()) {
            log.info("merchant uid 인자 누락");
            return ResponseEntity.status(BAD_REQUEST).body("merchant_uid 인자가 누락되었습니다.");
        }

        if ("merchant_1234567890".equals(merchantUid)) {
            return ResponseEntity.ok("test ok");
        }

        portoneService.checkPayment(merchantUid);

        return new ResponseEntity(OK);
    }
}
