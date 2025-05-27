package ru.van.tg_web_app_auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TgAuthService {

    @Value("${tg.bot.token}")
    private String botToken;

    public boolean validateInitData(String initData) {
        if (initData == null || initData.isEmpty()) {
            return false;
        }

        Map<String, String> data = parseInitData(initData);

        String authDateStr = data.get("auth_date");
        if (authDateStr == null) {
            return false;
        }
        long authDate = Long.parseLong(authDateStr);
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime - authDate > 86400) {
            return false;
        }

        String receivedHash = data.get("hash");
        if (receivedHash == null) {
            return false;
        }
        List<String> dataCheck = new ArrayList<>();
        data.entrySet().stream()
            .filter(entry -> !entry.getKey().equals("hash"))
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> dataCheck.add(entry.getKey() + "=" + entry.getValue()));
        String dataCheckString = String.join("\n", dataCheck);

        try {
            String secretKey = hmacSha256("WebAppData", botToken);
            String calculatedHash = hmacSha256(dataCheckString, secretKey);
            return calculatedHash.equals(receivedHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }

    public Map<String, String> parseInitData(String initData) {
        return Arrays.stream(initData.split("&"))
            .map(kv -> kv.split("=", 2))
            .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }

    private static String hmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
