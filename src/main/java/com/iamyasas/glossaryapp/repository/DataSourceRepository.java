package com.iamyasas.glossaryapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceRepository extends JpaRepository<DataSource, Long>{
	
	//long count();
}
