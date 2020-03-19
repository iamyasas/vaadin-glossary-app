package com.iamyasas.glossaryapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementRepository extends JpaRepository<Element, Long> {

	List<Element> findByNameStartsWithIgnoreCase(String name);
}
