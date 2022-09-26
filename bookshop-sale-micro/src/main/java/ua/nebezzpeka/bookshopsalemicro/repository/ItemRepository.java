package ua.nebezzpeka.bookshopsalemicro.repository;

import ua.nebezzpeka.bookshopsalemicro.entity.Item;
import ua.nebezzpeka.bookshopsalemicro.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
