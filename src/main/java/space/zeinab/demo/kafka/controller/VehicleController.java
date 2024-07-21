package space.zeinab.demo.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.zeinab.demo.kafka.model.Vehicle;
import space.zeinab.demo.kafka.service.VehicleService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles(
            @RequestParam(value = "query_other_hosts", required = false) String queryOtherHosts
    ) {
        if (!StringUtils.hasText(queryOtherHosts)) {
            queryOtherHosts = "true";
        }

        var allVehicles = vehicleService.getAllVehicles(queryOtherHosts);
        return ResponseEntity.ok(allVehicles);
    }

    @GetMapping("/{vehicle_id}")
    public ResponseEntity<Vehicle> getVehicle(
            @PathVariable(value = "vehicle_id") String vehicleId,
            @RequestParam(value = "query_other_hosts", required = false) String queryOtherHosts
    ) {
        if (!StringUtils.hasText(queryOtherHosts)) {
            queryOtherHosts = "true";
        }
        var vehicle = vehicleService.getVehicle(vehicleId, queryOtherHosts);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
