package myORM.base.dataSets.reflections.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ColumnMyORM {
    String type();
}
