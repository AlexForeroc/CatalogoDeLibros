package com.alexfc.catalogodelibros;

import com.alexfc.catalogodelibros.principal.Principal;
import com.alexfc.catalogodelibros.repository.XautorRepository;
import com.alexfc.catalogodelibros.repository.XlibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoDeLibrosApplication implements CommandLineRunner {

    @Autowired
    private XlibroRepository xlibroRepository;
    @Autowired
    private XautorRepository xautorRepository;

    public static void main(String[] args) {
        SpringApplication.run(CatalogoDeLibrosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(xlibroRepository, xautorRepository);
        principal.muestraElMenu();
    }
}
