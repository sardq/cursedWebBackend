package demo.Repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import demo.Model.MediaEntity;
import jakarta.transaction.Transactional;

public interface MediaRepository
        extends CrudRepository<MediaEntity, Long>, PagingAndSortingRepository<MediaEntity, Long> {

    List<MediaEntity> findByExaminationId(Long examinationId);

    @Transactional
    void deleteAllByExaminationId(Long examinationId);
}