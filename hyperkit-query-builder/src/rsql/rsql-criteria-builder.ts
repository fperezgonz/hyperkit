import {
    And,
    type Criterion,
    Equals, GreaterOrEqual,
    GreaterThan,
    In, LowerOrEqual, LowerThan,
    Not,
    NotEqual,
    Or,
    Out
} from "../criteria-builder/criteria-builder";

export class RsqlCriteriaBuilder {

    buildQuery(criterion: Criterion): string {

        if (criterion instanceof And) {
            return "(" + criterion.predicates.map(p => this.buildQuery(p)).join(";") + ")"
        } else if (criterion instanceof Or) {
            return "(" + criterion.predicates.map(p => this.buildQuery(p)).join(",") + ")"
        } else if (criterion instanceof Equals) {
            return `${criterion.fieldName}=='${criterion.value}'`
        } else if (criterion instanceof NotEqual) {
            return `${criterion.fieldName}!='${criterion.value}'`
        } else if (criterion instanceof GreaterThan) {
            return `${criterion.fieldName}=gt='${criterion.value}'`
        } else if (criterion instanceof GreaterOrEqual) {
            return `${criterion.fieldName}=ge='${criterion.value}'`
        } else if (criterion instanceof LowerThan) {
            return `${criterion.fieldName}=lt='${criterion.value}'`
        } else if (criterion instanceof LowerOrEqual) {
            return `${criterion.fieldName}=le='${criterion.value}'`
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