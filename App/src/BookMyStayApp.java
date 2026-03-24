import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized room inventory management using HashMap.
 * It replaces scattered availability variables with a single source of truth.
 *
 * @author YourName
 * @version 3.1
 */
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v3.1      ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types with availability
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display current inventory
        System.out.println("\nInitial Room Availability:");
        inventory.displayInventory();

        // Update availability (simulate booking/cancellation)
        System.out.println("\nUpdating Inventory...");
        inventory.updateAvailability("Single Room", -1); // booking
        inventory.updateAvailability("Suite Room", +1);  // cancellation

        // Display updated inventory
        System.out.println("\nUpdated Room Availability:");
        inventory.displayInventory();

        System.out.println("\nApplication terminating...");
    }
}

/**
 * RoomInventory
 *
 * This class encapsulates all inventory-related operations.
 * It acts as a single source of truth for room availability.
 */
class RoomInventory {

    // Centralized storage
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the HashMap.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    /**
     * Adds a new room type with initial availability.
     */
    public void addRoomType(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    /**
     * Retrieves availability for a given room type.
     */
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    /**
     * Updates availability in a controlled manner.
     */
    public void updateAvailability(String roomType, int change) {
        int current = getAvailability(roomType);
        int updated = current + change;

        if (updated < 0) {
            System.out.println("Cannot reduce below zero for: " + roomType);
            return;
        }

        roomAvailability.put(roomType, updated);
    }

    /**
     * Displays the entire inventory.
     */
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}