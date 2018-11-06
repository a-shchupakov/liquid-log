package ru.naumen.perfhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "ru.naumen" })
public class PerfhouseApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PerfhouseApplication.class);
    }


    public static void main(String[] args)
    {
        SpringApplication.run(PerfhouseApplication.class, args);
    }

}
