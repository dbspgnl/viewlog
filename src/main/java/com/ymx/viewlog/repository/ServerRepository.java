package com.ymx.viewlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ymx.viewlog.entity.Server;

public interface ServerRepository extends JpaRepository<Server, Integer> {
	
}
