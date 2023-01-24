package com.solution.testshop.exception;

public class NullOrderException extends RuntimeException {

    public NullOrderException() {
        super("Невозможно оформить пустой заказ!");
    }
}
