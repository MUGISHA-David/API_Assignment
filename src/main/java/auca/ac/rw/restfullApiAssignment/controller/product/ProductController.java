package auca.ac.rw.restfullApiAssignment.controller.product;

import auca.ac.rw.restfullApiAssignment.model.product.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(10);

    public ProductController() {
        products.add(new Product(1L, "Laptop", "15 inch laptop", 1200.0, "Electronics", 5, "Dell"));
        products.add(new Product(2L, "Smartphone", "Android smartphone", 800.0, "Electronics", 0, "Samsung"));
        products.add(new Product(3L, "Headphones", "Noise cancelling headphones", 150.0, "Accessories", 20, "Sony"));
        products.add(new Product(4L, "Office Chair", "Ergonomic office chair", 250.0, "Furniture", 10, "Ikea"));
        products.add(new Product(5L, "Desk Lamp", "LED desk lamp", 40.0, "Home", 30, "Philips"));
        products.add(new Product(6L, "Running Shoes", "Comfortable running shoes", 90.0, "Sportswear", 15, "Nike"));
        products.add(new Product(7L, "Backpack", "Travel backpack", 60.0, "Accessories", 8, "Adidas"));
        products.add(new Product(8L, "Coffee Maker", "Automatic coffee maker", 110.0, "Home Appliances", 12, "Bosch"));
        products.add(new Product(9L, "Monitor", "24 inch monitor", 200.0, "Electronics", 7, "LG"));
        products.add(new Product(10L, "Keyboard", "Mechanical keyboard", 80.0, "Electronics", 0, "Logitech"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "limit", required = false) Integer limit) {

        List<Product> sorted = products.stream()
                .sorted(Comparator.comparing(Product::getProductId))
                .collect(Collectors.toList());

        if (page == null || limit == null || page < 0 || limit <= 0) {
            return ResponseEntity.ok(sorted);
        }

        int fromIndex = page * limit;
        if (fromIndex >= sorted.size()) {
            return ResponseEntity.ok(List.of());
        }
        int toIndex = Math.min(fromIndex + limit, sorted.size());
        return ResponseEntity.ok(sorted.subList(fromIndex, toIndex));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> result = products.stream()
                .filter(p -> p.getCategory() != null &&
                        p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> result = products.stream()
                .filter(p -> p.getBrand() != null &&
                        p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword) {
        String lower = keyword.toLowerCase();
        List<Product> result = products.stream()
                .filter(p ->
                        (p.getName() != null && p.getName().toLowerCase().contains(lower)) ||
                                (p.getDescription() != null && p.getDescription().toLowerCase().contains(lower))
                )
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        List<Product> result = products.stream()
                .filter(p -> p.getPrice() != null &&
                        p.getPrice() >= min &&
                        p.getPrice() <= max)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> result = products.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        long newId = idGenerator.incrementAndGet();
        product.setProductId(newId);
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updated) {
        for (Product p : products) {
            if (p.getProductId().equals(productId)) {
                p.setName(updated.getName());
                p.setDescription(updated.getDescription());
                p.setPrice(updated.getPrice());
                p.setCategory(updated.getCategory());
                p.setStockQuantity(updated.getStockQuantity());
                p.setBrand(updated.getBrand());
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long productId,
            @RequestParam("quantity") int quantity) {
        for (Product p : products) {
            if (p.getProductId().equals(productId)) {
                p.setStockQuantity(quantity);
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean removed = products.removeIf(p -> p.getProductId().equals(productId));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

