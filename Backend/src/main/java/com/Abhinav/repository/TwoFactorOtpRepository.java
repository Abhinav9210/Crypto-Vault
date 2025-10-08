package com.Abhinav.repository;

import com.Abhinav.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP,String> {

    TwoFactorOTP findByUserId(Long userId);
}
