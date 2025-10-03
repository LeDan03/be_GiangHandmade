package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.Cart;
import com.pandadev.gianghandmade.entities.CartItem;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.CartMapper;
import com.pandadev.gianghandmade.repositories.CartItemRepository;
import com.pandadev.gianghandmade.repositories.CartRepository;
import com.pandadev.gianghandmade.repositories.ProductRepository;
import com.pandadev.gianghandmade.requests.CartItemRequest;
import com.pandadev.gianghandmade.responses.CartItemResponse;
import com.pandadev.gianghandmade.responses.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartResponse getCartByUserId(int userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        return cartMapper.toCartResponse(cartOptional.get());
    }

    public CartItemResponse addItemToCart(CartItemRequest cartItemRequest) {
        Cart cart = cartRepository.findById(cartItemRequest.getCartId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        // Tìm cartItem có productId trùng
        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId() == cartItemRequest.getProductId())
                .findFirst();

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            // Nếu có thì update số lượng
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
        } else {
            // Nếu chưa có thì tạo mới
            cartItem = cartMapper.toCartItem(cartItemRequest);
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        return cartMapper.toCartItemResponse(cartItem);
    }

    public CartItemResponse updateCartItemQuantity(Long itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (quantity <= 0) {
            // Nếu số lượng <= 0 thì xóa luôn item
            cartItemRepository.delete(cartItem);
            return null;
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return cartMapper.toCartItemResponse(cartItem);
        }
    }

    public void removeCartItemsByIds(List<Long> requestIds) {
        cartItemRepository.deleteAllById(requestIds);
    }
}
