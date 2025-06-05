package demo.Controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import demo.Service.LogService;

import java.io.InputStream;

@RestController
@RequestMapping("/api/log")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> uploadThenDownloadLog() {
        try {
            String filePath = "logs/application.log";
            String objectName = "application.log";

            logService.uploadLogFileToMinio(filePath, objectName);

            InputStream inputStream = logService.downloadLogFileFromMinio(objectName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
