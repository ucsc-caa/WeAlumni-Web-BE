package org.ucsccaa.homepagebe.domains;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(unique = true)
    private String email;
    private String password;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false,columnDefinition="int not null UNIQUE key auto_increment")
    private Integer uid;
    private Boolean emailVerfied;
}
