package demo.Service;

import java.util.List;
import java.util.stream.StreamSupport;

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

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TicketEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<TicketEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public TicketEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TicketEntity.class, id));
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
