import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayStack<T> {
    T[] list;
    int actLength;
    public ArrayStack(T[] list){
        this.list = list;
        this.actLength = list.length;
    }
    @SuppressWarnings("unchecked")
    public ArrayStack(int actLength){
        this.actLength = actLength;
        this.list = (T[]) new Object[actLength];
    }
    @SuppressWarnings("unchecked")
    void MaybelengthenArr(){
        this.actLength++;
        if(this.list.length < this.actLength){
            T[] newArr = (T[]) new Object[this.actLength*2];
            for (int i = 0; i < this.list.length; i++){
                newArr[i] = this.list[i];
            }
            this.list = newArr;
        }
    }
    //returns a new empty AStack
    static <T>ArrayStack<T> emptyStack(){
        return new ArrayStack<T>(0);
    }
    //adds value to start
    void push(T elt){
        MaybelengthenArr();
        this.list[actLength-1] = elt;
    }
    //deletes value away from end of list
    T pop(){
        if(actLength < 1){
            throw new IndexOutOfBoundsException("Empty List");
        }
        T elt = this.list[actLength-1];
        this.list[actLength-1] = null;
        actLength--;
        return elt;
    }
    //looks at value at the end of list
    T peek(){
        if(actLength < 1){
            throw new IndexOutOfBoundsException("Empty List");
        }
        return this.list[actLength-1];
    }
    //checks size of array
    int size(){
        return actLength;
    }
    //checks if list is empty
    boolean isEmpty(){
        return (actLength == 0);
    }
}
