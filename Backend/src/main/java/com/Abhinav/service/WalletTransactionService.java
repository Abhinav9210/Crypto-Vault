package com.Abhinav.service;

import com.Abhinav.domain.WalletTransactionType;
import com.Abhinav.model.Wallet;
import com.Abhinav.model.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {
    WalletTransaction createTransaction(Wallet wallet,
                                        WalletTransactionType type,
                                        String transferId,
                                        String purpose,
                                        Long amount
    );

    List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type);

}
