package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ParametresEntity;
import demo.Repositories.ParametresRepository;
import demo.core.error.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ParametresService {
    private final ParametresRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ParametresService.class);

    public ParametresService(ParametresRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAll(int page, int size) {
        logger.info("Попытка получить список параметров:", page, size);
        var result = repository.findAll(PageRequest.of(page, size));
        logger.info("Получунный список:", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAllByFilters(String name, String typeName, int page, int size) {
        logger.info("Попытка получить список параметров с помощью фильтра:", name, typeName, page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        var result = repository.findByFilter(name, typeName, pageRequest);
        logger.info("Отфильтрованный список:", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ParametresEntity> getAllByExaminationType(int page, int size, Long examinationTypeId) {
        logger.info("Попытка получить список параметров по типу обследования:", examinationTypeId, page, size);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (examinationTypeId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByExaminationTypeId(examinationTypeId, pageRequest);
        }
        var result = repository.findAll(pageRequest);
        logger.info("Полученный список:", examinationTypeId, page, size);
        return result;
    }

    @Transactional
    public ParametresEntity get(Long id) {
        logger.info("Попытка получить параметр:", id);
        var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ParametresEntity.class, id));
        logger.info("Полученный параметр:", result);
        return result;
    }

    @Transactional
    public ParametresEntity create(ParametresEntity entity) {
        logger.info("Попытка создать параметр:", entity);

        if (entity == null) {
            logger.error("Нет сущности для создания:", entity);
            ;
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);

    }

    @Transactional
    public ParametresEntity update(Long id, ParametresEntity entity) {
        logger.info("Попытка обновить параметр:", id, entity);

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
