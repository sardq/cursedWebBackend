package demo.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import demo.Model.ExaminationEntity;
import demo.Model.MediaEntity;
import demo.Repositories.MediaRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class MediaService {

    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    private MediaRepository repository;

    public MediaService(MinioClient minioClient, MediaRepository repository) {
        this.minioClient = minioClient;
        this.repository = repository;
    }

    public MediaEntity findByIdOrThrow(Long id) {
        logger.info("Попытка найти медиа", id);
        var result = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Медиа не найдено с id: " + id));
        logger.info("Полученное медиа", result);
        return result;
    }

    public MediaEntity save(MultipartFile file, Long examinationId) throws Exception {
        logger.info("Попытка сохранить медиа", file, examinationId);

        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        MediaEntity media = new MediaEntity();
        media.setFilename(file.getOriginalFilename());
        media.setMimeType(file.getContentType());
        media.setBucket(bucket);
        media.setObjectName(objectName);

        ExaminationEntity exam = new ExaminationEntity();
        exam.setId(examinationId);
        media.setExamination(exam);
        logger.info("Сохранение медиа:{}", media);

        return repository.save(media);
    }

    public void delete(Long mediaId) throws Exception {
        logger.info("Попытка удалить медиа", mediaId);
        MediaEntity media = repository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(media.getBucket())
                .object(media.getObjectName())
                .build());
        repository.delete(media);
    }

    public InputStream getResource(Long mediaId) throws Exception {
        logger.info("Попытка получить медиа", mediaId);
        MediaEntity media = repository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(media.getBucket())
                .object(media.getObjectName())
                .build());
    }

    public List<MediaEntity> listByExamination(Long examId) {
        logger.info("Попытка получить медиа по обследованию", examId);
        var result = repository.findByExaminationId(examId);
        logger.info("Список медиа", examId);
        return result;

    }
}
