package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.AbstracItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public abstract class AbstractItemController<T extends AbstracItem> {

    private HashMap<Integer, T> dataStorage = new HashMap<>();

    private Integer counter = 0;

    public List<T> getItem(){
      return new ArrayList<T>(dataStorage.values());
    }

    public T postItem(T item) throws MyValidateExeption {
        validate(item);
        item.setId(++counter);
        dataStorage.put(item.getId(), item);
        return item;
    }

    public T updateItem(T item) throws MyValidateExeption {
      validate(item);
      if(!dataStorage.containsKey(item.getId())){
          throw new MyValidateExeption("Film or User with such id did not found");
      }
        dataStorage.put(item.getId(), item);
        return item;
    }


    abstract void validate(T item) throws MyValidateExeption;
}
