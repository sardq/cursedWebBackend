package demo.Service;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import demo.DTO.ProtocolDto;
import demo.core.configuration.ProtocolConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProtocolGeneratorService {

    private final Configuration freemarkerConfig;
    private final ProtocolConfig protocolConfig;
    private final MinioClient minioClient;

    public ProtocolGeneratorService(Configuration freemarkerConfig, ProtocolConfig protocolConfig,
            MinioClient minioClient) {
        this.freemarkerConfig = freemarkerConfig;
        this.protocolConfig = protocolConfig;
        this.minioClient = minioClient;
    }

    public byte[] generateProtocolPdf(ProtocolDto dto) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", dto.id());
        data.put("description", dto.description());
        data.put("conclusion", dto.conclusion());
        data.put("date", dto.time().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        data.put("patientFullName", dto.patientFullName());
        data.put("examinationTypeName", dto.examinationTypeName());
        data.put("parameters", dto.parameters());
        // List<Map<String, String>> mediaWithLocalFiles = dto.mediaFiles().stream()
        // .filter(m -> m.mimeType().startsWith("image"))
        // .map(m -> {
        // Map<String, String> mediaMap = new HashMap<>();
        // try {
        // String tempFilePath = downloadImageToTemp(m.bucket(), m.filename());
        // mediaMap.put("url", tempFilePath); // file:/tmp/...
        // mediaMap.put("mimeType", m.mimeType());
        // } catch (Exception e) {
        // // можно логировать или игнорировать ошибку
        // System.err.println("Ошибка при скачивании файла: " + m.filename());
        // }
        // return mediaMap;
        // })
        // .toList();
        // data.put("mediaFiles", mediaWithLocalFiles);
        data.putAll(protocolConfig.getProtocolProperties());

        String html = generateHtml(data, "protocol-template.ftl");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        toPdf(html, byteStream);

        return byteStream.toByteArray();
    }

    public String generateHtml(Map<String, Object> data, String templateName) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Ошибка при генерации HTML из шаблона", e);
        }
    }

    public void toPdf(String html, OutputStream os) {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();

        String baseUrl = new File("src/main/resources/").toURI().toString();
        builder.withHtmlContent(html, baseUrl);

        builder.useFont(
                () -> getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"),
                "Roboto", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
        builder.useFont(
                () -> getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"),
                "Roboto-Bold", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
        builder.useFont(() -> getClass().getResourceAsStream("/fonts/DejaVuSans.ttf"),
                "DejaVuSans", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
        builder.toStream(os);
        try {
            builder.run();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации PDF", e);
        }
    }

    public String generatePresignedUrl(String bucket, String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .method(Method.GET)
                            .expiry(60 * 60)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении ссылки из MinIO", e);
        }
    }
}
// на случай медиа в отчете
// {<#if mediaFiles??&&(mediaFiles?size>0)><div
// class="section"><h2>Прикрепленные изображения</h2><#
// list mediaFiles
// as media><#if media.mimeType??&&media.mimeType?starts_with("image")>
// <div style="margin-bottom: 10px;">
// <img src="${media.url?replace('&', '&amp;')}" style="max-width: 100%;
// max-height: 300px;" />
// </div>
// </#if>
// </#list>
// </div>
// </#if> }