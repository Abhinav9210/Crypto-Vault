package com.Abhinav.repository;

import com.Abhinav.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin,String> {
}
