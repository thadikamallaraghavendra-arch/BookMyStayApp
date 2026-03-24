import java.util.*;

/**
 * UseCase9ErrorHandlingValidation
 *
 * Demonstrates validation and error handling in booking flow.
 * Ensures invalid inputs are caught early and handled gracefully.
 *
 * @author YourName
 * @version 9.0
 */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v9.0      ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0);

        // Validator
        BookingValidator validator = new BookingValidator(inventory);

        // Test booking inputs
        List<Reservation> requests = Arrays.asList(
                new Reservation("Guest1", "Single Room"),
                new Reservation("Guest2", "Double Room"), // no availability
                new Reservation("Guest3", "Suite Room")   // invalid type
        );

        // Process requests safely
        for (Reservation request : requests) {
            try {
                System.out.println("\nProcessing request: "
                        + request.getGuestName() + " -> " + request.getRoomType());

                validator.validate(request);

                // Simulate safe allocation after validation
                inventory.decrement(request.getRoomType());

                System.out.println("Booking SUCCESS for " + request.getGuestName());

            } catch (InvalidBookingException e) {
                System.out.println("Booking FAILED: " + e.getMessage());
            }
        }

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();

        System.out.println("\nApplication terminating...");
    }
}

/**
 * Reservation entity
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Custom Exception for invalid booking scenarios
 */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * BookingValidator
 *
 * Performs validation before booking is processed.
 */
class BookingValidator {

    private RoomInventory inventory;

    public BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Validates booking request
     */
    public void validate(Reservation request) throws InvalidBookingException {

        String roomType = request.getRoomType();

        // Validate room type existence
        if (!inventory.exists(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No availability for: " + roomType);
        }
    }
}

/**
 * RoomInventory with validation-safe operations
 */
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public boolean exists(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    /**
     * Safe decrement with guard
     */
    public void decrement(String type) {
        int current = getAvailability(type);

        if (current <= 0) {
            throw new IllegalStateException("Invalid inventory state for: " + type);
        }

        availability.put(type, current - 1);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + " -> Available: " + e.getValue());
        }
    }
}