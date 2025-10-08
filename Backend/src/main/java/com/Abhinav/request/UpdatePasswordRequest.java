package com.Abhinav.request;

import com.Abhinav.domain.VerificationType;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String sendTo;
    private VerificationType verificationType;
}
