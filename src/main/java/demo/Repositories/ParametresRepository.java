package demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import demo.Model.ParametresEntity;

public interface ParametresRepository
        extends CrudRepository<ParametresEntity, Long>, PagingAndSortingRepository<ParametresEntity, Long> {

    Page<ParametresEntity> findAll(Pageable pageable);

    Optional<ParametresEntity> findById(Long Id);

    List<ParametresEntity> findByExaminationTypeId(Long examinationTypeId);

    Page<ParametresEntity> findByExaminationTypeId(Long examinationTypeId, Pageable pageable);
}