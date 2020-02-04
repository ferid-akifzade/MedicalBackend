package org.medical.libs.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        if (message.isEmpty())
            this.message = "Default Message";
        else
            this.message = message;
    }
}
