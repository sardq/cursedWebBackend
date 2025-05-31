package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ExaminationTypeEntity;
import demo.Repositories.ExaminationTypeRepository;
import demo.core.error.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ExaminationTypeService {
    private final ExaminationTypeRepository repository;

    public ExaminationTypeService(ExaminationTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationTypeEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public ExaminationTypeEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExaminationTypeEntity.class, id));
    }

    @Transactional
    public ExaminationTypeEntity create(ExaminationTypeEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ExaminationTypeEntity update(Long id, ExaminationTypeEntity entity) {
        final ExaminationTypeEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public ExaminationTypeEntity delete(Long id) {
        final ExaminationTypeEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
