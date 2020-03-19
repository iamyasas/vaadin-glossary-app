package com.iamyasas.glossaryapp.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DataSource {

	@Id
	@GeneratedValue
	private Long id;
	private String source;
	
	//@ManyToOne(mappedBy = "dataSources", fetch = FetchType.LAZY)
	private Element element;
	
	public DataSource() {
		
	}

	public DataSource(String source) {
		this.source = source;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
