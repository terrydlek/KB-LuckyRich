package mul.cam.e.dao;

import mul.cam.e.dto.AccountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyAssetDao {
    List<AccountDTO> getAccounts(int userId);
}
