package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import javax.annotation.PostConstruct;
import javax.swing.*;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {

		SpringApplication.run(FilmorateApplication.class, args);

	}

}
