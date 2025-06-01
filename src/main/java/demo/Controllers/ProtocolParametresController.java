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

import demo.DTO.ProtocolParameterDTO;
import demo.Model.ProtocolParametresEntity;
import demo.Service.ParametresService;
import demo.Service.ProtocolParametresService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ProtocolParametresController.URL)
public class ProtocolParametresController {

    public static final String URL = Constants.ADMIN_PREFIX + "/protocolParametres";

    private final ParametresService parametresService;
    private final ProtocolParametresService protocolParametresService;
    private final ModelMapper modelMapper;

    public ProtocolParametresController(
            ProtocolParametresService protocolParametresService,
            ModelMapper modelMapper, ParametresService parametresService) {
        this.protocolParametresService = protocolParametresService;
        this.modelMapper = modelMapper;
        this.parametresService = parametresService;
    }

    private ProtocolParameterDTO toDto(ProtocolParametresEntity entity) {
        var model = modelMapper.map(entity, ProtocolParameterDTO.class);
        return model;
    }

    private ProtocolParametresEntity toEntity(ProtocolParameterDTO dto) {
        final ProtocolParametresEntity entity = modelMapper.map(dto, ProtocolParametresEntity.class);
        entity.setParametres(parametresService.get(dto.getParametersId()));
        return entity;
    }

    @GetMapping
    public List<ProtocolParameterDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<ProtocolParametresEntity> result = protocolParametresService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/edit/")
    public ProtocolParameterDTO create(
            @RequestBody @Valid ProtocolParameterDTO dto) {
        return toDto(protocolParametresService.create(toEntity(dto)));
    }

    @PostMapping("/edit/{id}")
    public ProtocolParameterDTO update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ProtocolParameterDTO dto) {
        return toDto(protocolParametresService.update(id, toEntity(dto)));
    }

    @PostMapping("/delete/{id}")
    public ProtocolParameterDTO delete(
            @PathVariable(name = "id") Long id) {
        return toDto(protocolParametresService.delete(id));
    }

}
