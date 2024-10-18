package mul.cam.e.dao;

import mul.cam.e.dto.DepositDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepositDao {

    List<DepositDto> getDeposit();

    DepositDto getDepositByProdname(String prodname);

}
