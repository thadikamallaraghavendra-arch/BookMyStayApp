import java.util.*;

/**
 * UseCase7AddOnServiceSelection
 *
 * Demonstrates how add-on services can be attached to reservations
 * without modifying core booking or inventory logic.
 *
 * @author YourName
 * @version 7.0
 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v7.0      ");
        System.out.println("====================================");

        // Sample reservation IDs (already confirmed bookings)
        String res1 = "RES-101";
        String res2 = "RES-102";

        // Initialize service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects services
        serviceManager.addService(res1, new AddOnService("Breakfast", 500));
        serviceManager.addService(res1, new AddOnService("Airport Pickup", 1200));

        serviceManager.addService(res2, new AddOnService("Extra Bed", 800));

        // Display services and total cost
        System.out.println("\nServices for " + res1 + ":");
        serviceManager.displayServices(res1);
        System.out.println("Total Add-On Cost: ₹" + serviceManager.calculateTotalCost(res1));

        System.out.println("\nServices for " + res2 + ":");
        serviceManager.displayServices(res2);
        System.out.println("Total Add-On Cost: ₹" + serviceManager.calculateTotalCost(res2));

        System.out.println("\nApplication terminating...");
    }
}

/**
 * AddOnService
 *
 * Represents an optional service that can be added to a reservation.
 */
class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

/**
 * AddOnServiceManager
 *
 * Manages mapping between reservation IDs and selected services.
 */
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    /**
     * Add a service to a reservation
     */
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: "
                + service.getServiceName()
                + " to "
                + reservationId);
    }

    /**
     * Display all services for a reservation
     */
    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getCost() + ")");
        }
    }

    /**
     * Calculate total cost of services for a reservation
     */
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}