import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.List;

public class InventorySystem {
    private static final int BANNER_WIDTH = 82;

    private Inventory inventory;
    private Scanner scanner;

    public InventorySystem() {
        this.inventory = new Inventory();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
     
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        InventorySystem app = new InventorySystem();
        app.run();
    }

    public void run() {

        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getMenuChoice(1, 9);

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    updateItem();
                    break;
                case 3:
                    removeItem();
                    break;
                case 4:
                    displayItemsByCategory();
                    break;
                case 5:
                    displayAllItems();
                    break;
                case 6:
                    searchItem();
                    break;
                case 7:
                    sortItems();
                    break;
                case 8:
                    displayLowStockItems();
                    break;
                case 9:
                    System.out.println("\nThank you for using my Inventory Management System. Bye!");
                    running = false;
                    break;
            }
        }

        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(BANNER_WIDTH));
        System.out.println(centerText("INVENTORY MANAGEMENT SYSTEM", BANNER_WIDTH));
        System.out.println("=".repeat(BANNER_WIDTH));
        System.out.println("\nWelcome to the Inventory Management System!");
        System.out.println("\n1. Add Item");
        System.out.println("2. Update Item");
        System.out.println("3. Remove Item");
        System.out.println("4. Display Items by Category");
        System.out.println("5. Display All Items");
        System.out.println("6. Search Item");
        System.out.println("7. Sort Items");
        System.out.println("8. Display Low Stock Items");
        System.out.println("9. Exit");
        System.out.print("\nEnter your choice: ");
    }

    private String centerText(String text, int width) {
        int totalPadding = width - text.length();
        if (totalPadding <= 0) {
            return text;
        }
        int leftPadding = totalPadding / 2;
        return " ".repeat(leftPadding) + text;
    }

    private void printSectionHeader(String title) {
        System.out.println("\n" + "-".repeat(BANNER_WIDTH));
        System.out.println(title);
        System.out.println("-".repeat(BANNER_WIDTH));
    }

    private String formatPrice(double price) {
        return "\u20B1" + String.format("%,.2f", price);
    }

    private int getMenuChoice(int min, int max) {
        return getMenuChoice(min, max, false);
    }

    private int getMenuChoice(int min, int max, boolean allowBack) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.print("Please enter a valid number: ");
                continue;
            }

            // Reject anything that isn't a plain whole number (no decimals, no signs,
            // no commas/symbols) before attempting to parse it.
            if (!input.matches("\\d+")) {
                System.out.print("Please enter a valid number: ");
                continue;
            }

            try {
                choice = Integer.parseInt(input);
                if (allowBack && choice == 0) {
                    validInput = true;
                } else if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            }
        }

        return choice;
    }

    private Integer getQuantityInput() {
        Integer quantity = null;
        boolean validInput = false;

        while (!validInput) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return null;
            }

            if (input.isEmpty()) {
                System.out.print("Quantity cannot be empty. Please enter a valid quantity (or 'back' to cancel): ");
                continue;
            }

            // Only a plain whole number is allowed: no decimals, commas, letters, or symbols.
            if (!input.matches("-?\\d+")) {
                System.out.print("Quantity must be a whole number with no letters, commas, or symbols. Please try again: ");
                continue;
            }

            long parsed;
            try {
                parsed = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.print("That quantity is too large. Please enter a valid quantity: ");
                continue;
            }

            if (parsed < 0) {
                System.out.print("Quantity cannot be negative. Please enter a valid quantity: ");
                continue;
            }

            if (parsed > Item.MAX_QUANTITY) {
                System.out.print("Quantity cannot exceed " + Item.MAX_QUANTITY + ". Please enter a valid quantity: ");
                continue;
            }

            quantity = (int) parsed;
            validInput = true;
        }

        return quantity;
    }

    private Double getPriceInput() {
        Double price = null;
        boolean validInput = false;

        while (!validInput) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return null;
            }

            if (input.isEmpty()) {
                System.out.print("Price cannot be empty. Please enter a valid price (\u20B1) (or 'back' to cancel): ");
                continue;
            }

            // Only digits with an optional decimal point and at most 2 decimal places.
            // Reject negative signs, commas, currency symbols, letters, and NaN/Infinity.
            if (!input.matches("\\d+(\\.\\d{1,2})?")) {
                System.out.print("Price must be a number with at most 2 decimal places, no letters, commas, or currency symbols. Please try again: ");
                continue;
            }

            double parsed;
            try {
                parsed = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid price: ");
                continue;
            }

            if (!inventory.isPriceValid(parsed)) {
                System.out.print("Price must be between \u20B10.00 and " + formatPrice(Item.MAX_PRICE) + ". Please enter a valid price: ");
                continue;
            }

            price = parsed;
            validInput = true;
        }

        return price;
    }

    private String getCategoryInput() {
        String category = "";
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Select Category:");
            System.out.println("\n1. Clothing");
            System.out.println("2. Electronics");
            System.out.println("3. Entertainment");
            System.out.print("\nEnter Category (name, number, or 'back' to cancel): ");
            String rawInput = scanner.nextLine().trim();

            if (rawInput.equalsIgnoreCase("back")) {
                return null;
            }

            category = applyCategoryShortcut(rawInput);

            if (category.isEmpty()) {
                System.out.println("Category cannot be empty!");
                continue;
            }

            if (inventory.isCategoryValid(category)) {
                validInput = true;
            } else {
                System.out.println("Category " + category + " does not exist!");
            }
        }

        return inventory.normalizeCategory(category);
    }

    private String applyCategoryShortcut(String input) {
        if (input.equals("1")) {
            return "Clothing";
        } else if (input.equals("2")) {
            return "Electronics";
        } else if (input.equals("3")) {
            return "Entertainment";
        }
        return input;
    }

    private String getIdInput() {
        String id = "";
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter Item ID (or 'back' to cancel): ");
            id = scanner.nextLine().trim();

            if (id.equalsIgnoreCase("back")) {
                return null;
            }

            if (id.isEmpty()) {
                System.out.println("ID cannot be empty!");
                continue;
            }

            if (!inventory.isIdValid(id)) {
                System.out.println("ID must be 3-20 characters long and contain only letters, numbers, hyphens (-), and underscores (_).");
                continue;
            }

            validInput = true;
        }

        return id;
    }

    private String getNameInput() {
        String name = "";
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter Item Name (or 'back' to cancel): ");
            name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("back")) {
                return null;
            }

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                continue;
            }

            if (!inventory.isNameValid(name)) {
                System.out.println("Name must be 2-40 characters, contain at least one letter, and must not contain double spaces.");
                continue;
            }

            validInput = true;
        }

        return name;
    }

    private void cancelOperation() {
        System.out.println("Operation cancelled. Returning to main menu...");
    }

    private void addItem() {
        printSectionHeader("Add Item");

        String category = getCategoryInput();
        if (category == null) {
            cancelOperation();
            return;
        }

        String id = getIdInput();
        if (id == null) {
            cancelOperation();
            return;
        }

        if (inventory.isIdDuplicate(id)) {
            System.out.println("Item with ID " + id + " already exists!");
            return;
        }

        String name = getNameInput();
        if (name == null) {
            cancelOperation();
            return;
        }

        System.out.print("Enter Quantity (or 'back' to cancel): ");
        Integer quantity = getQuantityInput();
        if (quantity == null) {
            cancelOperation();
            return;
        }

        System.out.print("Enter Price (\u20B1) (or 'back' to cancel): ");
        Double price = getPriceInput();
        if (price == null) {
            cancelOperation();
            return;
        }

        if (inventory.addItem(id, name, quantity, price, category)) {
            System.out.println("Item added successfully!");
        } else {
            System.out.println("Failed to add item!");
        }
    }

    private void updateItem() {
        printSectionHeader("Update Item");
        String id = getIdInput();
        if (id == null) {
            cancelOperation();
            return;
        }

        Item item = inventory.findItemById(id);

        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        System.out.println("\n1. Update Quantity");
        System.out.println("2. Update Price");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
        int choice = getMenuChoice(1, 2, true);

        if (choice == 0) {
            cancelOperation();
            return;
        }

        if (choice == 1) {
            System.out.print("Enter new Quantity (or 'back' to cancel): ");
            Integer newQuantity = getQuantityInput();
            if (newQuantity == null) {
                cancelOperation();
                return;
            }
            int oldQuantity = item.getQuantity();
            inventory.updateItemQuantity(id, newQuantity);
            System.out.println("Quantity of Item " + item.getName() + " is updated from " +
                    oldQuantity + " to " + newQuantity);
        } else {
            System.out.print("Enter new Price (\u20B1) (or 'back' to cancel): ");
            Double newPrice = getPriceInput();
            if (newPrice == null) {
                cancelOperation();
                return;
            }
            double oldPrice = item.getPrice();
            inventory.updateItemPrice(id, newPrice);
            System.out.println("Price of Item " + item.getName() + " is updated from " +
                    formatPrice(oldPrice) + " to " + formatPrice(newPrice));
        }
    }

    private void removeItem() {
        printSectionHeader("Remove Item");
        String id = getIdInput();
        if (id == null) {
            cancelOperation();
            return;
        }

        Item item = inventory.removeItem(id);

        if (item != null) {
            System.out.println("Item " + item.getName() + " has been removed from the inventory");
        } else {
            System.out.println("Item not found!");
        }
    }

    private void displayItemsByCategory() {
        printSectionHeader("Display Items by Category");
        System.out.println("\n1. Clothing");
        System.out.println("2. Electronics");
        System.out.println("3. Entertainment");
        System.out.print("\nEnter Category (name, number, or 'back' to cancel): ");
        String rawInput = scanner.nextLine().trim();

        if (rawInput.equalsIgnoreCase("back")) {
            cancelOperation();
            return;
        }

        String category = applyCategoryShortcut(rawInput);

        if (!inventory.isCategoryValid(category)) {
            System.out.println("Category " + category + " does not exist!");
            return;
        }

        String normalizedCategory = inventory.normalizeCategory(category);
        List<Item> items = inventory.getItemsByCategory(normalizedCategory);

        if (items.isEmpty()) {
            System.out.println("No items found in category " + normalizedCategory);
            return;
        }

        System.out.println("\n" + normalizedCategory + " Items:");
        printItemTable(items, false);
    }

    private void displayAllItems() {
        printSectionHeader("Display All Items");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty!");
            return;
        }

        List<Item> items = inventory.getAllItems();
        printItemTable(items, true);
    }

    private void searchItem() {
        printSectionHeader("Search Item");
        String id = getIdInput();
        if (id == null) {
            cancelOperation();
            return;
        }

        Item item = inventory.findItemById(id);

        if (item != null) {
            System.out.println("\nItem Found:");
            System.out.printf("%-15s %-25s %-12s %-14s %s\n", "ID", "Name", "Quantity", "Price", "Category");
            System.out.printf("%-15s %-25s %-12d %-14s %s\n",
                    item.getId(), item.getName(), item.getQuantity(), formatPrice(item.getPrice()), item.getCategory());
        } else {
            System.out.println("Item not found!");
        }
    }

    private void sortItems() {
        printSectionHeader("Sort Items");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty!");
            return;
        }

        System.out.println("Sort by:");
        System.out.println("\n1. Quantity");
        System.out.println("2. Price");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
        int sortField = getMenuChoice(1, 2, true);
        if (sortField == 0) {
            cancelOperation();
            return;
        }

        System.out.println("Sort order:");
        System.out.println("\n1. Ascending");
        System.out.println("2. Descending");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
        int sortOrderChoice = getMenuChoice(1, 2, true);
        if (sortOrderChoice == 0) {
            cancelOperation();
            return;
        }

        boolean ascending = (sortOrderChoice == 1);

        List<Item> sortedItems;
        if (sortField == 1) {
            sortedItems = inventory.sortByQuantity(ascending);
        } else {
            sortedItems = inventory.sortByPrice(ascending);
        }

        printItemTable(sortedItems, true);
    }

    private void displayLowStockItems() {
        printSectionHeader("Low Stock Items (Quantity <= 5)");

        List<Item> lowStockItems = inventory.getLowStockItems();

        if (lowStockItems.isEmpty()) {
            System.out.println("No low stock items!");
            return;
        }

        printItemTable(lowStockItems, true);
    }

    private void printItemTable(List<Item> items, boolean includeCategory) {
        if (includeCategory) {
            System.out.printf("%-15s %-25s %-12s %-14s %s\n", "ID", "Name", "Quantity", "Price", "Category");
            System.out.println("=".repeat(BANNER_WIDTH));
            for (Item item : items) {
                System.out.printf("%-15s %-25s %-12d %-14s %s\n",
                        item.getId(), item.getName(), item.getQuantity(), formatPrice(item.getPrice()), item.getCategory());
            }
        } else {
            System.out.printf("%-15s %-25s %-12s %s\n", "ID", "Name", "Quantity", "Price");
            System.out.println("=".repeat(BANNER_WIDTH));
            for (Item item : items) {
                System.out.printf("%-15s %-25s %-12d %s\n",
                        item.getId(), item.getName(), item.getQuantity(), formatPrice(item.getPrice()));
            }
        }
    }
}