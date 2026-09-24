package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.AppointmentStatus;
import com.example.demo.model.ManualAppointment;

@Repository
public interface ManualAppointmentRepo extends JpaRepository<ManualAppointment, Long>
{

	List<ManualAppointment> findByStatusIn(List<AppointmentStatus> statuses);

}
