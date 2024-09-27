package mul.cam.e.dao;

import mul.cam.e.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardDao {
    List<BoardDto> getBoardList();

    List<BoardDto> getBoardByUserId(@Param("userId") int userId);

    BoardDto getBoard(@Param("id") long id);

    int insertBoard(BoardDto boardDto);

    int updateBoard(BoardDto boardDto);

    int deleteBoard(@Param("id") long id);
}
