package com.Abhinav.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double quantity;
    private double buyPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_asset_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "coin_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_asset_coin"))
    private Coin coin;



}
