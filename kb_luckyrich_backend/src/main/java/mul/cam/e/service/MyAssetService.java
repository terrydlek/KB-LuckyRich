package mul.cam.e.service;

import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAssetService {

    @Autowired
    private MyAssetDao myAssetDao;

    public List<AccountDTO> getAccounts() {
        return myAssetDao.getAccounts(1);
    }
}
