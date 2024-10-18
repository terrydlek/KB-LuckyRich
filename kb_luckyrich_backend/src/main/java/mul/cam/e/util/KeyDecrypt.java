package mul.cam.e.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class KeyDecrypt {

    private final static String alg = "AES/CBC/PKCS5Padding";
//    private final static String key = "LuckyRichCode-32Character-LuckyRichAPIKey";
//    private final static String iv = key.substring(0, 16);

    @Value("${aes.key}")
    private String key;

//    private String iv;

    // Bean 생성 후 초기화 단계에서 IV를 설정하는 메서드
//    @PostConstruct
//    public void init() {
//        this.iv = key.substring(0, 16);  // IV는 key의 첫 16바이트로 설정
//    }

    public String decrypt(String clientKey) {
        String iv = key.substring(0, 16);
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 32), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decodeBytes = Base64.getDecoder().decode(clientKey); // Base64 디코딩
            byte[] decrypted = cipher.doFinal(decodeBytes);
            return new String(decrypted, StandardCharsets.UTF_8).trim(); // UTF-8로 변환
        } catch (Exception e) {
            log.info("Error Decrypt");
            throw new RuntimeException("복호화 처리중에 에러가 발생했습니다.", e);
        }
    }
}