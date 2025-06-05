package demo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.DTO.TicketDto;
import demo.Model.TicketEntity;
import demo.Service.TicketService;
import demo.Service.UserService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(TicketController.URL)
public class TicketController {

    private final UserService userService;
    public static final String URL = Constants.ADMIN_PREFIX + "/ticket";
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    public TicketController(TicketService ticketService,
            ModelMapper modelMapper, UserService userService) {
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    private TicketDto toDto(TicketEntity entity) {
        var model = modelMapper.map(entity, TicketDto.class);
        model.setPersonUsername(entity.getUser().getFullname());
        return model;
    }

    private TicketEntity toEntity(TicketDto dto) {
        final TicketEntity entity = modelMapper.map(dto, TicketEntity.class);
        entity.setUser(userService.get(dto.getUserId()));
        return entity;
    }

    @GetMapping
    public List<TicketDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        logger.info("Запрос на получение списка тикетов: {}", page);

        Page<TicketEntity> result = ticketService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/edit/")
    public TicketDto create(
            @RequestBody @Valid TicketDto dto) {
        logger.info("Запрос на создание тикета: {}", dto);

        return toDto(ticketService.create(toEntity(dto)));
    }

    // @PostMapping("/edit/{id}")
    // public TicketDto update(
    // @PathVariable(name = "id") Long id,
    // @RequestBody @Valid TicketDto dto) {
    // return toDto(ticketService.update(id, toEntity(dto)));
    // }

    // @PostMapping("/delete/{id}")
    // public TicketDto delete(
    // @PathVariable(name = "id") Long id) {
    // return toDto(ticketService.delete(id));
    // }

}
