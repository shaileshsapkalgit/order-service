package com.productorder.repository;


import com.productorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmail(String customerEmail);

    List<Order> findByProductId(Long productId);

    List<Order> findByStatus(Order.OrderStatus status);
}
