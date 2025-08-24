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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(
        summary = "Listar veículos à venda",
        description = "Retorna veículos disponíveis para venda, ordenados por preço do mais barato para o mais caro."
    )
    @ApiResponse(responseCode = "200", description = "Lista de veículos disponíveis")    
    @GetMapping("/forsale")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getVehiclesByStatus(Status.DISPONIVEL);
    }

    @Operation(
        summary = "Listar veículos vendidos",
        description = "Retorna veículos já vendidos, ordenados por preço do mais barato para o mais caro."
    )
    @ApiResponse(responseCode = "200", description = "Lista de veículos vendidos")    
    @GetMapping("/sold")
    public List<Vehicle> getSoldVehicles() {
        return vehicleService.getVehiclesByStatus(Status.VENDIDO);
    }

    @Operation(
        summary = "Cadastrar veículo",
        description = "Cadastra um novo veículo para venda na plataforma. "
                    + "Campos obrigatórios: marca, modelo, ano, cor e preço."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleDto dto) {
        return new ResponseEntity<>(vehicleService.createVehicle(dto), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Editar veículo",
        description = "Atualiza os dados de um veículo existente pelo seu ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @Operation(
        summary = "Comprar veículo",
        description = "Permite que um usuário cadastrado realize a compra de um veículo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário não autorizado ou dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })    
    @PostMapping("/{id}/buy")
    public Vehicle buyVehicle(@PathVariable Long id, @AuthenticationPrincipal String userEmail) {
        return vehicleService.buyVehicle(id, userEmail);
    }
}