package org.codenot.cs;

import org.codenot.cs.service.CheckoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final CheckoutService checkoutService;

    public Main(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        checkoutService.checkout();
    }
}
