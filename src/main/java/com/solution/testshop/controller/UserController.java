package com.solution.testshop.controller;

import com.solution.testshop.exception.CustomResponseException;
import com.solution.testshop.model.Order;
import com.solution.testshop.model.Product;
import com.solution.testshop.model.User;
import com.solution.testshop.repository.OrderRepository;
import com.solution.testshop.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user-service")
@SessionAttributes("order")
@Tag(name = "Контроллер пользователя", description = "Контроллер обрабатывающий запросы пользователя с ролью ROLE_USER")
@SecurityRequirement(name = "/v3/api-docs")
public class UserController {

    private ProductRepository prodRepo;

    private OrderRepository orderRepo;

    public UserController(ProductRepository repository, OrderRepository orderRepository) {
        prodRepo = repository;
        orderRepo = orderRepository;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает список всех товаров")
    public Iterable<Product> products() {
        return prodRepo.findAll();
    }

    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает конкретный товар", description = "Возвращает товар по указанному в пути id")
    public Optional<Product> getProduct(@PathVariable("id") @Parameter(description = "Идентификатор товара") Long id) {
        Optional<Product> product = prodRepo.findById(id);
        if (product.isEmpty()) {
            throw new CustomResponseException();
        }
        return product;
    }

    @PostMapping("/products/{prodId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавляет товар в заказ")
    public void addProduct(@ModelAttribute("order") @Parameter(description = "Атрибут сессии - заказ") Order order,
                           @PathVariable("prodId") @Parameter(description = "Идентификатор товара") Long id,
                           @AuthenticationPrincipal @Parameter(description = "Текущий пользователь") User user) {
        if (order.getUser() == null) {
            order.setUser(user);
        }
        Product prod = prodRepo.findById(id).get();
        order.addProduct(prod);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает список всех заказов текущего пользователя")
    public Optional<List<Order>> getUserOrders(@ParameterObject @AuthenticationPrincipal
                                               @Parameter(description = "Текущий пользователь") User user) {
        Long id = user.getId();
        return orderRepo.findOrderByUserId(id);
    }

    @GetMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает заказ текущего сеанса")
    public Order getOrder(@ParameterObject @ModelAttribute("order") @Parameter(description = "Атрибут сессии - заказ") Order order) {
        return order;
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Оплата заказа", description = "Устанавливается метка оплаты заказа и его сохранение в таблицу")
    public void payment(@ModelAttribute("order") @Parameter(description = "Атрибут сессии - заказ") Order order,
                        SessionStatus sessionStatus) {
        order.setIsPaid(true);
        order.setDate(new Timestamp(new Date().getTime()));
        orderRepo.save(order);
        sessionStatus.setComplete();
    }

}
