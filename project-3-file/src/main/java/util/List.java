package util;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a list of appointments. It provides methods for
 * adding, removing, and sorting appointments based on different criteria
 * such as patient, location, and provider.
 * It is generic and can be used to list any type
 * Teacher improved use of import statement and use of instance variable in
 * Iterator subclass
 * @author Keshav Dave, Daniel Watson
 */
public class List<E> implements Iterable<E> {
    // Variables
    private E[] objects;
    private int size;

    /**
     * Constructor for the List class with an initial capacity of 4
     * SuppressWarnings("unchecked") tag makes it so we can cast (E[]) to the
     * object array without error
     */
    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[4];
        this.size = 0;
    }

    /**
     * Method to find the index of an object in the list
     * @param obj the object to find
     * @return the index of the object, or -1 if not found
     */
    private int find(E obj) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(obj)) {
                return i;
            }
        }
        return -1; // Return -1 if the object is not found
    }

    /**
     * Private method to grow the array when it's full
     * SuppressWarnings("unchecked") tag makes it so we can cast (E[]) to the
     * object array without error
     */
    private void grow() {
        @SuppressWarnings("unchecked")
        E[] newObjects = (E[]) new Object[objects.length + 4];
        System.arraycopy(objects, 0, newObjects, 0, size);
        objects = newObjects;
    }

    /**
     * Method to check if the list contains an object
     * @param obj the object to check
     * @return true if the object is found, false otherwise
     */
    public boolean contains(E obj) {
        return find(obj) != -1;
    }

    /**
     * Method to add an object to the list. If the array is full, it will grow
     * @param obj the object to add
     */
    public void add(E obj) {
        // Check if the appointment is already in the list
        if (this.contains(obj)) {
            return; // If already present, do not add
        }

        // Check if the array has space
        if (this.size == this.objects.length) {
            this.grow(); // This method should handle resizing the array
        }

        // Add the appointment to the end of the array
        this.objects[this.size] = obj; // Add at the current size index
        this.size++; // Increase the size
    }

    /**
     * Method to remove an object from the list
     * Shifts the remaining elements after removal
     * @param obj the object to remove
     */
    public void remove(E obj){
        if(!this.contains(obj))
            return;
        int indexOfRemoved = indexOf(obj);
        if(indexOfRemoved == this.size-1){
            this.objects[indexOfRemoved] = null;
            this.size--;
            return;
        }
        for(int i = indexOfRemoved; i < this.size-1; i++){
            this.objects[i] = this.objects[i+1];
        }
        this.objects[this.size - 1] = null;
        this.size--;
    }

    /**
     * Method to check if the list is empty
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Method to return the size of the list
     * @return the number of elements in the list
     */
    public int size() {
        return this.size;
    }

    /**
     * Iterator method to iterate over the list
     * Overrides iterator method from Iterator interface
     * Implements the Iterable interface
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Method to get an object at a specific index
     * @param index the index of the object
     * @return the object at the specified index
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return objects[index];
    }

    /**
     * Method to set an object at a specific index
     * @param index the index to set the object at
     * @param obj   the object to set
     */
    public void set(int index, E obj) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.objects[index] = obj;
    }

    /**
     * Method to return the index of an object in the list
     * @param obj the object to find
     * @return the index of the object, or -1 if not found
     */
    public int indexOf(E obj){
        for (int i = 0; i < this.size; i++) {
            if (this.objects[i].equals(obj)) {
                return i; // Return the index if the element matches
            }
        }
        return -1;
    }

    /**
     * Inner class to implement the ListIterator for iteration over the list
     */
    private class ListIterator implements Iterator<E> {
        private int iterator = 0;

        /**
         * Iterator class override to find out if the iterator has a next
         * spot in the list to go to
         * @return true if there is a next, false otherwise
         */
        @Override
        public boolean hasNext() {
            return iterator < size;
        }

        /**
         * Iterator class override to return the next element in list
         * @return the next object in list or null if there is no next object
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return objects[iterator++];
        }
    }
}