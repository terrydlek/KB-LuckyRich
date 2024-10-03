package mul.cam.e.service;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dao.InvestmentTestResultDao;
import mul.cam.e.dto.InvestmentTestResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class InvestmentTestService {
    @Autowired
    private InvestmentTestResultDao resultDao;

    public void saveTestResult(InvestmentTestResultDto result) {
            resultDao.insertResult(result);
    }

    public InvestmentTestResultDto getInvestmentTestResult(int userId) {
        return resultDao.findByUserId(userId);
    }

    public void deleteTestResult(int userId) {
        try {
            resultDao.deleteByUserId(userId);
        } catch (Exception e) {
            // 로그 기록
            log.error("Error resetting test result for user " + userId, e);
            // 예외를 다시 던져서 컨트롤러에서 처리할 수 있게 함
            throw new RuntimeException("Failed to reset test result", e);
        }
    }
}
