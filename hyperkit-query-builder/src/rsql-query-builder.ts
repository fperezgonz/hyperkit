import {And, type Criterion, Equals, In, Not, NotEqual, Or, Out} from "./query-builder";

export class RsqlQueryBuilder {

    buildQuery(criterion: Criterion): string {

        if (criterion instanceof And) {
            return "(" + criterion.predicates.map(p => this.buildQuery(p)).join(";") + ")"
        } else if (criterion instanceof Or) {
            return "(" + criterion.predicates.map(p => this.buildQuery(p)).join(",") + ")"
        } else if (criterion instanceof Equals) {
            return `${criterion.fieldName}=='${criterion.value}'`
        } else if (criterion instanceof NotEqual) {
            return `${criterion.fieldName}!='${criterion.value}'`
        } else if (criterion instanceof In) {
            return `${criterion.fieldName}=in=(${criterion.values.map(v => `'${v}'`).join(",")})`
        } else if (criterion instanceof Out) {
            return `${criterion.fieldName}=out=(${criterion.values.map(v => `'${v}'`).join(",")})`
        } else if (criterion instanceof Not) {
            return this.buildQuery(criterion.predicate.negate())
        }

        throw new Error("Unsupported criterion type: " + criterion.constructor.name)

    }

}