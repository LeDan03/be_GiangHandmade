package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Cart;
import com.pandadev.gianghandmade.entities.CartItem;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.repositories.CartItemRepository;
import com.pandadev.gianghandmade.repositories.CartRepository;
import com.pandadev.gianghandmade.repositories.ProductRepository;
import com.pandadev.gianghandmade.requests.CartItemRequest;
import com.pandadev.gianghandmade.responses.CartItemResponse;
import com.pandadev.gianghandmade.responses.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItem toCartItem(CartItemRequest request) {
        Optional<Cart> cartOptional = cartRepository.findById(request.getCartId());
        Optional<Product> productOptional = productRepository.findById(request.getProductId());

        if(!(cartOptional.isPresent() && productOptional.isPresent())){
            throw new BadRequestException("Invalid request");
        }
        Cart cart = cartOptional.get();
        Product product = productOptional.get();
        CartItem  cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPriceSnapshot(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        cartItemRepository.save(cartItem);

        return cartItem;
    }
    public CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .priceSnapshot(item.getPriceSnapshot())
                .quantity(item.getQuantity())
                .productId(item.getProduct().getId())
                .build();
    }
    public List<CartItemResponse> toCartItemResponses(List<CartItem> items) {
        return items.stream().map(this::toCartItemResponse).toList();
    }
    public CartResponse toCartResponse(Cart cart) {
        List<CartItem> items = cart.getCartItems();
        int totalQuantity = items.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartResponse.builder()
                .cartId(cart.getId())
                .totalPrice(totalPrice)
                .quantity(totalQuantity)
                .items(toCartItemResponses(cart.getCartItems()))
                .build();
    }
}
