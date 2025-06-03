package demo.Repositories;

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
            "AND (:description IS NULL OR e.description ILIKE %:description%) " +
            "AND (cast(:dateStart as date) IS NULL OR e.time >= cast(:dateStart as date)) " +
            "AND (e.user.email ILIKE CONCAT('%', :email, '%')) " +
            "AND (cast(:dateEnd as date) IS NULL OR e.time <= cast(:dateEnd as date))")
    Page<ExaminationEntity> findByFilter(
            @Param("email") String email,
            @Param("description") String description,
            @Param("dateStart") LocalDate dateStart,
            @Param("dateEnd") LocalDate dateEnd,
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

    @Query("SELECT e.time, COUNT(e) FROM ExaminationEntity e " +
            "WHERE e.time BETWEEN :startDate AND :endDate " +
            "GROUP BY e.time " +
            "ORDER BY e.time")
    List<Object[]> countByDate(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}