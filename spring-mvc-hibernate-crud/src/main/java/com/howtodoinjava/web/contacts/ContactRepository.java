package com.howtodoinjava.web.contacts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA alternative to ContactDao. Spring generates the implementation.
 * The service in this project uses ContactDao; ContactRepositoryTest shows this interface at work.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {

  List<Contact> findAllByOrderByNameAsc();

  boolean existsByEmail(String email);
}
