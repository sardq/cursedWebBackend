package demo.Service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.Model.TicketEntity;
import demo.Repositories.TicketRepository;
import demo.core.error.NotFoundException;

@Service
public class TicketService {
    private final TicketRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getAll() {
        logger.info("Попытка получить список тикетов");

        var result = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        logger.info("Полученные тикеты: {}", result);
        return result;

    }

    @Transactional(readOnly = true)
    public Page<TicketEntity> getAll(int page, int size) {
        logger.info("Попытка получить список тикетов: {}", page, size);

        var result = repository.findAll(PageRequest.of(page, size));
        logger.info("Полученные тикеты: {}", result);
        return result;

    }

    @Transactional(readOnly = true)
    public TicketEntity get(Long id) {
        logger.info("Попытка получить тикет пользователя: {}", id);
        var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TicketEntity.class, id));
        logger.info("Тикет получен: {}", result);
        return result;
    }

    @Transactional
    public TicketEntity create(TicketEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    // @Transactional
    // public TicketEntity update(Long id, TicketEntity entity) {
    // final TicketEntity existsEntity = get(id);
    // existsEntity.setMessage(entity.getMessage());
    // existsEntity.setAnswer(entity.getAnswer());
    // existsEntity.setStatus(entity.getStatus());
    // repository.save(existsEntity);
    // return existsEntity;
    // }

    // @Transactional
    // public TicketEntity delete(Long id) {
    // final TicketEntity existsEntity = get(id);
    // repository.delete(existsEntity);
    // return existsEntity;
    // }
}
