package demo.Controllers;

import demo.DTO.ProtocolDto;
import demo.Service.ProtocolControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/protocol")
public class ProtocolController {

    public ProtocolController(ProtocolControlService protocolControlService) {
        this.protocolControlService = protocolControlService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ProtocolController.class);
    private final ProtocolControlService protocolControlService;

    @PostMapping("/pdf")
    public ResponseEntity<Void> generateProtocol(@RequestBody ProtocolDto dto) {
        logger.info("Запрос на генерацию протокола: {}", dto);
        protocolControlService.saveProtocol(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> getProtocol(@PathVariable Long id) {
        logger.info("Запрос на сохранение протокола: {}", id);
        byte[] pdf = protocolControlService.getProtocol(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=protocol_" + id + ".pdf")
                .body(pdf);
    }
}
