package mul.cam.e.util;

import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import mul.cam.e.dto.GoogleUserInfDto;
import org.springframework.stereotype.Component;

@Component
public class TokenDecoder {

    public static GoogleUserInfDto decodeIdToken(String idToken) throws Exception {
        String[] parts = idToken.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid ID token format.");
        }

        String payload = parts[1];
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(decodedPayload, GoogleUserInfDto.class);
    }
}

