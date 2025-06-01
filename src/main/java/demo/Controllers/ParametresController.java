package demo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.DTO.ParametresDto;
import demo.Model.ParametresEntity;
import demo.Service.ExaminationTypeService;
import demo.Service.ParametresService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ParametresController.URL)
public class ParametresController {

    public static final String URL = Constants.ADMIN_PREFIX + "/parametres";

    private final ParametresService parametresService;
    private final ModelMapper modelMapper;
    private final ExaminationTypeService examinationTypeService;

    public ParametresController(ParametresService parametresService,
            ModelMapper modelMapper, ExaminationTypeService examinationTypeService) {
        this.parametresService = parametresService;
        this.modelMapper = modelMapper;
        this.examinationTypeService = examinationTypeService;
    }

    private ParametresDto toDto(ParametresEntity entity) {
        var model = modelMapper.map(entity, ParametresDto.class);
        return model;
    }

    private ParametresEntity toEntity(ParametresDto dto) {
        final ParametresEntity entity = modelMapper.map(dto, ParametresEntity.class);
        entity.setExaminationType(examinationTypeService.get(dto.getExaminationTypeId()));
        return entity;
    }

    @GetMapping
    public List<ParametresDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<ParametresEntity> result = parametresService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/edit/")
    public ParametresDto create(
            @RequestBody @Valid ParametresDto dto) {
        return toDto(parametresService.create(toEntity(dto)));
    }

    @PostMapping("/edit/{id}")
    public ParametresDto update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ParametresDto dto) {
        return toDto(parametresService.update(id, toEntity(dto)));
    }

    @PostMapping("/delete/{id}")
    public ParametresDto delete(
            @PathVariable(name = "id") Long id) {
        return toDto(parametresService.delete(id));
    }

}
