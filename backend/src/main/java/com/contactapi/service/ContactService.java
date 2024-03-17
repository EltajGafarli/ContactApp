package com.contactapi.service;

import com.contactapi.dto.ContactDto;
import com.contactapi.entity.Contact;
import com.contactapi.exception.ContactAlreadyExist;
import com.contactapi.exception.ContactNotFoundException;
import com.contactapi.exception.ImageUploadException;
import com.contactapi.mapper.ContactMapper;
import com.contactapi.repository.ContactRepository;
import com.contactapi.utils.Constants;
import com.contactapi.utils.ErrorMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
    private final BiFunction<String, MultipartFile, String> photoFunction = (name, image) -> {
        String filename = name + fileExtension.apply(image.getOriginalFilename());

        try {
            Path fileStorageLocation = Paths.get(Constants.PHOTO_DIRECTORY).toAbsolutePath().normalize();
            log.info(filename);
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new ImageUploadException("Unable to save image");
        }
    };

    public Page<ContactDto> getAllContacts(int page, int size) {
        Page<Contact> contacts = contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
        List<ContactDto> contactDtoList = contacts.stream().map(contactMapper::contactToContactDto).toList();
        return new PageImpl<>(contactDtoList, PageRequest.of(page, size), contactDtoList.size());
    }

    public ContactDto getContactById(long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException(ErrorMessages.CONTACT_NOT_FOUND));
        return contactMapper.contactToContactDto(contact);
    }

    public ContactDto createContact(ContactDto contactDto) {
        if(contactRepository.findByEmail(contactDto.getEmail()).isPresent()) {
            throw new ContactAlreadyExist(ErrorMessages.CONTACT_ALREADY_EXIST);
        }
        Contact contact = contactMapper.contactDtoToContact(contactDto);

        if(contact.getPhotoUrl() == null) {
            contact.setPhotoUrl(getDefaultPhotoUrl());
        }

        Contact savedContact = contactRepository.save(contact);
        return contactMapper.contactToContactDto(savedContact);
    }

    public void deleteContact(long id) {
        contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));
        contactRepository.deleteById(id);
    }

    public String uploadPhoto(long id, MultipartFile file) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));
        String photoUrl = photoFunction.apply(contact.getName(), file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private String getDefaultPhotoUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(Constants.PHOTO_DIRECTORY + "default.png")
                .toUriString();
    }
}
