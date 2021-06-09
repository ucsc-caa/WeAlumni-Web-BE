package org.ucsccaa.homepagebe.domains;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucsccaa.homepagebe.utils.Encryptable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Encryptable
public class User {
    @Id
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true, nullable = false)
    private Integer uid;
    private Boolean emailVerified;
}
