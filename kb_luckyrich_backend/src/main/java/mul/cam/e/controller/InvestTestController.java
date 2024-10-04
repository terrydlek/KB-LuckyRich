package mul.cam.e.controller;

import mul.cam.e.dto.InvestmentTestResultDto;
import mul.cam.e.service.InvestmentTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investment")
public class InvestTestController {

    @Autowired
    private InvestmentTestService testService;

    @PostMapping("/saveResult")
    public ResponseEntity<String> saveResult(@RequestBody InvestmentTestResultDto result) {
        testService.saveTestResult(result);
        return ResponseEntity.ok("success save test result");
    }

    @GetMapping("/getResult")
    public InvestmentTestResultDto getResult(@RequestParam int userId) {
        return testService.getInvestmentTestResult(userId);
    }

    @DeleteMapping("/resetResult")
    public ResponseEntity<String> resetResult() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        testService.deleteTestResult(userName);
        return ResponseEntity.ok("success reset test result");
    }
}
