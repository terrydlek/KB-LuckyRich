package mul.cam.e.dao;

import mul.cam.e.dto.FundDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FundDao {

    List<FundDto> conservativeList();

    List<FundDto> neutralList();

}
