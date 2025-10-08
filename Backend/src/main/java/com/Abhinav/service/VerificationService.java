package com.Abhinav.service;

import com.Abhinav.domain.VerificationType;
import com.Abhinav.model.User;
import com.Abhinav.model.VerificationCode;

public interface VerificationService {
    VerificationCode sendVerificationOTP(User user, VerificationType verificationType);

    VerificationCode findVerificationById(Long id) throws Exception;

    VerificationCode findUsersVerification(User user) throws Exception;

    Boolean VerifyOtp(String opt, VerificationCode verificationCode);

    void deleteVerification(VerificationCode verificationCode);
}
