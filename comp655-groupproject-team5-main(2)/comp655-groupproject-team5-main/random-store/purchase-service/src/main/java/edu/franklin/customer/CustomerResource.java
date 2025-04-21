package edu.franklin.customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService;

    // Constructor for CustomerService
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    @Operation(summary = "Create a new customer", description = "Triggers the customer creation workflow.")
    public Response createCustomer() {
        customerService.createCustomer();  // Call logic to create customer
        return Response.status(Response.Status.CREATED)  // HTTP 201 Created status
                       .entity("Customer created successfully.")
                       .build();
    }
}