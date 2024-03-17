package com.contactapi.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "contacts", uniqueConstraints = {
        @UniqueConstraint(
                name = "EMAIL_ID_UK",
                columnNames = {"email", "id"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;
    private String name;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.name,
                this.email,
                this.title,
                this.phone,
                this.address,
                this.status,
                this.phone,
                this.address,
                this.photoUrl
        );
    }

    @Override
    public boolean equals(Object object) {
        Contact contact = (Contact) object;
        return Objects.deepEquals(this.id, contact.id)
                && Objects.deepEquals(this.name, contact.name)
                && Objects.deepEquals(this.email, contact.email)
                && Objects.deepEquals(this.title, contact.title)
                && Objects.deepEquals(this.phone, contact.phone)
                && Objects.deepEquals(this.address, contact.address)
                && Objects.deepEquals(this.status, contact.status)
                && Objects.deepEquals(this.photoUrl, contact.photoUrl);
    }
}
