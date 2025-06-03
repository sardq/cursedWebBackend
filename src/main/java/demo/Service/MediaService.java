package demo.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import demo.Model.ExaminationEntity;
import demo.Model.MediaEntity;
import demo.Repositories.MediaRepository;
import demo.core.error.NotFoundException;
import jakarta.annotation.Resource;

@Service
public class MediaService {
    private final MediaRepository repository;

    public MediaService(MediaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MediaEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<MediaEntity> getAllByExaminationId(Long examinationId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (examinationId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByExaminationId(examinationId, pageRequest);
        }
        return repository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public MediaEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(MediaEntity.class, id));
    }

    public MediaEntity save(MultipartFile file, ExaminationEntity exam) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = root.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        MediaEntity media = new MediaEntity();
        media.setFilename(filename);
        media.setOriginalFilename(file.getOriginalFilename());
        media.setContentType(file.getContentType());
        media.setFilePath(target.toString());
        media.setExamination(exam);

        return repository.save(media);
    }

    public Resource loadAsResource(Long mediaId) throws IOException {
        MediaEntity media = repository.findById(mediaId)
                .orElseThrow(() -> new FileNotFoundException("Media not found"));
        Path file = Paths.get(media.getFilePath());
        return new UrlResource(file.toUri());
    }

    @Transactional
    public MediaEntity delete(Long id) {
        final MediaEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
