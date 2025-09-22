package com.acobrazil.fuelsupply.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acobrazil.fuelsupply.models.Vehicle;

import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

    Optional<Vehicle> findByPlate(String plate);
}
