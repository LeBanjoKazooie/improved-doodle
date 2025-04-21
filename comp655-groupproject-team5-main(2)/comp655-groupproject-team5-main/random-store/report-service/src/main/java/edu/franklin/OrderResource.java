/**
 * Name: Prem Dahal
 * Represents a order resource class with endpoints to get all orders, get an order, and delete an order.
*/

package edu.franklin;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.logging.Logger;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    @GET
    @Operation(summary = "Fetch all orders", description = "Fetches a list of all orders.")
    @APIResponse(responseCode = "200", description = "List of orders returned successfully")
    @Tag(name = "orders Retrieval", description = "Operations for fetching orders data")
    public Uni<List<Order>> getAllStudents() {
        return Order.findAllOrders();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Fetch a order", description = "Fetches an order using given id.")
    @APIResponse(responseCode = "200", description = "order returned successfully")
    @APIResponse(responseCode = "404", description = "order not found")
    @Tag(name = "order Retrieval", description = "Operations for fetching a order data")
    public Uni<Order> getOrderById(Long id) {
        return Order.findOrderById(id)
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException("Order not found", Response.Status.NOT_FOUND));
    }

    @DELETE
    @Path("/{id}")
    @WithTransaction
    @Operation(summary = "Delete a order", description = "Deletes a order by id")
    @APIResponse(responseCode = "204", description = "order deleted successfully")
    @APIResponse(responseCode = "404", description = "order not found")
    @Tag(name = "Delete order", description = "Operations for deleting a order data")
    public Uni<RestResponse<Void>> deleteorder(Long id) {
        return Order.findById(id)
                .onItem().ifNull().failWith(() -> {
                    LOG.warnf("Delete Error: order with ID %d not found", id);
                    return new WebApplicationException("order not found", Response.Status.NOT_FOUND);
                })
                .flatMap(order -> {
                    LOG.infof("Deleting order with ID %d", id);
                    return order.delete()
                            .replaceWith(RestResponse.ResponseBuilder.<Void>create(Response.Status.ACCEPTED)
                                    .header("Location", URI.create("/reactive-api/orders" + id))
                                    .build());
                });
    }
}
