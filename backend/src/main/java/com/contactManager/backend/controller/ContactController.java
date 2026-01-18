package com.contactManager.backend.controller;

import com.contactManager.backend.dto.ContactDto;
import com.contactManager.backend.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor // auto generates constructor for final fields
@RestController // returns the json auto [ not html ]
@RequestMapping("/api/contacts") // comman base url for the all api's
@CrossOrigin(origins = "http://localhost:4200") // allow frontend to call the backend
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts(){
        return ResponseEntity.ok(contactService.getAllContacts());
    }
    // ResponseEntity : return body + status code + headers
    //Controller calls contactService.getAllContacts()
    //Service fetches from DB using repository
    //Converts List<Contact> → List<ContactDto>
    //Returns list to controller

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id){
        return ResponseEntity.ok(contactService.getContactById(id));
    }
    // extracts id = 5
    //Controller sends id to service
    //Service does findById
    //If found → convert to DTO
    //If not found → exception



    @PostMapping
    public ResponseEntity<ContactDto> createContact (@Valid @RequestBody ContactDto dto){
        return ResponseEntity.ok(contactService.createContact(dto));
    }
    // @Requestbody ContactDto dto [spring converts json -> contactDto auto]
    // @valid - > for not null, not blank, if fails returns 400 bad request
    // Controller receives DTO
    // calls service.createContact(dto)
    // service converts DTO → Entity
    // repo saves in DB
    // service converts saved entity → DTO
    // controller returns DTO JSON

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact (@PathVariable Long id, @RequestBody ContactDto dto){
        return ResponseEntity.ok(contactService.updateContact(id, dto));
    }
    // controller sends id + dto to service
    // service finds existing row in DB
    // updates fields
    // saves again
    // returns updated DTO

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id){
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}
