package mul.cam.e.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.BoardDao;
import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private UserDao userDao;

    public List<BoardDto> getBoardList() {
        return boardDao.getBoardList();
    }

    public List<BoardDto> getBoardByUserId(int userId) {
        return boardDao.getBoardByUserId(userId);
    }

    public BoardDto getBoard(int id) {
        return boardDao.getBoard(id);
    }

    public BoardDto create(BoardDto boardDto) {
        boardDao.insertBoard(boardDto);
        return boardDto;
    }

    public BoardDto update(BoardDto boardDto) {
        int result = boardDao.updateBoard(boardDto);
        if (result > 0) {
            return boardDao.getBoard(boardDto.getId());
        } else {
            throw new RuntimeException("Fail to update board");
        }
    }

    public void delete(long id) {
        int result = boardDao.deleteBoard(id);
        if (result == 0) {
            throw new RuntimeException("Fail to delete board");
        }
    }
}
