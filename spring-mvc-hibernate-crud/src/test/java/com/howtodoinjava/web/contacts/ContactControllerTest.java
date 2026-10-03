package com.howtodoinjava.web.contacts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional   // each test rolls back, so every test starts with the two seeded contacts
class ContactControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ContactService contactService;

  @Autowired
  ContactDao contactDao;

  private Long idOf(String name) {
    return contactService.findAll().stream()
        .filter(c -> c.getName().equals(name)).findFirst().orElseThrow().getId();
  }

  @Test
  void homeRedirectsToList() throws Exception {
    mvc.perform(get("/"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/contacts"));
  }

  @Test
  void listShowsContactsSortedByName() throws Exception {
    mvc.perform(get("/contacts"))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/list"))
        .andExpect(model().attribute("contacts", hasSize(2)))
        .andExpect(content().string(containsString("lokesh@example.com")))
        .andExpect(content().string(containsString("/contacts/" + idOf("Lokesh") + "/edit")));
    assertThat(contactService.findAll()).extracting(Contact::getName).containsExactly("Alex", "Lokesh");
  }

  @Test
  void newFormHasEmptyContact() throws Exception {
    mvc.perform(get("/contacts/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/form"))
        .andExpect(model().attribute("contact", org.hamcrest.Matchers.hasProperty("id", nullValue())))
        .andExpect(content().string(containsString("New contact")))
        .andExpect(content().string(containsString("action=\"/contacts\"")))
        .andExpect(content().string(not(containsString("class=\"error\""))));
  }

  @Test
  void createValidContactRedirectsWithFlashMessage() throws Exception {
    mvc.perform(post("/contacts")
            .param("name", "Priya")
            .param("email", "priya@example.com")
            .param("phone", "555-0103")
            .param("city", "Pune"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/contacts"))
        .andExpect(flash().attribute("message", "Contact Priya saved"));
    assertThat(contactService.findAll()).extracting(Contact::getName)
        .containsExactly("Alex", "Lokesh", "Priya");
  }

  @Test
  void createInvalidContactShowsFormWithErrors() throws Exception {
    mvc.perform(post("/contacts")
            .param("name", "")
            .param("email", "priya-at-example")
            .param("phone", "call me")
            .param("city", "Pune"))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/form"))
        .andExpect(model().attributeHasFieldErrors("contact", "name", "email", "phone"))
        .andExpect(model().errorCount(3))
        .andExpect(content().string(containsString("Name is required")))
        .andExpect(content().string(containsString("Email is not valid")))
        .andExpect(content().string(containsString("Phone may contain digits, spaces, + and - only")))
        .andExpect(content().string(containsString("value=\"Pune\"")));
    assertThat(contactService.findAll()).hasSize(2);
  }

  @Test
  void createWithUsedEmailShowsFieldError() throws Exception {
    mvc.perform(post("/contacts")
            .param("name", "Lokesh G")
            .param("email", "lokesh@example.com"))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/form"))
        .andExpect(model().attributeHasFieldErrorCode("contact", "email", "duplicate"))
        .andExpect(content().string(containsString("Email is already used")));
    assertThat(contactService.findAll()).hasSize(2);
  }

  @Test
  void editFormIsFilledWithTheContact() throws Exception {
    Long id = idOf("Lokesh");
    mvc.perform(get("/contacts/{id}/edit", id))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/form"))
        .andExpect(model().attribute("contact", org.hamcrest.Matchers.hasProperty("city", org.hamcrest.Matchers.is("Delhi"))))
        .andExpect(content().string(containsString("Edit contact")))
        .andExpect(content().string(containsString("action=\"/contacts/" + id + "\"")))
        .andExpect(content().string(containsString("value=\"lokesh@example.com\"")));
  }

  @Test
  void updateValidContactRedirects() throws Exception {
    Long id = idOf("Lokesh");
    mvc.perform(post("/contacts/{id}", id)
            .param("name", "Lokesh")
            .param("email", "lokesh@example.com")
            .param("phone", "555-0101")
            .param("city", "Mumbai"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/contacts"))
        .andExpect(flash().attribute("message", "Contact Lokesh updated"));
    assertThat(contactService.findById(id).getCity()).isEqualTo("Mumbai");
  }

  @Test
  void updateInvalidContactKeepsTheEditForm() throws Exception {
    Long id = idOf("Lokesh");
    mvc.perform(post("/contacts/{id}", id)
            .param("name", "")
            .param("email", "lokesh@example.com"))
        .andExpect(status().isOk())
        .andExpect(view().name("contacts/form"))
        .andExpect(model().attributeHasFieldErrors("contact", "name"))
        .andExpect(content().string(containsString("Edit contact")))
        .andExpect(content().string(containsString("action=\"/contacts/" + id + "\"")));
    assertThat(contactService.findById(id).getName()).isEqualTo("Lokesh");
  }

  @Test
  void updateWithEmailOfAnotherContactShowsFieldError() throws Exception {
    Long id = idOf("Lokesh");
    mvc.perform(post("/contacts/{id}", id)
            .param("name", "Lokesh")
            .param("email", "alex@example.com"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrorCode("contact", "email", "duplicate"));
  }

  @Test
  void deleteRemovesTheContact() throws Exception {
    Long id = idOf("Alex");
    mvc.perform(post("/contacts/{id}/delete", id))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/contacts"))
        .andExpect(flash().attribute("message", "Contact deleted"));
    assertThat(contactDao.findById(id)).isEmpty();
    assertThat(contactService.findAll()).extracting(Contact::getName).containsExactly("Lokesh");
  }

  @Test
  void flashMessageIsShownOnTheListAfterRedirect() throws Exception {
    mvc.perform(get("/contacts").flashAttr("message", "Contact Priya saved"))
        .andExpect(content().string(containsString("Contact Priya saved")));
    mvc.perform(get("/contacts"))
        .andExpect(content().string(not(containsString("Contact Priya saved"))));
  }

  @Test
  void repositoryTranslatesTheUniqueKeyViolation() {
    org.assertj.core.api.Assertions.assertThatThrownBy(
            () -> contactDao.save(new Contact("Copy", "alex@example.com", null, null)))
        .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
  }

  @Test
  void withoutBindingResultInvalidFormGives400() throws Exception {
    var result = mvc.perform(post("/test/no-binding-result").param("name", "").param("email", "x"))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResolvedException())
        .isInstanceOf(org.springframework.web.bind.MethodArgumentNotValidException.class);
  }

  @Test
  void unknownIdReturns404() throws Exception {
    mvc.perform(get("/contacts/{id}/edit", 999)).andExpect(status().isNotFound());
    mvc.perform(post("/contacts/{id}/delete", 999)).andExpect(status().isNotFound());
  }
}
