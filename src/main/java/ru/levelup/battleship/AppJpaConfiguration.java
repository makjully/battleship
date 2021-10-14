package ru.levelup.battleship;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "ru.levelup.battleship.dao")
@EnableJpaRepositories(basePackages = "ru.levelup.battleship.dao")
@EnableTransactionManagement
@EntityScan(basePackages = "ru.levelup.battleship.model")
public class AppJpaConfiguration {
}