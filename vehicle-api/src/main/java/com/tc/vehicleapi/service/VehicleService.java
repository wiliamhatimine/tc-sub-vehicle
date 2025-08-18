package com.tc.vehicleapi.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tc.vehicleapi.dto.VehicleDto;
import com.tc.vehicleapi.exception.VehicleNotFoundException;
import com.tc.vehicleapi.model.Status;
import com.tc.vehicleapi.model.Vehicle;
import com.tc.vehicleapi.repository.VehicleRepository;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setMarca(dto.marca());
        vehicle.setModelo(dto.modelo());
        vehicle.setAno(dto.ano());
        vehicle.setCor(dto.cor());
        vehicle.setPreco(dto.preco());
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle updateVehicle(Long id, VehicleDto dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
        
        vehicle.setMarca(dto.marca());
        vehicle.setModelo(dto.modelo());
        vehicle.setAno(dto.ano());
        vehicle.setCor(dto.cor());
        vehicle.setPreco(dto.preco());
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehiclesByStatus(Status status) {
        return vehicleRepository.findByStatusOrderByPrecoAsc(status);
    }

    public Vehicle buyVehicle(Long id, String buyerEmail) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
        if (vehicle.getStatus() == Status.VENDIDO) {
            throw new IllegalStateException("Vehicle is already sold.");
        }
        vehicle.setStatus(Status.VENDIDO);
        vehicle.setCompradorId(buyerEmail);
        return vehicleRepository.save(vehicle);
    }
}