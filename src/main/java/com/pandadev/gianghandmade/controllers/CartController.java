package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.requests.CartItemRequest;
import com.pandadev.gianghandmade.responses.ApiResponse;
import com.pandadev.gianghandmade.responses.CartItemResponse;
import com.pandadev.gianghandmade.responses.CartResponse;
import com.pandadev.gianghandmade.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addCartItem(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok().body(cartService.addItemToCart(request));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable long itemId, @RequestParam int quantity) {
        CartItemResponse response = cartService.updateCartItemQuantity(itemId, quantity);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> removeCartItemByIds(@RequestBody List<Long> requestIds) {
        cartService.removeCartItemsByIds(requestIds);
        return ResponseEntity.ok().body(new ApiResponse("Đã xóa items khỏi giỏ hàng",200));
    }
}
