package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.Order;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.entities.ShippingInfo;
import com.pandadev.gianghandmade.entities.enums.OrderStatus;
import com.pandadev.gianghandmade.entities.enums.PaymentMethod;
import com.pandadev.gianghandmade.entities.enums.PaymentStatus;
import com.pandadev.gianghandmade.entities.enums.RegionType;
import com.pandadev.gianghandmade.mappers.OrderItemMapper;
import com.pandadev.gianghandmade.mappers.OrderMapper;
import com.pandadev.gianghandmade.repositories.OrderRepository;
import com.pandadev.gianghandmade.requests.OrderRequest;
import com.pandadev.gianghandmade.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final ShippingService shippingService;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderResponse createOrder(OrderRequest request){
        ShippingInfo shippingInfo = shippingService.getShippingInfoById(request.getShippingInfoId());
        Order order = new Order();
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        order.setPaymentStatus(getPaymentStatusByPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod())));
        order.setEstimatedDeliveryDate(getEstimatedDeliveryDate(order.getCreatedAt(), RegionType.valueOf(request.getRegionType()) ));
        order.setOrderItems(orderItemMapper.toOrderItems(request.getOrderItems(), order));

        order.setStreetAddress(shippingInfo.getStreetAddress());
        order.setWard(shippingInfo.getWard());
        order.setDistrict(shippingInfo.getDistrict());
        order.setProvince(shippingInfo.getProvince());
        order.setPhoneNumber(shippingInfo.getPhoneNumber());
        order.setRecipientName(shippingInfo.getRecipientName());

        order.setOrderStatus(OrderStatus.PENDING);//Cho xac nhan
        order.setTotalPrice(orderItemService.getTotalPrice(order.getOrderItems()));
        order.setTotalQuantity(orderItemService.getTotalQuantity(order.getOrderItems()));
        order.setShippingFeeAmount(shippingService.getFinalShippingFee(request.getExtraFee(), RegionType.valueOf(request.getRegionType())));

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    public PaymentStatus getPaymentStatusByPaymentMethod(PaymentMethod paymentMethod){
        if (Objects.requireNonNull(paymentMethod) == PaymentMethod.COD) {
            return PaymentStatus.PENDING;
        }
        return PaymentStatus.UNPAID;
    }

    public Timestamp getEstimatedDeliveryDate(Timestamp orderCreatedDate, RegionType regionType) {
        LocalDateTime created = orderCreatedDate.toLocalDateTime();
        LocalDateTime estimated;

        switch (regionType) {
            case LOCAL -> estimated = created.plusDays(3);
            case DOMESTIC -> estimated = created.plusDays(5);
            case INTERNATIONAL -> estimated = created.plusDays(10);
            default -> throw new IllegalArgumentException("Unsupported region type: " + regionType);
        }

        return Timestamp.valueOf(estimated);
    }

}
