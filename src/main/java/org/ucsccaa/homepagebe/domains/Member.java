package org.ucsccaa.homepagebe.domains;

import javax.persistence.*;

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
    @OneToOne
    @JoinColumn(name = "member_uid", referencedColumnName = "uid")
    private User user;
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
    class Address {
        String street;
        String city;
        String country;
        String postal;
    }
    class Degree {
        String studentId;
        String program;
        Integer endYear;
        String major1;
        String major2;
        String minor;
    }
    class Career {
        Boolean status;
        String company;
        String position;
    }
}
