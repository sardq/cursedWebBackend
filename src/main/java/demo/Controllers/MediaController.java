package demo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.DTO.MediaDto;
import demo.Model.MediaEntity;
import demo.Service.ExaminationService;
import demo.Service.MediaService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(MediaController.URL)
public class MediaController {
    public static final String URL = Constants.API_URL + "/media";

    private final MediaService mediaService;
    private final ExaminationService examinationService;
    private final ModelMapper modelMapper;

    public MediaController(ExaminationService examinationService, MediaService mediaService,
            ModelMapper modelMapper) {
        this.examinationService = examinationService;
        this.mediaService = mediaService;
        this.modelMapper = modelMapper;
    }

    private MediaDto toDto(MediaEntity entity) {
        var model = modelMapper.map(entity, MediaDto.class);
        model.setExaminationName(entity.getExamination().getDescription());
        return model;
    }

    private MediaEntity toEntity(MediaDto dto) {
        final MediaEntity entity = modelMapper.map(dto, MediaEntity.class);
        entity.setExamination(examinationService.get(dto.getExaminationId()));
        return entity;
    }

    @GetMapping
    public List<MediaDto> getAll(
            @RequestParam(defaultValue = "0") int page) {
        Page<MediaEntity> result = mediaService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/create/")
    public MediaDto create(
            @RequestBody @Valid MediaDto dto) {
        return toDto(mediaService.create(toEntity(dto)));
    }

    // @PostMapping("/edit/{id}")
    // public MediaDto update(
    // @PathVariable(name = "id") Long id,
    // @RequestBody @Valid MediaDto dto) {
    // return toDto(mediaService.update(id, toEntity(dto)));
    // }

    @PostMapping("/delete/{id}")
    public MediaDto delete(
            @PathVariable(name = "id") Long id) {
        return toDto(mediaService.delete(id));
    }

}
