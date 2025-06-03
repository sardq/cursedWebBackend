package demo.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

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

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.repository = repository;
    }

    private void checkLogin(Long id, String login) {
        final Optional<UserEntity> existsUser = repository.findByEmailIgnoreCase(login);
        if (existsUser.isPresent() && !existsUser.get().getId().equals(id)
                && existsUser.get().getFullname() != "[deleted]") {
            throw new IllegalArgumentException(
                    String.format("User with login %s is already exists", login));
        }
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAllByFilters(String email, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByFilter(email, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public UserEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));
    }

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
    }

    @Transactional
    public UserEntity create(UserEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        checkLogin(null, entity.getFullname());
        final String password = Optional.ofNullable(entity.getPassword()).orElse("");
        entity.setPassword(
                passwordEncoder.encode(
                        StringUtils.hasText(password.strip()) ? password : Constants.DEFAULT_PASSWORD));
        entity.setRole(Optional.ofNullable(entity.getRole()).orElse(UserRole.USER));
        return repository.save(entity);
    }

    @Transactional
    public UserEntity update(long id, UserEntity entity) {
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
        return existsEntity;
    }

    @Transactional
    public UserEntity delete(long id) {
        final UserEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    public UserDto login(CredentialsDto credentialsDto) {
        UserEntity user = repository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(UserSignupDto UserEntity) {
        Optional<UserEntity> optionalUser = repository.findByEmail(UserEntity.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = userMapper.signUpToUser(UserEntity);
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(UserEntity.getPassword())));

        UserEntity savedUser = repository.save(user);

        return userMapper.toUserDto(savedUser);
    }
}
