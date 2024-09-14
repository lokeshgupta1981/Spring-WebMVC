package com.howtodoinjava.demo;

import com.howtodoinjava.demo.data.PersonRepository;
import com.howtodoinjava.demo.data.entity.PersonEntity;
import com.howtodoinjava.demo.model.PersonVO;
import com.howtodoinjava.demo.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

  @InjectMocks
  private PersonService personService;

  @Mock
  private PersonRepository personRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetAllPersonsWithPagingAndFiltering() {
    Pageable pageable = PageRequest.of(0, 10);
    List<PersonEntity> persons = List.of(
        new PersonEntity(1L, "John", "Doe", "john@example.com"),
        new PersonEntity(2L, "Jane", "Doe", "jane@example.com")
    );

    Page<PersonEntity> personPage = new PageImpl<>(persons, pageable, persons.size());

    when(personRepository.findByFilters("John", "Doe", null, pageable)).thenReturn(personPage);

    Page<PersonVO> result = personService.getAllPersons("John", "Doe", null, pageable);

    assertEquals(2, result.getTotalElements());
    assertEquals("John", result.getContent().getFirst().getFirstName());
  }
}

