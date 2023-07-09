package solutions.sulfura.gend.dtos;

public class ListOperation<T> {

    public enum ListOperationType {
        READONLY,
        INSERT,
        UPDATE,
        /**
         * If the item exists UPDATE, else INSERT
         */
        UPSERT,
        DELETE
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

    public static <T> ListOperation<T> of(T value) {
        ListOperation<T> result = new ListOperation<>();
        result.setValue(value);
        return result;
    }

}