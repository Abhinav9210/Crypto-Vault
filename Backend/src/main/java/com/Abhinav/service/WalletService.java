package com.Abhinav.service;


import com.Abhinav.exception.WalletException;
import com.Abhinav.model.Order;
import com.Abhinav.model.User;
import com.Abhinav.model.Wallet;

public interface WalletService {


    Wallet getUserWallet(User user) throws WalletException;

    public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException;

    public Wallet findWalletById(Long id) throws WalletException;

    public Wallet walletToWalletTransfer(User sender,Wallet receiverWallet, Long amount) throws WalletException;

    public Wallet payOrderPayment(Order order, User user) throws WalletException;



}
