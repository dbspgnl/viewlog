package com.ymx.viewlog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ymx.viewlog.entity.Server;

public interface ServerRepository extends JpaRepository<Server, Integer> {

	Optional<Server> findByStartPath(String startPath);
	
}
