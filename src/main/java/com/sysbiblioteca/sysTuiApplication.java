package com.sysbiblioteca;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sysbiblioteca.tui.MenuPrincipal;

@SpringBootApplication
public class sysTuiApplication implements CommandLineRunner {
    @Autowired
    private MenuPrincipal menuPrincipal;

    public static void main(String[] args) {
        SpringApplication.run(sysTuiApplication.class, args);
    }

    @Override
    public void run(String @NonNull ... applicationArgs)throws Exception {
        menuPrincipal.iniciarMenu();
    }
}