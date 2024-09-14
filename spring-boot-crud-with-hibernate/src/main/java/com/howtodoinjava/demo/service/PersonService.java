package com.howtodoinjava.demo.service;

import com.howtodoinjava.demo.data.PersonRepository;
import com.howtodoinjava.demo.data.entity.PersonEntity;
import com.howtodoinjava.demo.exception.ResourceNotFoundException;
import com.howtodoinjava.demo.model.PersonVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  // Fetch all persons
  public Page<PersonVO> getAllPersons(
      String firstName, String lastName, String email, Pageable pageable) {

    return personRepository.findByFilters(firstName, lastName, email, pageable)
        .map(this::convertEntityToVo);
  }

  // Fetch person by id with caching
  @Cacheable(cacheNames = "persons", key = "#id")
  public Optional<PersonVO> getPersonById(Long id) {

    return personRepository.findById(id).map(this::convertEntityToVo);
  }

  // Add a new person
  public PersonVO addPerson(PersonVO personVo) {

    PersonEntity entity = convertVoToEntity(personVo);
    entity = personRepository.save(entity);
    return convertEntityToVo(entity);
  }

  // Update person by id
  @Transactional
  public PersonVO updatePerson(Long id, PersonVO updatedPerson) {

    return personRepository.findById(id)
        .map(entity -> {
          BeanUtils.copyProperties(updatedPerson, entity, "id");
          entity = personRepository.save(entity);  // Save and reassign for clarity
          return convertEntityToVo(entity);
        })
        .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
  }

  // Delete person by id
  public void deletePerson(Long id) {

    if (!personRepository.existsById(id)) {
      throw new ResourceNotFoundException("Person not found with id: " + id);
    }
    personRepository.deleteById(id);
  }

  private PersonVO convertEntityToVo(PersonEntity entity) {

    PersonVO vo = new PersonVO();
    BeanUtils.copyProperties(entity, vo);
    return vo;
  }

  private PersonEntity convertVoToEntity(PersonVO vo) {

    PersonEntity entity = new PersonEntity();
    BeanUtils.copyProperties(vo, entity);
    return entity;
  }
}
