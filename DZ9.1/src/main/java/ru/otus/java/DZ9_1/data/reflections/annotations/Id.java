package ru.otus.java.DZ9_1.data.reflections.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    String type();
}