public class Item {
    

    public static final int MAX_QUANTITY = 1_000_000;
    public static final double MAX_PRICE = 1_000_000.0;

    private String id;
    private String name;
    private int quantity;
    private double price;
    private String category;

    public Item(String id, String name, int quantity, double price, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = 0;
        this.price = 0.0;
        setQuantity(quantity);
        setPrice(price);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0 && quantity <= MAX_QUANTITY) {
            this.quantity = quantity;
        }
    }

    public void setPrice(double price) {
        if (Double.isFinite(price) && price >= 0 && price <= MAX_PRICE) {
            this.price = price;
        }
    }

    @Override
    public String toString() {
        return String.format("%-15s %-25s %-12d %-12.2f %s",
                id, name, quantity, price, category);
    }
}
