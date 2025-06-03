package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ParametresEntity;
import demo.Repositories.ParametresRepository;
import demo.core.error.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ParametresService {
    private final ParametresRepository repository;

    public ParametresService(ParametresRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAllByFilters(String name, String typeName, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByFilter(name, typeName, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAllByExaminationType(int page, int size, Long examinationTypeId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (examinationTypeId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByExaminationTypeId(examinationTypeId, pageRequest);
        }
        return repository.findAll(pageRequest);
    }

    @Transactional
    public ParametresEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ParametresEntity.class, id));
    }

    @Transactional
    public ParametresEntity create(ParametresEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ParametresEntity update(Long id, ParametresEntity entity) {
        final ParametresEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        existsEntity.setExaminationType(entity.getExaminationType());
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public ParametresEntity delete(Long id) {
        final ParametresEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
