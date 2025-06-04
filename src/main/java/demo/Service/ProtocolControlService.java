package demo.Service;

import demo.DTO.ProtocolDto;
import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class ProtocolControlService {

    private final ProtocolGeneratorService generatorService;
    private final MinioClient minioClient;

    public ProtocolControlService(ProtocolGeneratorService generatorService, MinioClient minioClient) {
        this.generatorService = generatorService;
        this.minioClient = minioClient;
    }

    private final String BUCKET_NAME = "protocols";

    public void saveProtocol(ProtocolDto dto) {
        byte[] pdf = generatorService.generateProtocolPdf(dto);
        String filename = "protocol_" + dto.id() + ".pdf";

        try {
            createBucketIfNotExists();

            minioClient.putObject(PutObjectArgs.builder()
                    .stream(new ByteArrayInputStream(pdf), pdf.length, -1)
                    .bucket(BUCKET_NAME)
                    .object(filename)
                    .contentType("application/pdf")
                    .build());

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке файла в MinIO", e);
        }
    }

    public byte[] getProtocol(Long id) {
        String filename = "protocol_" + id + ".pdf";

        try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(filename)
                .build())) {

            return is.readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении файла из MinIO", e);
        }
    }

    @SneakyThrows
    private void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке/создании бакета", e);
        }
    }
}
