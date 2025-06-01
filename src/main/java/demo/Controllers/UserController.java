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

import demo.DTO.UserDto;
import demo.Model.UserEntity;
import demo.Service.UserService;
import demo.core.configuration.Constants;
import jakarta.validation.Valid;

@RestController
@RequestMapping(UserController.URL)
public class UserController {
    public static final String URL = Constants.ADMIN_PREFIX + "/user";

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private UserDto toDto(UserEntity entity) {
        var model = modelMapper.map(entity, UserDto.class);
        return model;

    }

    private UserEntity toEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @GetMapping
    public List<UserDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<UserEntity> result = userService.getAll(page, Constants.DEFUALT_PAGE_SIZE);
        return result.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/edit/")
    public UserDto create(
            @RequestBody @Valid UserDto dto) {
        return toDto(userService.create(toEntity(dto)));
    }

    @PostMapping("/delete/{id}")
    public UserDto delete(
            @PathVariable(name = "id") Long id) {
        return toDto(userService.delete(id));
    }

}
