package space.zeinab.demo.kafka.model;

public record Vehicle(
        String vehicleId,
        double latitude,
        double longitude,
        double speed,
        double fuelLevel,
        long timestamp
) {
}
