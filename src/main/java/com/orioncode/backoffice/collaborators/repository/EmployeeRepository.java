package com.orioncode.backoffice.collaborators.repository;

import com.orioncode.backoffice.collaborators.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findByTeam(String team);
    List<Employee> findByPositionId(String positionId);
}
