package com.muccio.dscommerce.services;

import com.muccio.dscommerce.dto.OrderDTO;
import com.muccio.dscommerce.dto.OrderItemDTO;
import com.muccio.dscommerce.entities.Order;
import com.muccio.dscommerce.entities.OrderItem;
import com.muccio.dscommerce.entities.Product;
import com.muccio.dscommerce.entities.User;
import com.muccio.dscommerce.entities.enums.OrderStatus;
import com.muccio.dscommerce.repositories.OrderItemRepository;
import com.muccio.dscommerce.repositories.OrderRepository;
import com.muccio.dscommerce.repositories.ProductRepository;
import com.muccio.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado."));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        User user = userService.authenticated();
        order.setClient(user);

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
