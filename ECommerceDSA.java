import java.util.*;

// -------------------- PRODUCT CLASS --------------------
class Product {
    int id;
    String name;
    double price;
    String category;

    Product(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

// -------------------- MAIN APPLICATION --------------------
public class ECommerceDSA {

    // HashMap for O(1) login lookup
    static HashMap<String, String> users = new HashMap<>();

    // Product list
    static ArrayList<Product> productList = new ArrayList<>();

    // Cart using LinkedHashMap (maintains insertion order)
    static LinkedHashMap<Integer, Integer> cart = new LinkedHashMap<>();

    // Stack for Order History (LIFO)
    static Stack<String> orderHistory = new Stack<>();

    static Scanner sc = new Scanner(System.in);
    static String currentUser = null;

    public static void main(String[] args) {

        loadProducts();

        while (true) {
            if (currentUser == null) {
                authenticationMenu();
            } else {
                storeMenu();
            }
        }
    }

    // -------------------- LOAD PRODUCTS --------------------
    static void loadProducts() {
        productList.add(new Product(1, "iPhone", 70000, "Electronics"));
        productList.add(new Product(2, "Laptop", 80000, "Electronics"));
        productList.add(new Product(3, "Shoes", 4000, "Fashion"));
        productList.add(new Product(4, "T-Shirt", 1000, "Fashion"));
        productList.add(new Product(5, "Headphones", 3000, "Accessories"));
    }

    // -------------------- AUTHENTICATION --------------------
    static void authenticationMenu() {
        System.out.println("\n1. Sign Up");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> signup();
            case 2 -> login();
            case 3 -> System.exit(0);
        }
    }

    static void signup() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        users.put(username, password);
        System.out.println("Signup Successful!");
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            System.out.println("Login Successful! Welcome " + username);
        } else {
            System.out.println("Invalid Credentials!");
        }
    }

    // -------------------- STORE MENU --------------------
    static void storeMenu() {
        System.out.println("\n1. View All Products (Bubble Sorted by Price)");
        System.out.println("2. Filter by Category");
        System.out.println("3. Add to Cart");
        System.out.println("4. View Cart");
        System.out.println("5. View Order History (LIFO)");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                bubbleSortByPrice();
                displayProducts(productList);
            }
            case 2 -> filterByCategory();
            case 3 -> addToCart();
            case 4 -> viewCart();
            case 5 -> viewOrderHistory();
            case 6 -> logout();
        }
    }

    // -------------------- BUBBLE SORT --------------------
    static void bubbleSortByPrice() {
        int n = productList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (productList.get(j).price > productList.get(j + 1).price) {

                    Product temp = productList.get(j);
                    productList.set(j, productList.get(j + 1));
                    productList.set(j + 1, temp);
                }
            }
        }
    }

    // -------------------- DISPLAY PRODUCTS --------------------
    static void displayProducts(List<Product> list) {
        System.out.println("\n--- Product List ---");
        for (Product p : list) {
            System.out.println(p.id + " | " + p.name + " | Rs." + p.price + " | " + p.category);
        }
    }

    // -------------------- FILTER PRODUCTS --------------------
    static void filterByCategory() {
        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        ArrayList<Product> filtered = new ArrayList<>();

        for (Product p : productList) {
            if (p.category.equalsIgnoreCase(category)) {
                filtered.add(p);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No products found!");
        } else {
            displayProducts(filtered);
        }
    }

    // -------------------- ADD TO CART --------------------
    static void addToCart() {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean found = false;

        for (Product p : productList) {
            if (p.id == id) {
                found = true;
                cart.put(id, cart.getOrDefault(id, 0) + 1);
                System.out.println("Product Added to Cart!");
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid Product ID!");
        }
    }

    // -------------------- VIEW CART --------------------
    static void viewCart() {

        if (cart.isEmpty()) {
            System.out.println("Cart is Empty!");
            return;
        }

        double total = 0;
        System.out.println("\n--- Cart Items ---");

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product p = findProductById(productId);

            double subtotal = p.price * quantity;
            total += subtotal;

            System.out.println(p.name + " x" + quantity + " = Rs." + subtotal);
        }

        System.out.println("Total Amount: Rs." + total);

        System.out.println("1. Remove Item");
        System.out.println("2. Proceed to Payment");
        System.out.println("3. Back");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> removeItem();
            case 2 -> processPayment(total);
        }
    }

    // -------------------- REMOVE ITEM --------------------
    static void removeItem() {
        System.out.print("Enter Product ID to remove: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (cart.containsKey(id)) {
            cart.remove(id);
            System.out.println("Item Removed!");
        } else {
            System.out.println("Product not in cart!");
        }
    }

    // -------------------- PAYMENT --------------------
    static void processPayment(double total) {

        System.out.print("Enter Card Number: ");
        String card = sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter CVV: ");
        String cvv = sc.nextLine();

        if (!card.isEmpty() && !name.isEmpty() && !cvv.isEmpty()) {

            orderHistory.push("User: " + currentUser +
                              " | Amount: Rs." + total +
                              " | Items: " + cart.size());

            System.out.println("Payment Successful! Rs." + total + " Paid.");
            cart.clear();

        } else {
            System.out.println("Payment Failed!");
        }
    }

    // -------------------- VIEW ORDER HISTORY --------------------
    static void viewOrderHistory() {

        if (orderHistory.isEmpty()) {
            System.out.println("No orders yet!");
            return;
        }

        System.out.println("\n--- Order History (Most Recent First - LIFO) ---");

        Stack<String> temp = new Stack<>();
        temp.addAll(orderHistory);

        while (!temp.isEmpty()) {
            System.out.println(temp.pop());
        }
    }

    // -------------------- FIND PRODUCT --------------------
    static Product findProductById(int id) {
        for (Product p : productList) {
            if (p.id == id)
                return p;
        }
        return null;
    }

    // -------------------- LOGOUT --------------------
    static void logout() {
        currentUser = null;
        System.out.println("Logged Out!");
    }
}
