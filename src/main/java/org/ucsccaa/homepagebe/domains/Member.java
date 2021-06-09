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
public class Member {
    @Id
    private Integer uid;
    @Column(unique = true)
    private Integer memberId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String name;
    private Boolean gender;
    private String birthday;
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

    public enum Status {
        ACTIVE,
        PENDING,
        DEACTIVE
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Encryptable
    public static class Address {
        private String street;
        private String city;
        private String country;
        private String postal;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Encryptable
    public static class Degree {
        private String studentId;
        private String program;
        private Integer endYear;
        private String major1;
        private String major2;
        private String minor;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Encryptable
    public static class Career {
        private String state;
        private String company;
        private String position;
    }
}