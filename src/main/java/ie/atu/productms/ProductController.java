package ie.atu.productms;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService myService;  //Dependency Injection for service

    public ProductController(ProductService myService) {

        this.myService = myService;
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, BindingResult result ){
        if (result.hasErrors()){
            List<ErrorDetails> errors = new ArrayList<>();
            result.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(new ErrorDetails(fieldName, errorMessage));
            });
            return ResponseEntity.badRequest().body(errors);
        }
        //Delegates logic to service layer
        Product savedProduct = myService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = myService.getAllProducts();
        return ResponseEntity.ok(products);  // Returns 200 OK with list of persons
    }


    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        // Attempt to get the product by its productId
        Optional<Product> product = myService.getProductById(productId);

        if (product.isPresent()) {
            // If the product is found, return a response with product details
            return ResponseEntity.status(HttpStatus.OK).body(product.get()); // or product.get().getName() for specific fields
        } else {
            // If the product is not found, return a 404 NOT FOUND response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with Product ID " + productId + " not found.");
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?>updateProduct(@PathVariable String productId, @RequestBody @Valid Product updatedProduct, BindingResult result){
        if (result.hasErrors()) {
            List<ErrorDetails>errors = new ArrayList<>();
            result.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(new ErrorDetails(fieldName, errorMessage));
            });
            return ResponseEntity.badRequest().body(errors);
        }

        Product updatedProducts = myService.updateProduct(productId, updatedProduct);
        if (updatedProducts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with Product ID: " + productId);
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deletePerson(@PathVariable String productId){
        try {
            myService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with Product ID " + productId + " not found");
        }
    }





}
