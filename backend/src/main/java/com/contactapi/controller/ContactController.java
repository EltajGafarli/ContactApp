package com.contactapi.controller;

import com.contactapi.dto.ContactDto;
import com.contactapi.service.ContactService;
import com.contactapi.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestBody @Valid ContactDto contactDto) {
        return ResponseEntity
                .created(
                        URI.create("/contacts/" + contactDto.getId())
                )
                .body(
                        contactService.createContact(contactDto)
                );
    }

    @GetMapping
    public ResponseEntity<Page<ContactDto>> getContacts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contactService.getAllContacts(page, size));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable long id) {
        return ResponseEntity
                .ok()
                .body(contactService.getContactById(id));
    }

    @PutMapping(path = "/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(Constants.PHOTO_DIRECTORY + filename));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable long id) {
        contactService.deleteContact(id);
        return ResponseEntity
                .ok()
                .body("Contact Deleted Successfully");
    }


}
