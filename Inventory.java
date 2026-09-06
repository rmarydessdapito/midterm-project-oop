import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private static final String[] VALID_CATEGORIES = {"Clothing", "Electronics", "Entertainment"};

    private static final int MIN_ID_LENGTH = 3;
    private static final int MAX_ID_LENGTH = 20;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 40;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean isCategoryValid(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }
        String trimmed = category.trim();
        for (String validCategory : VALID_CATEGORIES) {
            if (trimmed.equalsIgnoreCase(validCategory)) {
                return true;
            }
        }
        return false;
    }

    public String normalizeCategory(String category) {
        if (category == null) {
            return null;
        }
        String trimmed = category.trim();
        for (String validCategory : VALID_CATEGORIES) {
            if (trimmed.equalsIgnoreCase(validCategory)) {
                return validCategory;
            }
        }
        return null;
    }

    /**
     * ID must be 3-20 characters and contain only letters, digits, hyphens, and underscores (no spaces or other symbols).
     */
    
    public boolean isIdValid(String id) {
        if (id == null) {
            return false;
        }
        String trimmed = id.trim();
        if (trimmed.length() < MIN_ID_LENGTH || trimmed.length() > MAX_ID_LENGTH) {
            return false;
        }
        return trimmed.matches("[A-Za-z0-9_-]+");
    }

    
    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            return false;
        }
        if (trimmed.contains("  ")) {
            return false;
        }
        return trimmed.matches(".*[A-Za-z].*");
    }

    public boolean isQuantityValid(int quantity) {
        return quantity >= 0 && quantity <= Item.MAX_QUANTITY;
    }

    public boolean isPriceValid(double price) {
        return Double.isFinite(price) && price >= 0 && price <= Item.MAX_PRICE;
    }

    public boolean isIdDuplicate(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String trimmed = id.trim();
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(trimmed)) {
                return true;
            }
        }
        return false;
    }

    public Item findItemById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        String trimmed = id.trim();
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(trimmed)) {
                return item;
            }
        }
        return null;
    }

    public boolean addItem(String id, String name, int quantity, double price, String category) {
        if (!isIdValid(id)) {
            return false;
        }
        if (!isNameValid(name)) {
            return false;
        }
        if (!isQuantityValid(quantity)) {
            return false;
        }
        if (!isPriceValid(price)) {
            return false;
        }
        if (!isCategoryValid(category)) {
            return false;
        }

        String trimmedId = id.trim();
        String trimmedName = name.trim();
        String normalizedCategory = normalizeCategory(category);

        if (isIdDuplicate(trimmedId)) {
            return false;
        }

        Item newItem = new Item(trimmedId, trimmedName, quantity, price, normalizedCategory);
        items.add(newItem);
        return true;
    }

    public Item updateItemQuantity(String id, int newQuantity) {
        Item item = findItemById(id);
        if (item != null && isQuantityValid(newQuantity)) {
            item.setQuantity(newQuantity);
        }
        return item;
    }

    public Item updateItemPrice(String id, double newPrice) {
        Item item = findItemById(id);
        if (item != null && isPriceValid(newPrice)) {
            item.setPrice(newPrice);
        }
        return item;
    }

    public Item removeItem(String id) {
        Item item = findItemById(id);
        if (item != null) {
            items.remove(item);
        }
        return item;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public List<Item> getItemsByCategory(String category) {
        List<Item> result = new ArrayList<>();
        String normalized = normalizeCategory(category);
        if (normalized != null) {
            for (Item item : items) {
                if (item.getCategory().equals(normalized)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public List<Item> getLowStockItems() {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() <= 5) {
                result.add(item);
            }
        }
        return result;
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Item> sortByQuantity(boolean ascending) {
        List<Item> sorted = new ArrayList<>(items);

        if (ascending) {
            sorted.sort((item1, item2) -> Integer.compare(item1.getQuantity(), item2.getQuantity()));
        } else {
            sorted.sort((item1, item2) -> Integer.compare(item2.getQuantity(), item1.getQuantity()));
        }

        return sorted;
    }

    public List<Item> sortByPrice(boolean ascending) {
        List<Item> sorted = new ArrayList<>(items);

        if (ascending) {
            sorted.sort((item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));
        } else {
            sorted.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
        }

        return sorted;
    }
}
