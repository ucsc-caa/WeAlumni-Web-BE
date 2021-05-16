package org.ucsccaa.homepagebe.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private BasicInfo basicInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BasicInfo {
        private Integer uid;
        private Integer memberId;
        private String name;
        private String email;
        private Member.Status status;
        private Boolean emailVerified;
    }
}
