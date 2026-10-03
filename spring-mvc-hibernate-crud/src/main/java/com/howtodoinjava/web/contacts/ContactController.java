package com.howtodoinjava.web.contacts;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contacts")
public class ContactController {

  private final ContactService contactService;

  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("contacts", contactService.findAll());
    return "contacts/list";
  }

  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("contact", new Contact());
    return "contacts/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
                       RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "contacts/form";
    }
    try {
      contactService.create(contact);
    } catch (DuplicateEmailException e) {
      result.rejectValue("email", "duplicate", "Email is already used");
      return "contacts/form";
    }
    redirect.addFlashAttribute("message", "Contact " + contact.getName() + " saved");
    return "redirect:/contacts";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("contact", contactService.findById(id));
    return "contacts/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("contact") Contact contact,
                       BindingResult result, RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "contacts/form";
    }
    try {
      contactService.update(id, contact);
    } catch (DuplicateEmailException e) {
      result.rejectValue("email", "duplicate", "Email is already used");
      return "contacts/form";
    }
    redirect.addFlashAttribute("message", "Contact " + contact.getName() + " updated");
    return "redirect:/contacts";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirect) {
    contactService.delete(id);
    redirect.addFlashAttribute("message", "Contact deleted");
    return "redirect:/contacts";
  }
}
