/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A minimal Spring Boot application. It contains no banner code at all: adding
 * the {@code blazon-spring} dependency replaces the startup banner
 * automatically, deriving the text from {@code spring.application.name} and the
 * metadata from the environment. See {@code application.properties} for the
 * (optional) tuning.
 */
@SpringBootApplication
public class BlazonSpringDemoApplication {

    /**
     * Boots the application, which prints the banner and then exits (there is no
     * web server).
     *
     * @param args passed through to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(BlazonSpringDemoApplication.class, args);
    }

}
