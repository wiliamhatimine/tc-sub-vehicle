package com.tc.vehicleapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleDto(@NotBlank String marca, @NotBlank String modelo, @NotNull @Min(1900) int ano,
		@NotBlank String cor, @NotNull @Positive double preco) {
}