package demo.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ExaminationService.class);

    public ExaminationService(ExaminationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ExaminationEntity> getAll() {
        logger.info("Попытка получить список обследований");
        var result = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        logger.info("Полученный список:{}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationEntity> getAll(long userId, int page, int size) {
        logger.info("Попытка получить список обследований по: {}", userId, page, size);
        final Pageable pageRequest = PageRequest.of(page, size);
        if (userId <= 0L) {
            return repository.findAll(pageRequest);
        }
        var result = repository.findByUserId(userId, pageRequest);
        logger.info("Полученный список", result);
        return result;
    }

    public ExaminationStatistic getStatistics(LocalDate startDate, LocalDate endDate) {
        logger.info("Попытка получить статистику по: {}", startDate, endDate);

        if (startDate.isAfter(endDate)) {
            logger.error("Некорректный период: {}");
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
        Map<LocalDate, Long> byDate = repository
                .countByDate(startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        arr -> ((LocalDate) arr[0]),
                        arr -> (Long) arr[1],
                        (a, b) -> a,
                        TreeMap::new));
        stats.setExaminationsByDate(byDate);
        logger.info("Полученная статистика: {}", stats);
        return stats;
    }

    @Transactional(readOnly = true)
    public Page<ExaminationEntity> getAllByFilters(String email, String description,
            LocalDate dateStart,
            LocalDate dateEnd, String typeName,
            String sortOrder,
            int page, int size) {
        logger.info("Попытка получить статистику по: {}", email, dateStart, dateEnd, typeName, sortOrder, page, size);
        Sort sort = Sort.by(new Order(Sort.Direction.DESC, "time"));
        if (!sortOrder.isEmpty()) {
            if ("Сначала старые".equals(sortOrder)) {
                sort = Sort.by(new Order(Sort.Direction.ASC, "time"));
            }
        }
        Pageable pageRequest = PageRequest.of(page, size, sort);
        var result = repository.findByFilter(email, description, dateStart, dateEnd, typeName, pageRequest);
        logger.info("Статистика: {}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public ExaminationEntity get(Long id) {
        logger.info("Попытка получить обследование: {}", id);

        var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExaminationEntity.class, id));
        logger.info("Обследование получено: {}", result);
        return result;
    }

    @Transactional
    public ExaminationEntity create(ExaminationEntity entity) {
        logger.info("Попытка создать обследование: {}", entity);

        if (entity == null) {
            logger.info("Обследование отсутствует: {}");

            throw new IllegalArgumentException("Entity is null");
        }
        logger.info("Сохранение обследования: {}");
        return repository.save(entity);
    }

    @Transactional
    public ExaminationEntity update(Long id, ExaminationEntity entity) {
        logger.info("Попытка обновить обследование: {}", id, entity);
        final ExaminationEntity existsEntity = get(id);
        existsEntity.setConclusion(entity.getConclusion());
        existsEntity.setDescription(entity.getDescription());
        existsEntity.setTime(entity.getTime());
        existsEntity.setExaminationType(entity.getExaminationType());
        existsEntity.setUser(entity.getUser());
        repository.save(existsEntity);
        logger.info("Обновление обследования: {}", existsEntity);
        return existsEntity;
    }

    @Transactional
    public ExaminationEntity delete(Long id) {
        logger.info("Попытка удалить обследование: {}", id);
        final ExaminationEntity existsEntity = get(id);
        repository.delete(existsEntity);
        logger.info("Удаленное обследование: {}", existsEntity);
        return existsEntity;
    }
}
