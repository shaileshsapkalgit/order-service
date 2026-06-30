package com.productorder.service;


import com.productorder.entity.Order;
import com.productorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    // get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // get order by id
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    // place new order — calls product-service to validate product
    public Order createOrder(Order order) {

        // call product-service to get product details
        Map<String, Object> product = productClient.getProductById(order.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + order.getProductId());
        }

        // set product name and calculate total price from product-service response
        order.setProductName((String) product.get("name"));
        Double price = ((Number) product.get("price")).doubleValue();
        order.setTotalPrice(price * order.getQuantity());

        return orderRepository.save(order);
    }

    // update order status
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // cancel order
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    // get orders by customer email
    public List<Order> getOrdersByCustomer(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    // get orders by status
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
