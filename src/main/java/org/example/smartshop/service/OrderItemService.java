package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.entity.Commande;
import org.example.smartshop.model.entity.OrderItem;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItem create(Commande commande, Product product, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCommande(commande);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getUnitPrice());
        orderItem.setLineTotal(product.getUnitPrice() * quantity);

        return orderItemRepository.save(orderItem);
    }

}
