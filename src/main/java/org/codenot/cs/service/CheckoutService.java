package org.codenot.cs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckoutService {

    public void checkout() {
        log.info("Checkout started");
    }
}
