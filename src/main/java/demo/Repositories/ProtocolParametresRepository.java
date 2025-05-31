package demo.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import demo.Model.ProtocolParametresEntity;

public interface ProtocolParametresRepository
        extends CrudRepository<ProtocolParametresEntity, Long>,
        PagingAndSortingRepository<ProtocolParametresEntity, Long> {

    Page<ProtocolParametresEntity> findAll(Pageable pageable);

    Optional<ProtocolParametresEntity> findById(Long Id);
}