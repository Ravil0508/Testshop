package com.solution.testshop.repository;

import com.solution.testshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<List<Order>> findOrderByUserId(Long id);

    Optional<List<Order>> findOrderByOrderConfirmationIsFalse();

    Optional<List<Order>> findOrderByOrderConfirmationIsTrue();
}
