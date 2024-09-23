package mul.cam.e.service;

import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyAssetService {

    private final MyAssetDao myAssetDao;


    public MyAssetService(MyAssetDao myAssetDao) {
        this.myAssetDao = myAssetDao;
    }

    public List<MyStockDto> getMyStock(int userId) {
        return myAssetDao.getMyStock(userId);
    }

    public List<MyEstateDto> getMyEstate(int userId) {
        return myAssetDao.getMyEstate(userId);
    }

    public List<MyCarDto> getMyCar(int userId) {
        return myAssetDao.getMyCar(userId);
    }

    public List<AccountDto> getMyAccount(int userId) {
        return myAssetDao.getMyAccount(userId);
    }

    public boolean setMyAccount(String account_number, String email, int bank_id, int account_type_id, int balance) {
        System.out.println(account_number);
        return myAssetDao.setMyAccount(account_number, email, bank_id, account_type_id, balance);
    }

    public boolean setTransaction(TransactionDto transactionDto) { return myAssetDao.setTransaction(transactionDto); }
}
