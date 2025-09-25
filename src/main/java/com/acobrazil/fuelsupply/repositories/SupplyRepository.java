package com.acobrazil.fuelsupply.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acobrazil.fuelsupply.models.Supply;

@Repository
public interface SupplyRepository extends CrudRepository<Supply, Long> {

	List<Supply> findByDriverId(Long driverId);

}
