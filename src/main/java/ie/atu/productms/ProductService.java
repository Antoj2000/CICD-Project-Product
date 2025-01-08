package ie.atu.productms;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;


import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product)
    {
        return  productRepository.save(product);
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(String productId, Product updatedProduct) {
        // Check if the product with the given Product ID exists
        Optional<Product> existingProductOptional = productRepository.findByProductId(productId);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            // Update the fields of the existing person with the new values
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setProductId(updatedProduct.getProductId());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());

            // Save the updated product back to the database
            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("Product with product ID " + productId + " not found");
        }
    }

    public void deleteProduct(String productId) {
        // Find person by productId (assuming productId is unique)
        Optional<Product> productOptional = productRepository.findAll().stream()
                .filter(person -> person.getProductId().equals(productId))
                .findFirst();

        if (productOptional.isPresent()) {
            // Delete the person
            productRepository.delete(productOptional.get());
        } else {
            throw new IllegalArgumentException("Product with Product ID " + productId + " not found");
        }

    }











}
