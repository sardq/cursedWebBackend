package demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import demo.Model.MediaEntity;

public interface MediaRepository
        extends CrudRepository<MediaEntity, Long>, PagingAndSortingRepository<MediaEntity, Long> {

    List<MediaEntity> findByExaminationId(Long examinationId);

    Page<MediaEntity> findByExaminationId(Long examinationId, Pageable pageable);

    Optional<MediaEntity> findById(Long Id);

}