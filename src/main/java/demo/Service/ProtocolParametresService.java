package demo.Service;

import org.springframework.transaction.annotation.Transactional;

import demo.Model.ProtocolParametresEntity;
import demo.Repositories.ProtocolParametresRepository;
import demo.core.error.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProtocolParametresService {
    private final ProtocolParametresRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ProtocolParametresService.class);

    public ProtocolParametresService(ProtocolParametresRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProtocolParametresEntity> getAll(Long examinationId, int page, int size) {
        logger.info("Получить список параметров:{}", examinationId, page, size);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (examinationId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByExaminationId(examinationId, pageRequest);
        }
        var result = repository.findAll(pageRequest);
        logger.info("Получен список параметров:{}", result);
        return result;
    }

    @Transactional
    public ProtocolParametresEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProtocolParametresEntity.class, id));
    }

    @Transactional
    public ProtocolParametresEntity create(ProtocolParametresEntity entity) {
        logger.info("Получить список параметров:{}", entity);

        if (entity == null) {
            logger.error("Получить список параметров");
            throw new IllegalArgumentException("Entity is null");
        }
        var result = repository.save(entity);
        logger.info("Получен список значений параметров", result);
        return result;
    }

    @Transactional
    public ProtocolParametresEntity update(Long id, ProtocolParametresEntity entity) {
        logger.info("Попытка обновить значение протокола:{}", id, entity);

        final ProtocolParametresEntity existsEntity = get(id);
        existsEntity.setBody(entity.getBody());
        repository.save(existsEntity);
        logger.info("Значение протокола обновлено:{}", existsEntity);
        return existsEntity;
    }

    @Transactional
    public ProtocolParametresEntity delete(Long id) {
        logger.info("Попытка удалить значение протокола:{}", id);
        final ProtocolParametresEntity existsEntity = get(id);
        repository.delete(existsEntity);
        logger.info("Значение протокола удалено:{}", existsEntity);

        return existsEntity;
    }
}
