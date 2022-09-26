package ua.nebezzpeka.bookshopsalemicro.repository;

import ua.nebezzpeka.bookshopsalemicro.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> getCustomerByUserName(String userName);

    boolean existsByUserName(String userName);
}
