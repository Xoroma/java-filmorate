package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractItemController<User>{

    @GetMapping
    @Override
    public List<User> getItem() {

        return  super.getItem();
    }

    @PostMapping
    @Override
    public User postItem(@Valid @RequestBody User user) throws MyValidateExeption {
        log.info("User {} was post to dataStorage",user);
        return  super.postItem(user);
    }

    @PutMapping
    @Override
    public User updateItem(@Valid @RequestBody User user) throws MyValidateExeption {
        log.info("User {} was updated in dataStorage",user);
        return super.updateItem(user);
    }



    @Override
    void validate(User user) throws MyValidateExeption {
        if(user.getName()==null || user.getName().isBlank()){
            log.info("Имя пользователя пустое. Имя установлено такое же как и логин");
            user.setName(user.getLogin());
        }

        if(user.getEmail().isBlank() || !user.getEmail().contains("@")){
            throw new MyValidateExeption("Email can't be empty");
        }

        if(user.getLogin()==null || user.getLogin().isEmpty()||user.getLogin().contains(" ")){
            throw new MyValidateExeption("Login can't be empty");
        }

        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new MyValidateExeption("Birthdate can't be in the future");
        }

    }


}
