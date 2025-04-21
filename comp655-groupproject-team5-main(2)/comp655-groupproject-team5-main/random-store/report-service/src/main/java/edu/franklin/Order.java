/**
 * Name: Prem Dahal
 * Represents an Order with order id, customerid, productid, totalAmount of the order, and order time.
*/

package edu.franklin;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {

    @Column(nullable = false)
    public Long customerId;

    @Column(nullable = false)
    public Long productId;

    @Column(nullable = false)
    public int quantity;

    @Column(nullable = false)
    public double totalAmount;

    @Column(nullable = false)
    public LocalDateTime time;

    public static Uni<List<Order>> findAllOrders() {
        return Order.listAll();
    }

    public static Uni<Order> findOrderById(Long id) {
        return Order.findById(id);
    }

    public static Uni<Void> deleteStudent(Long id) {
        return Order.findById(id)
                .onItem().transformToUni(student -> {
                    if (student != null) {
                        return student.delete();
                    } else {
                        return Uni.createFrom().voidItem();
                    }
                });
    }
}
