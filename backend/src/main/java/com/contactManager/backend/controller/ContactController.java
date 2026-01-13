package com.contactManager.backend.controller;

import com.contactManager.backend.dto.ContactDto;
import com.contactManager.backend.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts(){
        return ResponseEntity.ok(contactService.getAllContacts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id){
        return ResponseEntity.ok(contactService.getContactById(id));
    }
    @PostMapping
    public ResponseEntity<ContactDto> createContact (@Valid @RequestBody ContactDto dto){
        return ResponseEntity.ok(contactService.createContact(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact (@PathVariable Long id, @RequestBody ContactDto dto){
        return ResponseEntity.ok(contactService.updateContact(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id){
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}
