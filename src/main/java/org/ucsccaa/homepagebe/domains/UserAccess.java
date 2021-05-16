package org.ucsccaa.homepagebe.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccess {
    @Id
    private String email;
    private Boolean isAdmin;
}
