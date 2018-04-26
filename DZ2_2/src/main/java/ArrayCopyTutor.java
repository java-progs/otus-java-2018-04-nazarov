
import org.junit.Test;

/**
 * Данный класс делает то же, что делает стандартный ArrayList:
 * увеличивает размер массива, когда массив заполнен.
 *
 * Реализуйте метод deleteAnimal(int position) 
 *
 */
public class ArrayCopyTutor {
    int animals_capacity = 5;
    int animals_expand_by = 5;
    int animals_narrow_by = 5;
    int animals_size = 0;
    String[] animals = new String[animals_capacity];

    public static void log(String s) {
        System.out.println(s);
    }

    public void addAnimal(String animal) {
        if (animals_size>=animals_capacity) {
            expandAnimalsArray();
        }
        animals[animals_size] = animal;
        animals_size++;
    }

    private void expandAnimalsArray() {
        int old_animals_size = animals_capacity;
        animals_capacity += animals_expand_by;
        String[] animals_new = new String[animals_capacity];
        System.arraycopy(animals, 0, animals_new, 0, old_animals_size);
        animals = animals_new;
    }

    private void narrowAnimalsArray() {
        animals_capacity -= animals_narrow_by;
        String[] animals_new = new String[animals_capacity];
        System.arraycopy(animals, 0, animals_new, 0, animals_capacity);
        animals = animals_new;
    }

    public void insertAnimal(int position, String animal) {
        if (position<0 || position>animals_size-1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        /*
        Не совсем понятно для чего данный код. Если добавляется элемент в array[position], где position равна
        последнуму индексу массива, то по сути элемент добавляется в array[position+1].
        if (position==animals_size-1) {
            addAnimal(animal);
        } else {
        */
            if (animals_size>=animals_capacity) {
                expandAnimalsArray();
            }
            System.arraycopy(animals, position, animals, position+1, animals_size-position);
            animals[position] = animal;
            animals_size++;
        //}
    }

    public void deleteAnimal(int position) {
        if (position<0 || position>animals_size-1) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (position == 0) {
            System.arraycopy(animals, position+1, animals, position, animals_size-1);
            animals_size--;
        } else if (position == (animals_size-1)) {
            animals_size--;
        } else {
            System.arraycopy(animals, position+1, animals, position, animals_size-position-1);
            animals_size--;
        }

        if ((animals_capacity - animals_size) >= animals_narrow_by) {
            narrowAnimalsArray();
        }
    }

    public void showAnimals() {
        for (int i=0;i<animals_size; i++) {
            log(i+") "+animals[i]);
        }
    }

    @Test
    public void testAnimals() {
        addAnimal("Лошадь");
        addAnimal("Носорог");
        addAnimal("Собака");
        addAnimal("Змея");
        addAnimal("Обезьяна");
        addAnimal("Индюк");
        addAnimal("Косуля");
        addAnimal("Лев");
        addAnimal("Тигр");
        addAnimal("Кошка");
        addAnimal("Черепаха");
        insertAnimal(1, "Человек");
        deleteAnimal(2);
        showAnimals();
    }

}