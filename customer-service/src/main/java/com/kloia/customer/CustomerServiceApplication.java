package com.kloia.customer;

import com.kloia.utils.objectmapper.ObjectMapperDateConfiguration;
import com.kloia.utils.objectmapper.ObjectMapperXssConfiguration;
import com.kloia.utils.xss.XssConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({XssConfiguration.class, ObjectMapperXssConfiguration.class, ObjectMapperDateConfiguration.class})
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
