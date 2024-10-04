package mul.cam.e.dao;

import mul.cam.e.dto.InvestmentTestResultDto;

public interface InvestmentTestResultDao {
    void insertResult(InvestmentTestResultDto result);

    InvestmentTestResultDto findByUserId(int userId);

    void deleteByUserId(String userName);
}
