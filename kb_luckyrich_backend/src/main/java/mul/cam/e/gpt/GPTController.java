package mul.cam.e.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
//@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {
    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;


    //    @GetMapping("/chat")
//    public String chat(@RequestParam("prompt") String prompt){
    public String chat(String prompt) {
        log.info("GPT TEST");

        /**
         * temperature: 다양성 (높을수록 무작위성 상승)
         * max_tokens: 답변 최대길이
         * topP : 텍스트 다양성 증가
         * *_Penalty: 같은 단어나 구 반복 억제
         */
        GPTRequest request = new GPTRequest(
                model, prompt, 1, 256, 1, 0, 0);

        GPTResponse gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GPTResponse.class
        );

        return gptResponse.getChoices().get(0).getMessage().getContent();

    }
}
