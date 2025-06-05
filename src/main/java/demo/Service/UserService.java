package demo.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;
import demo.Exceptions.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import demo.DTO.CredentialsDto;
import demo.DTO.UserDto;
import demo.DTO.UserSignupDto;
import demo.Model.UserEntity;
import demo.Model.UserRole;
import demo.Repositories.UserRepository;
import demo.core.configuration.Constants;
import demo.core.configuration.UserMapper;
import demo.core.error.NotFoundException;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.repository = repository;
    }

    private void checkLogin(Long id, String login) {
        logger.info("Проверка сущестования пользователя: {}", id, login);
        final Optional<UserEntity> existsUser = repository.findByEmailIgnoreCase(login);
        if (existsUser.isPresent() && !existsUser.get().getId().equals(id)) {
            logger.warn("Пользователь с такой почтой уже существует");
            throw new IllegalArgumentException(
                    String.format("Пользователь с такой почтой уже существует", login));
        }
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAllByFilters(String email, int page, int size) {

        logger.info("Получение списка с помощью фильтрации: {}", email, page, size);
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = repository.findByFilter(email, pageRequest);
        logger.info("Ответ: {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAllUsers(String role, int page, int size) {
        logger.info("Получение списка по роли: {}", role, page, size);

        UserRole userRole = UserRole.ADMIN;
        if ("USER".equals(role)) {
            userRole = UserRole.USER;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = repository.findByRole(userRole, pageRequest);
        logger.info("Ответ: {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        logger.info("Получение списка пользователей");

        var result = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        logger.info("Ответ: {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAll(int page, int size) {
        logger.info("Получение списка пользователей: {}", page, size);
        var result = repository.findAll(PageRequest.of(page, size));
        logger.info("Ответ: {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public UserEntity get(Long id) {
        logger.info("Получение пользователя: {}", id);
        var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));

        logger.info("Ответ: {}", result);
        return result;

    }

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email) {
        logger.info("Получение пользователя с помощью почты :{}", email);
        var result = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        logger.info("Ответ: {}", result);
        return result;
    }

    @Transactional
    public UserEntity create(UserEntity entity) {
        logger.info("Попытка создать пользователя: {}", entity);
        if (entity == null) {
            logger.error("Отсутствует сущность", entity);
            throw new IllegalArgumentException("Entity is null");
        }
        checkLogin(null, entity.getFullname());
        final String password = Optional.ofNullable(entity.getPassword()).orElse("");
        entity.setPassword(
                passwordEncoder.encode(
                        StringUtils.hasText(password.strip()) ? password : Constants.DEFAULT_PASSWORD));
        entity.setRole(Optional.ofNullable(entity.getRole()).orElse(UserRole.USER));
        var result = repository.save(entity);
        logger.info("Пользователь сохранен: {}", entity);
        return result;
    }

    @Transactional
    public UserEntity update(long id, UserEntity entity) {
        logger.info("Попытка обновить пользователя: {}", id, entity);
        final UserEntity existsEntity = get(id);
        checkLogin(id, entity.getEmail());
        existsEntity.setFullname(entity.getFullname());
        existsEntity.setEmail(entity.getEmail());
        if ((entity.getPassword() != null) && (entity.getPassword().length() < 30))
            existsEntity.setPassword(
                    passwordEncoder.encode(
                            StringUtils.hasText(entity.getPassword().strip()) ? entity.getPassword()
                                    : Constants.DEFAULT_PASSWORD));
        repository.save(existsEntity);
        logger.info("Пользователь сохранен: {}", existsEntity);
        return existsEntity;

    }

    @Transactional
    public UserEntity delete(long id) {
        logger.info("Попытка удалить пользователя: {}", id);

        final UserEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    public UserDto login(CredentialsDto credentialsDto) {
        logger.info("Попытка входа: {}", credentialsDto);

        UserEntity user = repository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            logger.info("Пользователь вошел: {}", user);
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);

    }

    public UserDto register(UserSignupDto UserEntity) {
        logger.info("Попытка регистрации: {}", UserEntity);

        Optional<UserEntity> optionalUser = repository.findByEmail(UserEntity.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userMapper.signUpToUser(UserEntity);
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(UserEntity.getPassword())));

        UserEntity savedUser = repository.save(user);
        logger.info("Пользователь зарегистрирован: {}", savedUser);

        return userMapper.toUserDto(savedUser);
    }
}
