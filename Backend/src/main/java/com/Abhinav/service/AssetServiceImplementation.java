package com.Abhinav.service;


import com.Abhinav.model.Asset;
import com.Abhinav.model.Coin;
import com.Abhinav.model.User;
import com.Abhinav.repository.AssetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImplementation implements AssetService {
    private final AssetsRepository assetRepository;

    @Autowired
    public AssetServiceImplementation(AssetsRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        System.out.println("26**************");
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        asset.setCoin(coin);
        asset.setUser(user);
        System.out.println("31**************");
        return assetRepository.save(asset);
    }


//    public Asset buyAsset(User user, Coin coin, Long quantity) {
//        return createAsset(user,coin,quantity);
//    }

    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

    @Override
    public Asset getAssetByUserAndId(Long userId, Long assetId) {
        return assetRepository.findByIdAndUserId(assetId, userId);
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }


    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {

        Asset oldAsset = getAssetById(assetId);
        if (oldAsset == null) {
            throw new Exception("Asset not found...");
        }
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());

        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);
    }

}
