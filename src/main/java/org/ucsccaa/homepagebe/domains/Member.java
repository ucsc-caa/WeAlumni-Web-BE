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
    private Integer memberId;
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

    private enum Status {
        ACTIVE,
        PENDING,
        DEACTIVE,
        BRANCH,
        BOARD
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Address {
        String street;
        String city;
        String country;
        String postal;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Degree {
        private String studentId;
        String program;
        Integer endYear;
        String major1;
        String major2;
        String minor;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Career {
        Boolean status;
        String company;
        String position;
    }
}
