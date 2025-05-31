package demo.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.Model.MediaEntity;
import demo.Repositories.MediaRepository;
import demo.core.error.NotFoundException;

@Service
public class MediaService {
    private final MediaRepository repository;

    public MediaService(MediaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MediaEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public MediaEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(MediaEntity.class, id));
    }

    @Transactional
    public MediaEntity create(MediaEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        repository.save(entity);
        return repository.save(entity);
    }

    @Transactional
    public MediaEntity delete(Long id) {
        final MediaEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
