package mul.cam.e.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Component
public class KeyDecrypt {

    private final static String alg = "AES/CBC/PKCS5Padding";
    private final static String key = "LuckyRichCode-32Character-LuckyRichAPIKey";
    private final static String iv = key.substring(0, 16);

    public String decrypt(String clientKey) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 32), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decodeBytes = Base64.getDecoder().decode(clientKey);
            byte[] decrypted = cipher.doFinal(decodeBytes);
            return new String(decrypted).trim();
        } catch (Exception e) {
            throw new RuntimeException("복호화 처리중에 에러가 발생했습니다. e = {}", e);
        }
    }
}
