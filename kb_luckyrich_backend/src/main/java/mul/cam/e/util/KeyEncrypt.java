package mul.cam.e.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class KeyEncrypt {

    private final static String aesAlg = "AES/CBC/PKCS5Padding";
    private final static String key = "LuckyRichCode-32Character-LuckyRichAPIKey";
    private final static String iv = key.substring(0, 16);

    public String encrypt(String text) {
        try {
            return getAesKey(text); // 입력 텍스트를 직접 AES로 암호화
        } catch (Exception e) {
            throw new RuntimeException("암호화 처리중에 에러가 발생했습니다.", e);
        }
    }

    // AES 키 만들기
    private String getAesKey(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(aesAlg);
        SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 32), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] encodeBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)); // UTF-8로 변환
        return Base64.getEncoder().encodeToString(encodeBytes); // Base64 인코딩
    }
}