package com.portone.web.service;

import com.portone.domain.entity.AbstractPortonePayment;
import com.portone.web.client.PortoneClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PortoneService {
    private final PortoneClient portoneClient;
    private final PaymentService paymentService;
    private final OrderPaymentService orderPaymentService;

    @Value("${portone.pg.provider}")
    private String pgProvider;

    @Value("${portone.shop.id}")
    private String shopId;

    @Value("${portone.api.key}")
    private String apiKey;

    @Value("${portone.api.secret}")
    private String secretKey;

    // 토큰 발급
    public String getAccessToken() {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("imp_key", apiKey);
        credentials.put("imp_secret", secretKey);

        Map<String, Object> response = portoneClient.getAccessToken(credentials);
        Map<String, Object> tokenResponse = (Map<String, Object>) response.get("response");

        return tokenResponse.get("access_token").toString();
    }

    // 결제 데이터 조회
    public Map<String, Object> getPaymentDetails(String merchantUid) {
        String accessToken = getAccessToken();
        Map<String, Object> paymentDetails = portoneClient.getPaymentDetails(merchantUid, "Bearer " + accessToken);
        return paymentDetails;
    }

    // 결제 데이터 검증
    @Transactional
    public Map<String, Object> checkPayment(String uid) {
        AbstractPortonePayment payment = orderPaymentService.findByOrderPaymentId(uid);
        Map<String, Object> paymentData = this.getPaymentDetails(uid);
        log.info("paymentData is {}", paymentData);
        payment.check(paymentData);
        payment.update(paymentData);
        return paymentData;
    }
//    @Transactional
//    public Payment checkPayment(String uid) {
//        Payment payment = paymentService.findByUid(uid);
//        Map<String, Object> paymentData = this.getPaymentDetails(uid);
//        payment.check(paymentData);
//        return payment;
//    }

//    @Transactional
//    public void updateOrderPayment(Map<String, Object> paymentData) {
//        AbstractPortonePayment payment = (AbstractPortonePayment) paymentData.get("paymentObject");
//        payment.update(payment, paymentData);
//    }
}
