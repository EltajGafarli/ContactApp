package com.contactapi.dto;

import com.contactapi.utils.ContactValidationMessage;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDto {
    private long id;

    @NotNull(message = ContactValidationMessage.NAME_NOT_NULL)
    @NotEmpty(message = ContactValidationMessage.NAME_NOT_EMPTY)
    @NotBlank(message = ContactValidationMessage.NAME_NOT_BLANK)
    private String name;

    @Email(message = ContactValidationMessage.EMAIL_NOT_VALID)
    private String email;

    @NotNull(message = ContactValidationMessage.TITLE_NOT_NULL)
    @NotEmpty(message = ContactValidationMessage.TITLE_NOT_EMPTY)
    @NotBlank(message = ContactValidationMessage.TITLE_NOT_BLANK)
    private String title;

    @NotNull(message = ContactValidationMessage.PHONE_NOT_NULL)
    @NotEmpty(message = ContactValidationMessage.PHONE_NOT_EMPTY)
    @NotBlank(message = ContactValidationMessage.PHONE_NOT_BLANK)
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = ContactValidationMessage.PHONE_NOT_VALID)
    private String phone;

    @NotNull(message = ContactValidationMessage.ADDRESS_NOT_NULL)
    @NotEmpty(message = ContactValidationMessage.ADDRESS_NOT_EMPTY)
    @NotBlank(message = ContactValidationMessage.ADDRESS_NOT_BLANK)
    private String address;

    @NotNull(message = ContactValidationMessage.STATUS_NOT_NULL)
    @NotEmpty(message = ContactValidationMessage.STATUS_NOT_EMPTY)
    @NotBlank(message = ContactValidationMessage.STATUS_NOT_BLANK)
    private String status;

//    @NotNull(message = ContactValidationMessage.PHOTO_URL_NOT_NULL)
//    @NotEmpty(message = ContactValidationMessage.PHOTO_URL_NOT_EMPTY)
//    @NotBlank(message = ContactValidationMessage.PHOTO_URL_NOT_BLANK)
    private String photoUrl;
}
