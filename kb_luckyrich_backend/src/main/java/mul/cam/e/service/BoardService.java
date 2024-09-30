package mul.cam.e.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.BoardDao;
import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.dto.BoardReplyDto;
import mul.cam.e.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardDao boardDao;

    public BoardService(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public List<BoardDto> getBoardList() {
        return boardDao.getBoardList();
    }

    public void insertBoard(BoardDto boardDto) {
        boardDao.insertBoard(boardDto);
    }

    public BoardDto getBoard(int boardNum) {
        return boardDao.getBoard(boardNum);
    }

    public void deleteBoard(int boardNum) {
        System.out.println("service " + boardNum);
        boardDao.deleteBoard(boardNum);
    }

    public void updateBoard(BoardDto boardDto) {
        boardDao.updateBoard(boardDto);
    }

    public void addComment(BoardReplyDto boardReplyDto) {
        boardDao.addComment(boardReplyDto);
    }

    public List<BoardReplyDto> getReply(int boardNum) {
        return boardDao.getReply(boardNum);
    }
}
