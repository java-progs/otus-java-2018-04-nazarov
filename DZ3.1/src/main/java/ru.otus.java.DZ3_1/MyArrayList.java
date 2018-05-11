package ru.otus.java.DZ3_1;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private int MAX_ARRAY_SIZE = Integer.MAX_VALUE;
    private final int INITIAL_SIZE = 16;
    private int capacity;
    private int pointer = 0;
    private Object[] arrayList;

    public MyArrayList() {
        capacity = INITIAL_SIZE;
        arrayList = new Object[capacity];
    }

    public MyArrayList(int capacity) {
        if (capacity > 0 && capacity <= MAX_ARRAY_SIZE) {
            this.capacity = capacity;
        } else {
            this.capacity = INITIAL_SIZE;
        }
        arrayList = new Object[this.capacity];
    }

    private boolean increaseArraySize(int count) throws IndexOutOfBoundsException {
        // Если размер добавляемого массива больше чем свободно элементов в целевом массиве
        if (capacity - (pointer) < count) {

            if (MAX_ARRAY_SIZE - count < pointer) {
                throw new IndexOutOfBoundsException(String.format("Maximum array size %s exceeded. Pointer %s, add count %s", MAX_ARRAY_SIZE, pointer, count));
            }

            // Увеличиваем размер массива кратно capacity
            while (capacity < pointer + count) {

                if (MAX_ARRAY_SIZE - capacity < capacity) {
                    capacity = MAX_ARRAY_SIZE;
                } else {
                    capacity *= 2;
                }
            }

            Object[] arrayTmp = Arrays.copyOf(arrayList, capacity);
            arrayList = arrayTmp;

            return true;
        }

        return false;
    }

    @Override
    public int size() {
        return pointer;
    }

    @Override
    public boolean isEmpty() {
        return pointer == 0;
    }

    @Override
    public boolean add(T t) {
        try {
            increaseArraySize(1);
            arrayList[pointer++] = t;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < pointer) {
            return (T) arrayList[index];
        }
        throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, size()));
    }

    @Override
    public T set(int index, T element) throws IndexOutOfBoundsException {
        if (index >= 0 && index < pointer) {
            T oldElement = (T) arrayList[index];
            arrayList[index] = element;
            return oldElement;
        } else {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, size()));
        }
    }

    @Override
    public T remove(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < pointer) {
            T oldElement = (T) arrayList[index];
            for (int i = index; i < pointer; i++) {
                arrayList[i] = arrayList[i+1];
            }
            pointer--;
            return oldElement;
        } else {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, size()));
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] array = c.toArray();
        increaseArraySize(array.length);
        System.arraycopy(array, 0, arrayList, pointer, array.length);
        pointer += array.length;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("on method remove(Object o)");
        //return false;
    }

    @Override
    public boolean contains(Object o) {
        throw new RuntimeException("on method contains(Object o)");
        //return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new RuntimeException("on method iterator()");
        //return null;
    }

    @Override
    public Object[] toArray() {
        Object[] arrayNew = Arrays.copyOf(arrayList, size());
        return arrayNew;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new RuntimeException("on method toArray(T1[] a)");
        //return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new RuntimeException("on method containsAll(Collection<?> c)");
        //return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException("on method addAll(int index, Collection<? extends T> c)");
        //return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("on method removeAll(Collection<?> c)");
        //return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("on method retainAll(Collection<?> c)");
        //return false;
    }

    @Override
    public void clear() {
        throw new RuntimeException("on method clear()");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("on method add(int index, T element)");
    }

    @Override
    public int indexOf(Object o) {
        throw new RuntimeException("on method indexOf(Object o)");
        //return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new RuntimeException("on method lastIndexOf(Object o)");
        //return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new thisArrayListIterator();
        //return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new RuntimeException("on method listIterator(int index)");
        //return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("on method subList(int fromIndex, int toIndex)");
        //return null;
    }

    private class thisArrayListIterator implements ListIterator<T> {

        private int iteratorPointer = 0;
        private int lastReturnPointer = -1;

        @Override
        public boolean hasNext() {
            if (iteratorPointer < pointer) {
                return true;
            }
            return false;
        }

        @Override
        public T next() throws NoSuchElementException {
            if (iteratorPointer < pointer) {
                lastReturnPointer = iteratorPointer;
                return (T) arrayList[++iteratorPointer];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            if (iteratorPointer > 0) {
                return true;
            }
            return false;
        }

        @Override
        public T previous() throws NoSuchElementException {
            if (iteratorPointer > 0) {
                lastReturnPointer = iteratorPointer--;
                return (T) arrayList[lastReturnPointer];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            if (iteratorPointer < pointer - 1) {
                return iteratorPointer + 1;
            } else {
                return size();
            }
        }

        @Override
        public int previousIndex() {
            if (iteratorPointer > 0) {
                return iteratorPointer - 1;
            } else {
                return -1;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove()");
        }

        @Override
        public void set(T t) throws IllegalStateException {
            if (lastReturnPointer < 0) {
                throw new IllegalStateException();
            } else {
                MyArrayList.this.set(lastReturnPointer, t);
            }
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException("add(T t)");
        }
    }

}