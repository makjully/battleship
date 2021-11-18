package ru.levelup.battleship;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.levelup.battleship.AppJpaConfiguration;
import ru.levelup.battleship.BattleshipApplication;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@ComponentScan(basePackages = {"ru.levelup.battleship"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                AppJpaConfiguration.class, BattleshipApplication.class
        })
})
public class TestMvcConfiguration {

}