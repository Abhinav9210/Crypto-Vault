package com.Abhinav.service;

import com.Abhinav.domain.OrderStatus;
import com.Abhinav.domain.OrderType;
import com.Abhinav.model.*;
import com.Abhinav.repository.*;


import com.Abhinav.repository.OrderRepository;
import com.Abhinav.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {
@Autowired
    private   OrderRepository orderRepository;
@Autowired
    private  AssetService assetService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    public OrderServiceImplementation(OrderRepository orderRepository, AssetService assetService) {
//        this.orderRepository = orderRepository;
//        this.assetService = assetService;
//    }

    @Override
    @Transactional
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {

        System.out.println("39182381209381230912-03912-392-=32");

        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        System.out.println("ullulumasdasksfdk "+ order);

        return orderRepository.save(order);
//        return order;
    }

    @Override
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersForUser(Long userId, String orderType, String assetSymbol) {
        List<Order> allUserOrders = orderRepository.findByUserId(userId);

        if (orderType != null && !orderType.isEmpty()) {
            OrderType type = OrderType.valueOf(orderType.toUpperCase());
            allUserOrders = allUserOrders.stream()
                    .filter(order -> order.getOrderType() == type)
                    .collect(Collectors.toList());
        }

        if (assetSymbol != null && !assetSymbol.isEmpty()) {
            allUserOrders = allUserOrders.stream()
                    .filter(order -> order.getOrderItem().getCoin().getSymbol().equals(assetSymbol))
                    .collect(Collectors.toList());
        }

        return allUserOrders;

    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Cannot cancel order, it is already processed or cancelled.");
        }
    }


    private OrderItem createOrderItem(Coin coin,double quantity,double buyPrice,double sellPrice) {
        OrderItem orderItem = new OrderItem();


        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
//        orderItem.setBuyPrice(coin.getCurrentPrice());
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);


        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin,double quantity, User user) throws Exception {
        if(quantity<=0)throw new Exception("quantity should be > 0");
        double buyPrice=coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin,quantity,buyPrice,0);

        orderItem=orderItemRepository.save(orderItem);
        System.out.println("Order Item: " + orderItem);
        System.out.println("User 82131289: " + user);

        Order order = createOrder(user, orderItem, OrderType.BUY);

        System.out.println("2809383205832-5093235 " );
System.out.println("order item88888888888 " +orderItem);
        System.out.println("order 777777777777 " +order);

        orderItem.setOrder(order);

        System.out.println("Order ** " + order);


        walletService.payOrderPayment(order, user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        System.out.println("147**************");

        Order savedOrder = orderRepository.save(order);
        System.out.println("Order : " + savedOrder);
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
                order.getUser().getId(),
                order.getOrderItem().getCoin().getId()
        );
        System.out.println("155**************");
        if (oldAsset == null) {
            System.out.println("157**************");
            assetService.createAsset(
                    user,orderItem.getCoin(),
                    orderItem.getQuantity()
            );
        } else {
            System.out.println("163**************");
            assetService.updateAsset(
                    oldAsset.getId(),quantity
            );
        }
        System.out.println("168**************");
        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin,double quantity, User user) throws Exception {
        double sellPrice =coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(
                user.getId(),
                coin.getId()
        );

        if (assetToSell != null) {

            OrderItem orderItem = createOrderItem(coin,quantity, assetToSell.getBuyPrice(), sellPrice);

            Order order = createOrder(user, orderItem, OrderType.SELL);

            orderItem.setOrder(order);

            Order savedOrder = orderRepository.save(order);

            if (assetToSell.getQuantity() >= quantity) {

                walletService.payOrderPayment(order, user);

                Asset updatedAsset=assetService.updateAsset(
                        assetToSell.getId(),
                        -quantity
                );
                savedOrder.setStatus(OrderStatus.SUCCESS);
//                savedOrder.setOrderType(OrderType.SELL);

                 savedOrder = orderRepository.save(savedOrder);
                if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            } else {

                orderRepository.delete(order);
                throw new Exception("Insufficient quantity to sell");
            }
        }

            throw new Exception("Asset not found for selling");

    }

    @Override
    @Transactional
    public Order processOrder(Coin coin,double quantity,OrderType orderType, User user) throws Exception {

        if (orderType == OrderType.BUY) {
            return buyAsset(coin,quantity, user);
        } else if (orderType == OrderType.SELL) {
            return sellAsset(coin,quantity, user);
        } else {
            throw new Exception("Invalid order type");
        }
    }


}
