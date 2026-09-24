package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import com.example.demo.enums.AppointmentStatus;
import com.example.demo.model.Appointment;


@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long>
{
	List<Appointment> findByStatusIn(List<AppointmentStatus> statuses);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM appointment_services WHERE menu_id = :menuId", nativeQuery = true)
	void deleteFromJoinTableByMenuId(@Param("menuId") Long menuId);
}

