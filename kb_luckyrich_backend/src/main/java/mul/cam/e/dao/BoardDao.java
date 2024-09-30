package mul.cam.e.dao;

import mul.cam.e.dto.BoardDto;
import mul.cam.e.dto.BoardReplyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardDao {
    List<BoardDto> getBoardList();

    void insertBoard(BoardDto board);

    BoardDto getBoard(int boardNum);

    void deleteBoard(int boardNum);

    void updateBoard(BoardDto board);

    void addComment(BoardReplyDto boardReplyDto);

    List<BoardReplyDto> getReply(int boardNum);

}
