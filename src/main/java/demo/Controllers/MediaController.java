package demo.Controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.Model.MediaEntity;
import demo.Service.ExaminationService;
import demo.Service.MediaService;
import demo.core.configuration.Constants;

@RestController
@RequestMapping(MediaController.URL)
public class MediaController {
    public static final String URL = Constants.API_URL + "/media";

    private final MediaService mediaService;
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    public MediaController(ExaminationService examinationService, MediaService mediaService,
            ModelMapper modelMapper) {
        this.mediaService = mediaService;
    }

    @PostMapping(value = "/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<MediaEntity> upload(@RequestParam("files") List<MultipartFile> files,
            @RequestParam("examinationId") Long examinationId) throws Exception {
        logger.info("Запрос на загрузку медиа");
        List<MediaEntity> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(mediaService.save(file, examinationId));
        }
        return result;
    }

    @GetMapping
    public List<MediaEntity> getMedia(@RequestParam Long examinationId) {
        logger.info("Запрос на загрузку списка медиа по обследованию: {}", examinationId);
        return mediaService.listByExamination(examinationId);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        logger.info("Запрос на удаление медиа по ID: {}", id);
        mediaService.delete(id);
    }

    @GetMapping("/resource/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) throws Exception {
        logger.info("Запрос на загрузку медиа по Id: {}", id);
        MediaEntity media = mediaService.findByIdOrThrow(id);
        try (InputStream is = mediaService.getResource(id)) {
            byte[] content = is.readAllBytes();
            MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
            try {
                contentType = MediaType.parseMediaType(media.getMimeType());
            } catch (Exception ignored) {
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);
            headers.setContentLength(content.length);
            headers.setContentDisposition(ContentDisposition.inline().filename(media.getFilename()).build());
            headers.set("Accept-Ranges", "bytes");

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        }
    }

}
