package com.epam.aws.training.repository;

import com.epam.aws.training.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findByName(String name);

    //Optional<ImageEntity> deleteByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image ORDER BY RANDOM() limit 1;")
    Optional<ImageEntity> findRandomEntity();
}
