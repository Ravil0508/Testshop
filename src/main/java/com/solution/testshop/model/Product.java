package com.solution.testshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Schema(description = "Сущность товара")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор", accessMode = Schema.AccessMode.READ_ONLY)//accessMode = Schema.AccessMode.READ_ONLY
    private Long id;

    @NotBlank
    @Schema(description = "Наименование товара")
    private String name;

    @Positive
    @Schema(description = "Цена товара")
    private Double price;
}
