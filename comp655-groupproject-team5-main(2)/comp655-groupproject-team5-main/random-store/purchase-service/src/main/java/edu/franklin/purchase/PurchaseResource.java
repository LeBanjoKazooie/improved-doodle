package edu.franklin.purchase;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/purchases")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseResource {

    private final PurchaseService purchaseService;

    // Constructor for PurchaseService
    public PurchaseResource(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @POST
    @Operation(summary = "Create a new purchase", description = "Triggers the purchase workflow.")
    public Response createPurchase() {
        purchaseService.createPurchase();  // Call logic to create purchase
        return Response.status(Response.Status.CREATED)  // HTTP 201 Created status
                       .entity("Purchase created successfully.")
                       .build();
    }
}