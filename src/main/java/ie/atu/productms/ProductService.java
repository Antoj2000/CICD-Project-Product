package ie.atu.productms;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;


import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product)
    {

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    // In the service layer, change the return type to Optional<Product> or Product
    public Optional<Product> getProductById(String productId) {

        return productRepository.findByProductId(productId);
    }

    @Transactional
    public Product updateProductStock(String productId) {
        // Check if the product with the given Product ID exists
        Optional<Product> existingProductOptional = productRepository.findByProductId(productId);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            // Deduct stock by 1
            existingProduct.setStockQuantity(existingProduct.getStockQuantity() - 1);

            // Save the updated product back to the database
            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("Product with product ID " + productId + " not found");
        }
    }

    @Transactional
    public Product updateProductInventory(String productId, int stockToAdd) {
        // Check if the product with the given Product ID exists
        Optional<Product> existingProductOptional = productRepository.findByProductId(productId);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            existingProduct.setStockQuantity(existingProduct.getStockQuantity() + stockToAdd);

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
