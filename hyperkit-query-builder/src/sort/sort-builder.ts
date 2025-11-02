export interface SortBuilder {
    buildSort(sort: Sort): string;
}

export class Order {

    public field: string;
    public direction: 'ASC' | 'DESC' = 'ASC';

    constructor(field: string, direction: 'ASC' | 'DESC' = 'ASC') {
        this.field = field;
        this.direction = direction;
    }

}

export class Sort {

    public orders: Order[] = [];

    constructor(...orders: Order[]) {
        this.orders = orders;
    }

    asc(field: string) {
        this.orders.push(new Order(field, 'ASC'))
        return this;
    }

    desc(field: string) {
        this.orders.push(new Order(field, 'DESC'))
        return this;
    }

}

export const asc = (field: string) => new Sort(new Order(field, 'ASC'));

export const desc = (field: string) => new Sort(new Order(field, 'DESC'));