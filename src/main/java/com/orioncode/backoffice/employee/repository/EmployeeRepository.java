package com.orioncode.backoffice.employee.repository;

import com.orioncode.backoffice.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCode(String employeeCode);
    List<Employee> findByTeam(String team);
    List<Employee> findByPositionId(Long positionId);
}
