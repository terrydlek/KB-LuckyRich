package mul.cam.e.rabbit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private RabbitMQProducer producer;
    private RabbitMQJsonProducer rabbitMQJsonProducer;

    public MessageController(RabbitMQProducer producer, RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.producer = producer;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        producer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ ...");
    }

//    @PostMapping("/publish")
//    public ResponseEntity<String> sendJsonMessage(@RequestBody UserDto userDto) {
//        rabbitMQJsonProducer.sendJsonMessage(userDto);
//        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
//    }
}
