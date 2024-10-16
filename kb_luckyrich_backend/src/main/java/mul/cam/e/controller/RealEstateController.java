package mul.cam.e.controller;


import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.RealEstateDto;
import mul.cam.e.service.RealEstateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j
@RequestMapping("/realestate")
public class RealEstateController {

    private  final RealEstateService realEstateService;

    public RealEstateController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @GetMapping("/")
    public ResponseEntity<List<RealEstateDto>> getEstateByRegion(@RequestParam String region) {
        return ResponseEntity.ok(realEstateService.getEstateByRegion(region));
    }
}
