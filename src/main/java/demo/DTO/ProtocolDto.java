package demo.DTO;

import demo.Service.ProtocolService.ParameterDto;
import demo.Service.ProtocolService.MediaDto;

import java.time.LocalDate;
import java.util.List;

public record ProtocolDto(
        Long id,
        String description,
        String conclusion,
        LocalDate time,
        String patientFullName,
        String examinationTypeName,
        List<ParameterDto> parameters,
        List<MediaDto> mediaFiles) {
}