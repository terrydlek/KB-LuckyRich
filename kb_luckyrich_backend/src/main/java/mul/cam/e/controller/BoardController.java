package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.dto.BoardReplyDto;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final SecurityUserService securityUserService;

    public BoardController(BoardService boardService, SecurityUserService securityUserService) {
        this.boardService = boardService;
        this.securityUserService = securityUserService;
    }

    // 모든 게시글의 목록을 가져오는 메서드
    @GetMapping
    public ResponseEntity<List<BoardDto>> getBoardList() {
        return ResponseEntity.ok(boardService.getBoardList());
    }

    // 새 게시글을 생성하는 메서드
    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody BoardDto boardDto) {
        // 인증된 사용자의 이름을 가져옴
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        boardDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardDto.setUserName(userName);

        try {
            boardService.insertBoard(boardDto);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    // 특정 게시글을 번호로 가져오는 메서드
    @GetMapping("/{boardNum}")
    public ResponseEntity<Map<String, Object>> getBoard(@PathVariable int boardNum) {
        BoardDto board = boardService.getBoard(boardNum);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> map = new HashMap<>();
        map.put("board", board);
        map.put("userName", userName);
        return ResponseEntity.ok(map);
    }

    // 게시글의 del 컬럼을 1로 수정하는 메서드
    @PutMapping("/changeDel")
    public ResponseEntity<String> deleteBoard(@RequestBody BoardDto boardDto) {
        log.info("execute deleteBoard~~~");
        int boardId = boardDto.getBoardNum();
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    // 기존 게시글을 업데이트하는 메서드
    @PutMapping
    public ResponseEntity<String> updateBoard(@RequestBody BoardDto boardDto) {
        log.info("execute updateBoard~~~");
        try {
            boardService.updateBoard(boardDto);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    // 사용자가 관리자 권한이 있는지 확인하는 메서드
    @GetMapping("/checkAdmin")
    public ResponseEntity<String> checkAdmin() {
        System.out.println("execute checkAdmin~~~");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return ResponseEntity.ok("ok");
    }

    // 게시글에 댓글을 추가하는 메서드
    @PostMapping("/reply")
    public ResponseEntity<String> addComment(@RequestBody BoardReplyDto boardReplyDto) {
        log.info("execute addComment~~~");
        boardReplyDto.setReplyAt(new Timestamp(System.currentTimeMillis()));
        try {
            boardService.addComment(boardReplyDto);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    // 특정 게시글에 대한 댓글을 가져오는 메서드
    @GetMapping("/reply/{boardNum}")
    public ResponseEntity<List<BoardReplyDto>> getReply(@PathVariable int boardNum) {
        log.info("execute getReply~~~");
        return ResponseEntity.ok(boardService.getReply(boardNum));
    }
}