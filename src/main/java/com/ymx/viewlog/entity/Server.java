package com.ymx.viewlog.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Server {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int index;
	private int status;
	private String name;
	private String logPath;
	private Timestamp date;
	
}
