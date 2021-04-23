package org.ucsccaa.homepagebe.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private Integer memberid;
    private Member.Status status;
    private String name;
    private Boolean gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String wechat;
    private Integer branch;
    private Member.Address address;
    private Member.Degree degree;
    private Member.Career career;
    private Boolean search;

    enum Status {
        ACTIVE,
        PENDING,
        DEACTIVE,
        BRANCH,
        BOARD
    }
    private class Address {
        String street;
        String city;
        String country;
        String postal;
    }
    private class Degree {
        String studentId;
        String program;
        Integer endYear;
        String major1;
        String major2;
        String minor;
    }
    private class Career {
        Boolean status;
        String company;
        String position;
    }
}
