package demo.Controllers;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.DTO.ExaminationDto;
import demo.DTO.ExaminationStatistic;
import demo.Model.ExaminationEntity;
import demo.Service.ExaminationService;
import demo.Service.ExaminationTypeService;
import demo.Service.UserService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ExaminationController.URL)
public class ExaminationController {
    public static final String URL = Constants.API_URL + "/examination";

    private final ExaminationTypeService examinationTypeService;
    private final UserService userService;
    private final ExaminationService examinationService;
    private final ModelMapper modelMapper;

    public ExaminationController(ExaminationService examinationService, UserService userService,
            ExaminationTypeService examinationTypeService,
            ModelMapper modelMapper) {
        this.examinationService = examinationService;
        this.userService = userService;
        this.examinationTypeService = examinationTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/examinationStat")
    public ExaminationStatistic getExaminationStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ExaminationStatistic stats = examinationService.getStatistics(startDate, endDate);
        return stats;
    }

    public ExaminationStatistic toTopDto(ExaminationEntity examinationEntity) {
        return modelMapper.map(examinationEntity, ExaminationStatistic.class);

    }

    private ExaminationDto toDto(ExaminationEntity entity) {
        var model = modelMapper.map(entity, ExaminationDto.class);
        model.setExaminationTypeName(entity.getExaminationType().getName());
        model.setExaminationTypeId(entity.getExaminationType().getId());
        model.setUserFullname(entity.getUser().getFullname());
        model.setUserId(entity.getUser().getId());
        return model;
    }

    private ExaminationEntity toEntity(ExaminationDto dto) {
        final ExaminationEntity entity = modelMapper.map(dto, ExaminationEntity.class);
        entity.setExaminationType(examinationTypeService.get(dto.getExaminationTypeId()));
        entity.setUser(userService.get(dto.getUserId()));
        return entity;
    }

    @GetMapping
    public List<ExaminationDto> getAll(
            @RequestParam(name = "userId", defaultValue = "0") Long userId,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<ExaminationEntity> result = examinationService.getAll(userId, page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/examination/filter")
    public List<ExaminationDto> getAllByFilter(
            @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateStart,
            @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
            @RequestParam(name = "typeName", defaultValue = "") String typeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "sortOrder", defaultValue = "Сначала новые") String sortOrder,
            Model model) {
        Page<ExaminationEntity> result = examinationService.getAllByFilters(dateStart, dateEnd, typeName, sortOrder,
                page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/edit/")
    public ExaminationDto create(
            @RequestBody @Valid ExaminationDto dto) throws ParseException {
        return toDto(examinationService.create(toEntity(dto)));
    }

    @PostMapping("/edit/{id}")
    public ExaminationDto update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ExaminationDto dto) {
        return toDto(examinationService.update(id, toEntity(dto)));
    }

    @PostMapping("/delete/{id}")
    public ExaminationDto delete(
            @PathVariable(name = "id") Long id) {
        return toDto(examinationService.delete(id));
    }

}
