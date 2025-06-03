package demo.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.DTO.ExaminationStatistic;
import demo.Model.ExaminationEntity;
import demo.Repositories.ExaminationRepository;
import demo.core.error.NotFoundException;

@Service
public class ExaminationService {
    private final ExaminationRepository repository;

    public ExaminationService(ExaminationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ExaminationEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<ExaminationEntity> getAll(long userId, int page, int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        if (userId <= 0L) {
            return repository.findAll(pageRequest);
        }
        return repository.findByUserId(userId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<ExaminationEntity> getAllByUser(int page, int size, Long userId) {
        Pageable pageRequest = PageRequest.of(page, size);
        if (userId != null) {
            pageRequest = PageRequest.of(page, size);
            return repository.findByUserId(userId, pageRequest);
        }
        return repository.findAll(pageRequest);
    }

    public ExaminationStatistic getStatistics(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Некорректный период");
        }

        ExaminationStatistic stats = new ExaminationStatistic();

        stats.setTotalExaminations(
                repository.countByPeriod(startDate, endDate));

        Map<String, Long> byType = repository
                .countByTypeAndPeriod(startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]));
        stats.setExaminationsByType(byType);

        return stats;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationEntity> getAllByFilters(String email, String description,
            LocalDate dateStart,
            LocalDate dateEnd, String typeName,
            String sortOrder,
            int page, int size) {

        Sort sort = Sort.by(new Order(Sort.Direction.DESC, "time"));
        if (!sortOrder.isEmpty()) {
            if ("Сначала старые".equals(sortOrder)) {
                sort = Sort.by(new Order(Sort.Direction.ASC, "time"));
            }
        }
        Pageable pageRequest = PageRequest.of(page, size, sort);
        return repository.findByFilter(email, description, dateStart, dateEnd, typeName, pageRequest);
    }

    @Transactional(readOnly = true)
    public ExaminationEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExaminationEntity.class, id));
    }

    @Transactional
    public ExaminationEntity create(ExaminationEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }

        return repository.save(entity);
    }

    @Transactional
    public ExaminationEntity update(Long id, ExaminationEntity entity) {
        final ExaminationEntity existsEntity = get(id);
        existsEntity.setConclusion(entity.getConclusion());
        existsEntity.setDescription(entity.getDescription());
        existsEntity.setTime(entity.getTime());
        existsEntity.setExaminationType(entity.getExaminationType());
        existsEntity.setUser(entity.getUser());
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public ExaminationEntity delete(Long id) {
        final ExaminationEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
