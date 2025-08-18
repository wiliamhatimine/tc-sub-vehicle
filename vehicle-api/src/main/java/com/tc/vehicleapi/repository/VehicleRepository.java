package com.tc.vehicleapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tc.vehicleapi.model.Status;
import com.tc.vehicleapi.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByStatusOrderByPrecoAsc(Status status);
}