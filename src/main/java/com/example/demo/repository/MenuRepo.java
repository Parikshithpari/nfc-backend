package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.enums.Catogery;
import com.example.demo.enums.Men;
import com.example.demo.enums.Women;
import com.example.demo.model.Menu;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Long>
{
	List<Menu> findByActiveTrue();

	@Query("SELECT m FROM Menu m WHERE UPPER(m.category) = UPPER(:category)")
	List<Menu> findByCategory(@Param("category") String category);

	Optional<Menu> findByNameAndCategory(String name, Catogery category);

	Optional<Menu> findByName(String name);
	
	List<Menu> findAllByName(String name);

	List<Menu> findByCategoryAndMensCatogeries(Catogery category, Men mensCatogeries);
	List<Menu> findByCategoryAndWomenCatogeries(Catogery category, Women womenCatogeries);

}
