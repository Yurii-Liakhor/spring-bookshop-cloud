package ua.nebezzpeka.bookshopsalemicro.repository;

import ua.nebezzpeka.bookshopsalemicro.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> getSaleByOrderCode(String orderCode);

    Optional<Order> getSaleByOrderCodeAndUserName(String orderCode, String userName);
}
