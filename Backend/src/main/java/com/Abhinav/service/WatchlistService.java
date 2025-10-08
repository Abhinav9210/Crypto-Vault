package com.Abhinav.service;

import com.Abhinav.model.Coin;
import com.Abhinav.model.User;
import com.Abhinav.model.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchList(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin,User user) throws Exception;
}
