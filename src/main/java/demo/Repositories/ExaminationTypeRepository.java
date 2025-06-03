package demo.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import demo.Model.ExaminationTypeEntity;

public interface ExaminationTypeRepository
        extends CrudRepository<ExaminationTypeEntity, Long>, PagingAndSortingRepository<ExaminationTypeEntity, Long> {

    Page<ExaminationTypeEntity> findAll(Pageable pageable);

    Optional<ExaminationTypeEntity> findById(Long Id);

    @Query("SELECT e FROM ExaminationTypeEntity e WHERE " +
            "(:name IS NULL OR e.name ILIKE %:name%) ")
    Page<ExaminationTypeEntity> findByFilter(
            @Param("name") String name, Pageable pageable);
}