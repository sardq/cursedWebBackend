package demo.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import demo.Model.UserEntity;
import demo.Model.UserRole;

public interface UserRepository extends CrudRepository<UserEntity, Long>,
        PagingAndSortingRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long Id);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Page<UserEntity> findAll(Pageable pageable);

    Page<UserEntity> findByRole(UserRole role, Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE " +
            "(:email IS NULL OR u.email ILIKE %:email%) ")
    Page<UserEntity> findByFilter(
            @Param("email") String email, Pageable pageable);
}
