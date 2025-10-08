package com.Abhinav.service;

import com.Abhinav.model.PaymentDetails;
import com.Abhinav.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails( String accountNumber,
                                             String accountHolderName,
                                             String ifsc,
                                             String bankName,
                                             User user
    );

    public PaymentDetails getUsersPaymentDetails(User user);


}
