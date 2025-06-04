package demo.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProtocolService {

    public void generateExaminationProtocol(HttpServletResponse response,
            Long examinationId,
            String description,
            String conclusion,
            LocalDate time,
            String patientFullName,
            String examinationTypeName,
            List<ParameterDto> parameters,
            List<MediaDto> mediaFiles) throws IOException {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            BaseFont boldFont = BaseFont.createFont(
                    "fonts/NSB.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            BaseFont regFont = BaseFont.createFont(
                    "fonts/NSR.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);

            Font titleFont = new Font(boldFont, 18);
            Font headerFont = new Font(boldFont, 14);
            Font bodyFont = new Font(regFont, 12);
            Font boldBodyFont = new Font(boldFont, 12);

            Paragraph title = new Paragraph("ПРОТОКОЛ ОБСЛЕДОВАНИЯ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);
            infoTable.setSpacingAfter(10);

            addTableHeader(infoTable, "Пациент:", patientFullName, headerFont, bodyFont);
            addTableHeader(infoTable, "Тип обследования:", examinationTypeName, headerFont, bodyFont);
            addTableHeader(infoTable, "Дата обследования:",
                    time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    headerFont, bodyFont);

            document.add(infoTable);

            addSection(document, "Описание:", description, headerFont, bodyFont);
            addSection(document, "Заключение:", conclusion, headerFont, bodyFont);

            Paragraph paramsHeader = new Paragraph("Параметры обследования:", headerFont);
            paramsHeader.setSpacingBefore(15);
            paramsHeader.setSpacingAfter(10);
            document.add(paramsHeader);

            if (parameters != null && !parameters.isEmpty()) {
                PdfPTable paramsTable = new PdfPTable(2);
                paramsTable.setWidthPercentage(80);
                paramsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                for (ParameterDto param : parameters) {
                    addTableRow(paramsTable, param.name(), param.value(), boldBodyFont, bodyFont);
                }
                document.add(paramsTable);
            } else {
                document.add(new Paragraph("Нет данных о параметрах", bodyFont));
            }

            // Медиафайлы
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                Paragraph mediaHeader = new Paragraph("Прикрепленные файлы:", headerFont);
                mediaHeader.setSpacingBefore(15);
                mediaHeader.setSpacingAfter(10);
                document.add(mediaHeader);

                for (MediaDto media : mediaFiles) {
                    Paragraph mediaItem = new Paragraph();
                    mediaItem.add(new Chunk(media.filename() + " (", bodyFont));
                    mediaItem.add(new Chunk(media.mimeType(), boldBodyFont));
                    mediaItem.add(new Chunk(")", bodyFont));
                    document.add(mediaItem);
                }
            }

            Paragraph signature = new Paragraph();
            signature.setSpacingBefore(30);
            signature.add(new Chunk("Врач: ___________________", bodyFont));
            signature.add(Chunk.NEWLINE);
            signature.add(new Chunk("Дата: " +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    bodyFont));
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

        } catch (DocumentException e) {
            throw new IOException("Ошибка генерации PDF", e);
        }
    }

    private void addTableHeader(PdfPTable table, String header, String value, Font headerFont, Font valueFont) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setPaddingBottom(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(5);

        table.addCell(headerCell);
        table.addCell(valueCell);
    }

    private void addTableRow(PdfPTable table, String name, String value, Font nameFont, Font valueFont) {
        table.addCell(new Phrase(name, nameFont));
        table.addCell(new Phrase(value, valueFont));
    }

    private void addSection(Document document, String header, String text, Font headerFont, Font textFont)
            throws DocumentException {
        if (text != null && !text.isEmpty()) {
            Paragraph section = new Paragraph();
            section.add(new Chunk(header, headerFont));
            section.add(Chunk.NEWLINE);
            section.add(new Chunk(text, textFont));
            section.setSpacingBefore(10);
            section.setSpacingAfter(10);
            document.add(section);
        }
    }

    public record ParameterDto(String name, String value) {
    }

    public record MediaDto(Long id, String filename, String mimeType, String bucket) {
    }
}