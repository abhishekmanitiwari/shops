package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    Logger logger = Logger.getLogger(CartService.class.getName());

    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart =cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        Cart savedCart = cartRepository.save(cart);
        return savedCart;
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override

    public Long initializeNewCart() {
        Cart newCart = new Cart();
        Long newCardId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCardId);
        newCart.setTotalAmount(BigDecimal.ZERO);
       Cart savedCart = cartRepository.save(newCart);

        System.out.println(savedCart.getId() + " " + savedCart.getTotalAmount() + " " + savedCart.getItems());
        return savedCart.getId();
    }

}
