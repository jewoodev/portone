package com.portone.web.controller.validator;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class PortoneIpValidator {
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    public static boolean isAllowedIp(String clientIp, List<String> allowedIps) {
        return allowedIps.contains(clientIp);
    }
}
