package demo.Repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import demo.Model.ExaminationEntity;

public interface ExaminationRepository
        extends CrudRepository<ExaminationEntity, Long>, PagingAndSortingRepository<ExaminationEntity, Long> {

    Page<ExaminationEntity> findAll(Pageable pageable);

    @Query("SELECT e FROM ExaminationEntity e WHERE " +
            "(:typeName IS NULL OR e.examinationType.name ILIKE %:typeName%) " +
            "AND ((:dateStart IS NULL OR e.time >= :dateStart) " +
            "AND (:dateEnd IS NULL OR e.time <= :dateEnd))")
    Page<ExaminationEntity> findByFilter(
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd,
            @Param("typeName") String typeName, Pageable pageable);

    Optional<ExaminationEntity> findById(Long Id);

    List<ExaminationEntity> findByUserId(Long userId);

    Page<ExaminationEntity> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM ExaminationEntity e WHERE e.time BETWEEN :startDate AND :endDate")
    Long countByPeriod(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT e.examinationType.name, COUNT(e) FROM ExaminationEntity e " +
            "WHERE e.time BETWEEN :startDate AND :endDate " +
            "GROUP BY e.examinationType.name")
    List<Object[]> countByTypeAndPeriod(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}