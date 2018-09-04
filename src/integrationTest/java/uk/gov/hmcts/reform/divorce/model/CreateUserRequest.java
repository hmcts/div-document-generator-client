package uk.gov.hmcts.reform.divorce.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String forename;
    private String surname;
    private UserCode[] roles;
    private UserCode userGroup;
}
