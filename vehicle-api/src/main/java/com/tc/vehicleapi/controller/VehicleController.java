package com.tc.vehicleapi.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tc.vehicleapi.dto.VehicleDto;
import com.tc.vehicleapi.model.Status;
import com.tc.vehicleapi.model.Vehicle;
import com.tc.vehicleapi.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/forsale")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getVehiclesByStatus(Status.DISPONIVEL);
    }

    @GetMapping("/sold")
    public List<Vehicle> getSoldVehicles() {
        return vehicleService.getVehiclesByStatus(Status.VENDIDO);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleDto dto) {
        return new ResponseEntity<>(vehicleService.createVehicle(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @PostMapping("/{id}/buy")
    public Vehicle buyVehicle(@PathVariable Long id, @AuthenticationPrincipal String userEmail) {
        return vehicleService.buyVehicle(id, userEmail);
    }
}