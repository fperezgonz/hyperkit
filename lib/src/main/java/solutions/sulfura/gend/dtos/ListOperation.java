package solutions.sulfura.gend.dtos;

public class ListOperation<T>{

    public enum ListOperationType{
        INSERT, UPDATE, UPSERT, DELETE, NONE
    }

    ListOperationType operationType;

    T value;

    public ListOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ListOperationType operationType) {
        this.operationType = operationType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }    

}