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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_uid", referencedColumnName = "uid")
    private User user;

    //@Enumerated(EnumType.STRING)
    //private Status status;
    private String name;
    private Boolean gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String wechat;
    private Integer branch;

    @Embedded
    private Member.Address address;
    @Embedded
    private Member.Degree degree;
    @Embedded
    private Member.Career career;
    private Boolean search;

    enum Status {
        ACTIVE,
        PENDING,
        DEACTIVE,
        BRANCH,
        BOARD
    }

    @Embeddable
    class Address {
        private String street;
        private String city;
        private String country;
        private String postal;
    }

    @Embeddable
    class Degree {
        private String studentId;
        private String program;
        private Integer endYear;
        private String major1;
        private String major2;
        private String minor;
    }

    @Embeddable
    class Career {
        private Boolean status;
        private String company;
        private String position;
    }
}
