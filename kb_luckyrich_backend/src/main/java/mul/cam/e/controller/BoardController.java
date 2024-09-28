package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.BoardService;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getName")
    public ResponseEntity<String> getBoardName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return ResponseEntity.ok(userName);
    }

    @DeleteMapping("/deleteBoard")
    public ResponseEntity<String> deleteBoard(@RequestParam int boardNum) {
        System.out.println(boardNum);
        try {
            boardService.deleteBoard(boardNum);
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
}
