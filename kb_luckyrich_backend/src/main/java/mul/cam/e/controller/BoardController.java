package mul.cam.e.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.BoardDto;
import mul.cam.e.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private final BoardService boardService;

    @GetMapping("/post")
    public ResponseEntity<List<BoardDto>> getBoardList() {
        log.info("getBoardList");
        List<BoardDto> boardList = boardService.getBoardList();
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/post/{userid}")
    public ResponseEntity<List<BoardDto>> getBoardByUserId(@PathVariable int userid) {
        List<BoardDto> userBoards = boardService.getBoardByUserId(userid);
        return ResponseEntity.ok(userBoards);
    }

    @GetMapping("/created")
    public ResponseEntity<BoardDto> createBoard(@RequestParam String title, @RequestParam String content) {
        BoardDto board = new BoardDto(1, title, content, "2024-09-26");
        return ResponseEntity.ok(boardService.create(board));
    }

    @PostMapping("/created")
    public ResponseEntity<String> createBoard(@RequestBody String answer) {
        return ResponseEntity.ok(answer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable int id, @RequestBody BoardDto boardDto) {
        boardDto.setId(id);
        BoardDto updateBoard = boardService.update(boardDto);
        return ResponseEntity.ok(updateBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable int id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
