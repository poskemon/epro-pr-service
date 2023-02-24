package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select i from Item i where (:itemDescription is null or i.itemDescription like %:itemDescription%)")
    List<Item> findAllByItemDescription( String itemDescription);
}
