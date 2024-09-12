package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.FundDto;
import mul.cam.e.service.RecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j
@RequestMapping("/asset")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }


    @GetMapping("/conservativeList")
    public ResponseEntity<List<FundDto>> conservativeList() {
        return ResponseEntity.ok(recommendService.conservativeList());
    }

    @GetMapping("/neutrilityList")
    public ResponseEntity<List<FundDto>> neutrilityList() {
        return ResponseEntity.ok(recommendService.neutrilityList());
    }

}
