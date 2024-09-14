package com.howtodoinjava.demo.web;

import com.howtodoinjava.demo.exception.ResourceNotFoundException;
import com.howtodoinjava.demo.model.PersonVO;
import com.howtodoinjava.demo.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping
  public ResponseEntity<Page<PersonVO>> getAllPersons(
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String lastName,
      @RequestParam(required = false) String email,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,asc") String[] sort) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(parseSortParameters(sort)));
    Page<PersonVO> personsPage = personService.getAllPersons(firstName, lastName, email, pageable);
    return ResponseEntity.ok(personsPage);
  }

  private Sort.Order parseSortParameters(String[] sortParams) {

    String property = sortParams[0];
    Sort.Direction direction = sortParams.length > 1
        && sortParams[1].equalsIgnoreCase("desc")
        ? Sort.Direction.DESC : Sort.Direction.ASC;
    return new Sort.Order(direction, property);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonVO> getPersonById(@PathVariable Long id) {

    return personService.getPersonById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PersonVO> addPerson(@Valid @RequestBody PersonVO person) {

    PersonVO createdPerson = personService.addPerson(person);
    return ResponseEntity.ok(createdPerson);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonVO> updatePerson(
      @PathVariable Long id, @Valid @RequestBody PersonVO updatedPerson) {

    try {
      PersonVO person = personService.updatePerson(id, updatedPerson);
      return ResponseEntity.ok(person);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePerson(@PathVariable Long id) {

    personService.deletePerson(id);
    return ResponseEntity.noContent().build();
  }
}
