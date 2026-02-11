package auca.ac.rw.restfullApiAssignment.controller.menu;

import auca.ac.rw.restfullApiAssignment.model.menu.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final List<MenuItem> menuItems = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(8);

    public MenuController() {
        menuItems.add(new MenuItem(1L, "Spring Rolls", "Crispy vegetable spring rolls", 5.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Caesar Salad", "Fresh romaine with Caesar dressing", 6.99, "Appetizer", true));
        menuItems.add(new MenuItem(3L, "Grilled Chicken", "Grilled chicken with herbs", 12.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Beef Steak", "Juicy grilled beef steak", 18.50, "Main Course", false));
        menuItems.add(new MenuItem(5L, "Chocolate Cake", "Rich chocolate layer cake", 4.50, "Dessert", true));
        menuItems.add(new MenuItem(6L, "Ice Cream", "Vanilla ice cream scoop", 3.00, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Lemonade", "Fresh squeezed lemonade", 2.50, "Beverage", true));
        menuItems.add(new MenuItem(8L, "Coffee", "Hot brewed coffee", 2.00, "Beverage", false));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItem> item = menuItems.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getItemsByCategory(@PathVariable String category) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.getCategory() != null &&
                        m.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems(@RequestParam(name = "available", defaultValue = "true") boolean available) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.isAvailable() == available)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.getName() != null &&
                        m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        long newId = idGenerator.incrementAndGet();
        menuItem.setId(newId);
        menuItems.add(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        for (MenuItem item : menuItems) {
            if (item.getId().equals(id)) {
                item.setAvailable(!item.isAvailable());
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        boolean removed = menuItems.removeIf(m -> m.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

