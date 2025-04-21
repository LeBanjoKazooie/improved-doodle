/* Author: Prem Dahal
 * Consumes order messages from the Purchase service via RabbitMQ,
 * saves them to the Report service's database, and sends the order_id
 * back to the Purchase service via another RabbitMQ exchange.
 */

package edu.franklin;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@ApplicationScoped
public class OrderConsumer {

    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    @Incoming("orders-in")
    @Outgoing("order-confirm")
    @WithTransaction

    public Uni<String> consumeAndRespond(Message<byte[]> msg) {
        byte[] payload = msg.getPayload();
        String rawJson = new String(payload, StandardCharsets.UTF_8);
        LOG.infof("Raw message: " + rawJson);

        JsonObject json = new JsonObject(rawJson);

        Order order = new Order();
        order.customerId = json.getLong("customerId");
        order.productId = json.getLong("productId");
        order.quantity = json.getInteger("quantity");
        order.totalAmount = json.getDouble("totalAmount");
        order.time = LocalDateTime.now();

        return order.persistAndFlush()
                .invoke(() -> LOG.infof("Order persisted with ID: %d", order.id))
                .replaceWith(() -> String.valueOf(order.id));
    }
}
