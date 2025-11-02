import {Order, Sort, type SortBuilder} from "../sort/sort-builder";

export class HyperkitSortBuilder implements SortBuilder {

    buildSort(sort: Sort): string {

        let result = ""

        for (let i = 0; i < sort.orders.length; i++) {

            const order: Order = sort.orders[i]!

            if (i > 0) {
                result += ";"
            }

            result += order.field + ":" + order.direction

        }

        return result

    }

}