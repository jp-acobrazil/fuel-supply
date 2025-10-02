package com.acobrazil.fuelsupply.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acobrazil.fuelsupply.models.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}
