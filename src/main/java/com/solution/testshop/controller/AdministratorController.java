package com.solution.testshop.controller;

import com.solution.testshop.exception.CustomResponseException;
import com.solution.testshop.model.Order;
import com.solution.testshop.model.Product;
import com.solution.testshop.repository.OrderRepository;
import com.solution.testshop.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin-service")
@Tag(name = "Контроллер администратора", description = "Контроллер обрабатывающий запросы пользователя с ролью ROLE_ADMIN")
@SecurityRequirement(name = "/v3/api-docs")
public class AdministratorController {

    private ProductRepository prodRepo;

    private OrderRepository orderRepo;

    public AdministratorController(ProductRepository repository, OrderRepository orderRepository) {
        prodRepo = repository;
        orderRepo = orderRepository;
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает список всех товаров")
    public Iterable<Product> products() {
        return prodRepo.findAll();
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавляет новый товар в таблицу")
    public void addProduct(@RequestBody @Parameter(description = "Новый товар передаваемый в теле запроса") Product product) {
        prodRepo.save(product);
    }

    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает конкретный товар", description = "Возвращает товар по указанному в пути id")
    public Optional<Product> getProduct(@PathVariable("id") @Parameter(description = "Идентификатор товара") Long id) {
        Optional<Product> product = prodRepo.findById(id);
        if (product.isEmpty()) {
            throw new CustomResponseException(id);
        }
        return product;
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаляет товар", description = "Удаляет товар из таблицы по указанному в пути id")
    public void deleteProduct(@PathVariable @Parameter(description = "Идентификатор товара") Long id) {
        if (!prodRepo.existsById(id)) {
            throw new CustomResponseException(id);
        }
        prodRepo.deleteById(id);
    }

    @PatchMapping("/products/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Изменяет товар", description = "Изменяет товар и сохраняет его в таблицу по указанному в пути id")
    public Product updateProduct(@PathVariable @Parameter(description = "Идентификатор товара") Long id,
                                 @RequestBody @Parameter(description = "Изменения для товара передаваемые в теле запроса") Product product) {
        if (!prodRepo.existsById(id)) {
            throw new CustomResponseException(id);
        }
        Product result = prodRepo.findById(id).get();
        if (product.getName() != null) {
            result.setName(product.getName());
        }
        if (product.getPrice() != null) {
            result.setPrice(product.getPrice());
        }
        return prodRepo.save(result);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает список всех оплаченных и подтвержденных заказов всех пользователей")
    public Optional<List<Order>> getAllOrders() {
        return orderRepo.findOrderByOrderConfirmationIsTrue();
    }

    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает список оплаченных заказов ожидающих подтверждения")
    public Optional<List<Order>> nonConfirmOrdList() {
        return orderRepo.findOrderByOrderConfirmationIsFalse();
    }

    @GetMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Возвращает заказ", description = "Возвращает заказ ожидающий подтверждения по указанному в пути id")
    public Optional<Order> nonConfirmOrd(@PathVariable @Parameter(description = "Идентификатор заказа") Long id) {
        Optional<Order> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            throw new CustomResponseException(id);
        }
        return order;
    }

    @PatchMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Подтверждает заказ", description = "Устанавливает в true метку подтверждения заказа")
    public void orderConfirmation(@PathVariable @Parameter(description = "Идентификатор товара") Long id) {
        if (!orderRepo.existsById(id)) {
            throw new CustomResponseException(id);
        }
        Order order = orderRepo.findById(id).get();
        order.setOrderConfirmation(true);
        orderRepo.save(order);
    }
}
