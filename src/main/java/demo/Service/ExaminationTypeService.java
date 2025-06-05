package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ExaminationTypeEntity;
import demo.Repositories.ExaminationTypeRepository;
import demo.core.error.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ExaminationTypeService {
    private final ExaminationTypeRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ExaminationTypeService.class);

    public ExaminationTypeService(ExaminationTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationTypeEntity> getAllByFilters(String name, int page, int size) {
        logger.info("Получение типов обследования по {}", name, size, page);
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = repository.findByFilter(name, pageRequest);
        logger.info("Список типов обследования {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationTypeEntity> getAll(int page, int size) {
        logger.info("Получение типов обследования по {}", size, page);
        var result = repository.findAll(PageRequest.of(page, size));
        logger.info("Список типов обследования {}", result);
        return result;
    }

    @Transactional
    public ExaminationTypeEntity get(Long id) {
        logger.info("Получение типа обследования по {}", id);
        var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExaminationTypeEntity.class, id));
        logger.info("Полученный тип обследования {}", result);
        return result;

    }

    @Transactional
    public ExaminationTypeEntity create(ExaminationTypeEntity entity) {
        logger.info("Попытка создания типа обследования по {}", entity);

        if (entity == null) {
            logger.error("Сущность отсутствует ");

            throw new IllegalArgumentException("Entity is null");
        }
        logger.info("Сохранение сущности");

        return repository.save(entity);
    }

    @Transactional
    public ExaminationTypeEntity update(Long id, ExaminationTypeEntity entity) {
        logger.info("Попытка обновления типа обследования по {}", id);

        final ExaminationTypeEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        logger.info("Обновление типа обследования {}", existsEntity);
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public ExaminationTypeEntity delete(Long id) {
        logger.info("Попытка удаления типа обследования по {}", id);
        final ExaminationTypeEntity existsEntity = get(id);
        repository.delete(existsEntity);
        logger.info("Тип обследования удален {}", existsEntity);
        return existsEntity;
    }
}
