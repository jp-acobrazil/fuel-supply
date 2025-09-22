package com.acobrazil.fuelsupply.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acobrazil.fuelsupply.models.Driver;

@Repository
public interface DriverRepository extends CrudRepository<Driver, Integer> {

}
