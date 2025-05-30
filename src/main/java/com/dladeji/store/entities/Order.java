package com.dladeji.store.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST ,orphanRemoval = true)
    private Set<OrderItem> items = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public void addItems(Set<CartItem> cartItems){
        try {
            cartItems.forEach((item) -> {
                // Consider a builder class here
                var orderItem = new OrderItem();
                orderItem.setOrder(this);
                orderItem.setProduct(item.getProduct());
                orderItem.setUnitPrice(orderItem.getProduct().getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setTotalPrice(item.getTotalPrice());
                this.items.add(orderItem);
            });
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
    }

}
