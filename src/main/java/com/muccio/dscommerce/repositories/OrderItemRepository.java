package com.muccio.dscommerce.repositories;

import com.muccio.dscommerce.entities.OrderItem;
import com.muccio.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
