package edu.franklin.product;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService productService;

    // Constructor for ProductService
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Operation(summary = "Create a new product", description = "Triggers the product creation workflow.")
    public Response createProduct() {
        productService.createProduct();  // Call logic create product
        return Response.status(Response.Status.CREATED)  // HTTP 201 Created status
                       .entity("Product created successfully.")
                       .build();
    }
}