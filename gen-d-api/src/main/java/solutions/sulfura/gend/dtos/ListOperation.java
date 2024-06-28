package solutions.sulfura.gend.dtos;

public class ListOperation<T> {

    public enum ListOperationType {
        /**Don't perform automatic data modification with this item*/
        READONLY,
        /**Add to the list*/
        INSERT,
        /**Create or update entity data if it is on the list*/
        UPDATE,
        /**
         * If the item is not on the list, add it
         * Also create or update the entity data
         */
        UPSERT,
        /**
         * Remove the entity from the list. If this list represents an aggregate, the process might delete the entity from the database upon removal from the list
         * */
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