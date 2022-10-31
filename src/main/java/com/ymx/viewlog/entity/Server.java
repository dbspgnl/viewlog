package com.ymx.viewlog.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity @Data
public class Server {
	
	@Id
	private int id;
	private String name;
	
}
