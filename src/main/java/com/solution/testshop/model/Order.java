package com.solution.testshop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Schema(description = "Сущность заказа")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор")
    private Long id;

    @ManyToOne
    @Schema(description = "Пользователь - владелец заказа")
    private User user;

    @Schema(description = "Дата оформления заказа")
    private Timestamp date;

    @ManyToMany(cascade = CascadeType.MERGE)
    @Schema(description = "Список товаров в заказе")
    private List<Product> products = new ArrayList<>();

    @Schema(description = "Метка подтверждения администратором")
    private Boolean orderConfirmation = false;

    @Schema(description = "Метка оплаты заказа")
    private Boolean isPaid = false;

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
