package com.muccio.dscommerce.dto;

import com.muccio.dscommerce.entities.Order;
import com.muccio.dscommerce.entities.OrderItem;
import com.muccio.dscommerce.entities.enums.OrderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;
    private ClientDTO client;
    private PaymentDTO payment;
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(ClientDTO client, Long id, Instant moment, PaymentDTO payment, OrderStatus status) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        status = entity.getStatus();
        client = new ClientDTO(entity.getClient());
        payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
        for (OrderItem item : entity.getItems()) {
            items.add(new OrderItemDTO(item));
        }
    }

    public ClientDTO getClient() {
        return client;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Instant getMoment() {
        return moment;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubtotal();
        }
        return sum;
    }
}
