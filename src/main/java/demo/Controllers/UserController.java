package demo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.DTO.UserDto;
import demo.Model.UserEntity;
import demo.Service.UserService;
import demo.core.configuration.Constants;

@RestController
@RequestMapping(UserController.URL)
public class UserController {
    public static final String URL = Constants.API_URL + "/user";

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private UserDto toDto(UserEntity entity) {
        var model = modelMapper.map(entity, UserDto.class);
        return model;

    }

    @GetMapping
    public List<UserDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        logger.info("Запрос на получение списка пользователей: {}", page);

        Page<UserEntity> result = userService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public Page<UserDto> getAllByFilter(
            @RequestParam(name = "email", defaultValue = "") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {
        logger.info("Запрос на получение списка пользователей по: {} {} {}", email, page, pageSize);
        Page<UserEntity> result = userService.getAllByFilters(email, page, pageSize);
        return result.map(entity -> this.toDto(entity));
    }

    @GetMapping("/patients")
    public Page<UserDto> getAllUsers(
            @RequestParam(name = "role", defaultValue = "") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {
        logger.info("Запрос на получение списка пользователей по роли: {} {} {}", role, page, pageSize);
        Page<UserEntity> result = userService.getAllUsers(role, page, pageSize);
        return result.map(entity -> this.toDto(entity));
    }

    @PostMapping("/delete/{id}")
    public UserDto delete(
            @PathVariable(name = "id") Long id) {
        logger.info("Запрос на удаление пользователя: {} ", id);
        return toDto(userService.delete(id));
    }

}
