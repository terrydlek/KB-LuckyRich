package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.BoardDao;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
public class BoardAdminController {

    private final BoardService boardService;
    private final SecurityUserService securityUserService;


    public BoardAdminController(BoardService boardService, SecurityUserService securityUserService) {
        this.boardService = boardService;
        this.securityUserService = securityUserService;
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardDto>> boards() {
        return ResponseEntity.ok(boardService.getAdminBoardList());
    }

    @DeleteMapping("/boards")
    public ResponseEntity<String> delete(@RequestBody List<Integer> data) {
        System.out.println(data);
        try {
            for (Integer boardNum: data) {
                boardService.deleteBoardByIds(boardNum);
            }
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/reply/{replyNum}")
    public ResponseEntity<String> deleteReply(@PathVariable Integer replyNum) {
        try {
            boardService.deleteReplyById(replyNum);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

}
