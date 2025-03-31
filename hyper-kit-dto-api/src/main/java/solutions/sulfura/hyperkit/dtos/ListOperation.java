package solutions.sulfura.hyperkit.dtos;

public class ListOperation<T> {

    public enum ListOperationType {
        /**
         * Don't perform automatic data modification with this item
         */
        NONE,
        /**
         * Add to the list
         */
        ADD,
        /**
         * Remove the entity from the list. If this list is part of an aggregate, the process might delete the entity from the database upon removal from the list
         */
        REMOVE
    }

    public enum ItemOperationType {
        /**
         * Don't perform automatic data modification with this item
         */
        NONE,
        /**
         * Create entity if it is going to be added to the list
         */
        INSERT,
        /**
         * Update entity data if it is on the list or is going to be added to it
         */
        UPDATE,
        /**
         * If the item does not exist, create it with the supplied data
         * If the entity is on the list or is going to be added to it, update the data
         */
        UPSERT,
    }

    ListOperationType operationType;
    ItemOperationType itemOperationType;
    T value;

    public ListOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ListOperationType operationType) {
        this.operationType = operationType;
    }

    public ItemOperationType getItemOperationType() {
        return itemOperationType;
    }

    public void setItemOperationType(ItemOperationType itemOperationType) {
        this.itemOperationType = itemOperationType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static <T> ListOperation<T> valueOf(T value, ListOperationType listOperationType, ItemOperationType itemOperationType) {
        ListOperation<T> result = new ListOperation<>();
        result.setValue(value);
        result.operationType = listOperationType;
        result.itemOperationType = itemOperationType;
        return result;
    }

    public static <T> ListOperation<T> valueOf(T value) {
        return valueOf(value, ListOperationType.NONE, ItemOperationType.NONE);
    }

}