package org.ucsccaa.homepagebe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.utils.Encryptable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Encryptable
public class LoginResponse {
    private String token;
    private BasicInfo basicInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Encryptable
    public static class BasicInfo {
        private Integer uid;
        private Integer memberId;
        private String name;
        private String email;
        private Member.Status status;
        private Boolean emailVerified;
    }
}
