package demo.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import demo.Model.TicketEntity;

public interface TicketRepository
        extends CrudRepository<TicketEntity, Long>, PagingAndSortingRepository<TicketEntity, Long> {

    Optional<TicketEntity> findById(Long Id);

    Page<TicketEntity> findAll(Pageable pageable);
}