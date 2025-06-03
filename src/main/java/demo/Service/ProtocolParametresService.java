package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ProtocolParametresEntity;
import demo.Repositories.ProtocolParametresRepository;
import demo.core.error.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProtocolParametresService {
    private final ProtocolParametresRepository repository;

    public ProtocolParametresService(ProtocolParametresRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProtocolParametresEntity> getAll(Long examinationId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (examinationId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByExaminationId(examinationId, pageRequest);
        }
        return repository.findAll(pageRequest);
    }

    @Transactional
    public ProtocolParametresEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProtocolParametresEntity.class, id));
    }

    @Transactional
    public ProtocolParametresEntity create(ProtocolParametresEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ProtocolParametresEntity update(Long id, ProtocolParametresEntity entity) {
        final ProtocolParametresEntity existsEntity = get(id);
        existsEntity.setBody(entity.getBody());
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public ProtocolParametresEntity delete(Long id) {
        final ProtocolParametresEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
