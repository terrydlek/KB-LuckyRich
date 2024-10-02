package mul.cam.e.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class KeyEncrypt {

    private final static String shaAlg = "SHA-256";
    private final static String aesAlg = "AES/CBC/PKCS5Padding";
    private final static String key = "LuckyRichCode-32Character-LuckyRichAPIKey";
    private final static String iv = key.substring(0, 16);

    public String encrypt(String text) {
        try {
            String shaKey = getShaKey(text);
            log.info(shaKey);
            return getAesKey(shaKey);
        } catch (Exception e) {
            throw new RuntimeException("암호화 처리중에 에러가 발생했습니다. e = {}", e);
        }
    }

    // sha-256 키 생성
    private String getShaKey(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(shaAlg);
        md.update(text.getBytes());
        return bytesToHex(md.digest());
    }

    // AES 키 만들기
    private String getAesKey(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(aesAlg);
        SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 32), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(iv.getBytes(), 16));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] encodeBytes = cipher.doFinal(text.getBytes());
        byte[] encrypted = Base64.getEncoder().encode(encodeBytes);
        return new String(encrypted).trim();
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
