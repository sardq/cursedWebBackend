package demo.Service;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import demo.DTO.ProtocolDto;
import demo.core.configuration.ProtocolConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProtocolGeneratorService {

    private final Configuration freemarkerConfig;
    private final ProtocolConfig protocolConfig;

    public ProtocolGeneratorService(Configuration freemarkerConfig, ProtocolConfig protocolConfig) {
        this.freemarkerConfig = freemarkerConfig;
        this.protocolConfig = protocolConfig;
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
        data.put("mediaFiles", dto.mediaFiles());

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
}
