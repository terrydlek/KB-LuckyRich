package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.dto.BoardReplyDto;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    @GetMapping("/getList")
    public ResponseEntity<List<BoardDto>> getBoardList() {
        return ResponseEntity.ok(boardService.getBoardList());
    }

    @PostMapping("/created")
    public ResponseEntity<String> createBoard(@RequestBody BoardDto boardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boardDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardDto.setUserName(userName);

        System.out.println(boardDto);
        try {
            boardService.insertBoard(boardDto);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    @GetMapping("/{boardNum}")
    public ResponseEntity<Map<String, Object>> getBoard(@PathVariable int boardNum) {
        System.out.println(boardNum);
        BoardDto board = boardService.getBoard(boardNum);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Map<String, Object> map = new HashMap<>();
        map.put("board", board);
        map.put("userName", userName);
        System.out.println(map);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getName")
    public ResponseEntity<String> getBoardName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return ResponseEntity.ok(userName);
    }

    @PostMapping("/deleteBoard")
    public ResponseEntity<String> deleteBoard(@RequestBody BoardDto boardDto) {
        System.out.println("execute deleteBoard~~~");
        int boardId = boardDto.getBoardNum();
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    @PostMapping("/updateBoard")
    public ResponseEntity<String> updateBoard(@RequestBody BoardDto boardDto) {
        System.out.println(boardDto);
        try {
            boardService.updateBoard(boardDto);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    @GetMapping("/checkAdmin")
    public ResponseEntity<String> checkAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.getName());
        if (securityUserService.checkAuthority(authentication.getName()) > 0) {
            System.out.println("yes");
            return ResponseEntity.ok("yes");
        }
        return ResponseEntity.ok("no");
    }

    @PostMapping("/addComment")
    public ResponseEntity<String> addComment(@RequestBody BoardReplyDto boardReplyDto) {
        System.out.println("execute addComment~~~");
        boardReplyDto.setReplyAt(new Timestamp(System.currentTimeMillis()));
        System.out.println(boardReplyDto);
        try {
            boardService.addComment(boardReplyDto);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("fail");
    }

    @GetMapping("/reply/{boardNum}")
    public ResponseEntity<List<BoardReplyDto>> getReply(@PathVariable int boardNum) {
        System.out.println(boardNum);
        System.out.println("execute getReply~~~");
        return ResponseEntity.ok(boardService.getReply(boardNum));
    }


}
