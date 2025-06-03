package demo.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.model.Model;
import demo.DTO.ExaminationTypeDto;
import demo.Model.ExaminationTypeEntity;
import demo.Service.ExaminationTypeService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ExaminationTypeController.URL)
public class ExaminationTypeController {
    public static final String URL = Constants.API_URL + "/examinationType";

    private final ExaminationTypeService examinationTypeService;
    private final ModelMapper modelMapper;

    public ExaminationTypeController(ExaminationTypeService examinationTypeService,
            ModelMapper modelMapper) {
        this.examinationTypeService = examinationTypeService;
        this.modelMapper = modelMapper;
    }

    private ExaminationTypeDto toDto(ExaminationTypeEntity entity) {
        var model = modelMapper.map(entity, ExaminationTypeDto.class);
        return model;
    }

    private ExaminationTypeEntity toEntity(ExaminationTypeDto dto) {
        final ExaminationTypeEntity entity = modelMapper.map(dto, ExaminationTypeEntity.class);
        return entity;
    }

    @GetMapping
    public Page<ExaminationTypeDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ExaminationTypeEntity> result = examinationTypeService.getAll(page, size);
        return result.map(this::toDto);
    }

    @GetMapping("/filter")
    public Page<ExaminationTypeDto> getAllByFilter(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {
        Page<ExaminationTypeEntity> result = examinationTypeService.getAllByFilters(name, page, pageSize);
        return result.map(this::toDto);
    }

    @PostMapping("/create/")
    public ExaminationTypeDto create(
            @RequestBody @Valid ExaminationTypeDto dto) {
        return toDto(examinationTypeService.create(toEntity(dto)));
    }

    @PostMapping("/edit/{id}")
    public ExaminationTypeDto update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ExaminationTypeDto dto) {
        return toDto(examinationTypeService.update(id, toEntity(dto)));
    }

    @PostMapping("/delete/{id}")
    public ExaminationTypeDto delete(
            @PathVariable(name = "id") Long id) {
        return toDto(examinationTypeService.delete(id));
    }

}
