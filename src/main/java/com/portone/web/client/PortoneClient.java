package com.portone.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.Map;

@FeignClient(name = "portoneClient", url = "https://api.iamport.kr")
public interface PortoneClient {
    // 토큰 발급 API
    @PostMapping(value = "/users/getToken", consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getAccessToken(@RequestBody Map<String, Object> credentials);

    // 결제 정보 조회 API
    @GetMapping(value = "/payments/find/{merchant_uid}")
    Map<String, Object> getPaymentDetails(@PathVariable("merchant_uid") String merchantUid,
                                          @RequestHeader("Authorization") String accessToken);
}
