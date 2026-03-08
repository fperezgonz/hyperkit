export interface Criterion {
    negate(): Criterion
}

export class And implements Criterion {

    constructor(public predicates: Criterion[]) {
    }

    negate(): Criterion {
        return new Or(this.predicates.map(p => p.negate()))
    }

}

export class Or implements Criterion {

    constructor(public predicates: Criterion[]) {
    }

    negate(): Criterion {
        return new And(this.predicates.map(p => p.negate()))
    }

}

export class Equals implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new NotEqual(this.fieldName, this.value)
    }

}

export class NotEqual implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new Equals(this.fieldName, this.value)
    }

}

export class In implements Criterion {

    constructor(public fieldName: string, public values: any[]) {
    }

    negate(): Criterion {
        return new Out(this.fieldName, this.values)
    }

}

export class Out implements Criterion {

    constructor(public fieldName: string, public values: any[]) {
    }

    negate(): Criterion {
        return new In(this.fieldName, this.values)
    }

}

export class Not implements Criterion {

    constructor(public predicate: Criterion) {
    }

    negate(): Criterion {
        return this.predicate
    }

}

export class GreaterThan implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new LowerOrEqual(this.fieldName, this.value)
    }

}

export class GreaterOrEqual implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new LowerThan(this.fieldName, this.value)
    }

}

export class LowerThan implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new GreaterOrEqual(this.fieldName, this.value)
    }

}

export class LowerOrEqual implements Criterion {

    constructor(public fieldName: string, public value: any) {
    }

    negate(): Criterion {
        return new GreaterThan(this.fieldName, this.value)
    }
}

export class IsNull implements Criterion {

    constructor(public fieldName: string) {
    }

    negate(): Criterion {
        return new IsNotNull(this.fieldName)
    }

}

export class IsNotNull implements Criterion {

    constructor(public fieldName: string) {
    }

    negate(): Criterion {
        return new IsNull(this.fieldName)
    }

}

export class Like implements Criterion {

    constructor(public fieldName: string, public value: string) {
    }

    negate(): Criterion {
        return new NotLike(this.fieldName, this.value)
    }

}

export class NotLike implements Criterion {

    constructor(public fieldName: string, public value: string) {
    }

    negate(): Criterion {
        return new Like(this.fieldName, this.value)
    }

}

export class ILike implements Criterion {

    constructor(public fieldName: string, public value: string) {
    }

    negate(): Criterion {
        return new NotILike(this.fieldName, this.value)
    }

}

export class NotILike implements Criterion {

    constructor(public fieldName: string, public value: string) {
    }

    negate(): Criterion {
        return new ILike(this.fieldName, this.value)
    }

}

export class Between implements Criterion {

    constructor(public fieldName: string, public lower: any, public upper: any) {
    }

    negate(): Criterion {
        return new NotBetween(this.fieldName, this.lower, this.upper)
    }

}

export class NotBetween implements Criterion {

    constructor(public fieldName: string, public lower: any, public upper: any) {
    }

    negate(): Criterion {
        return new Between(this.fieldName, this.lower, this.upper)
    }

}

export interface CriteriaBuilder {
    buildQuery: (criterion: Criterion) => string;
}

export const and = (...predicates: Criterion[]) => new And(predicates)

export const or = (...predicates: Criterion[]) => new Or(predicates)

export const not = (predicate: Criterion) => new Not(predicate)

export const equals = (fieldName: string, value: any) => new Equals(fieldName, value)

export const notEqual = (fieldName: string, value: any) => new NotEqual(fieldName, value)

export const startsWith = (fieldName: string, value: any) => new Equals(fieldName, value + '*')

export const endsWith = (fieldName: string, value: any) => new Equals(fieldName, '*' + value)

export const contains = (fieldName: string, value: any) => new Equals(fieldName, '*' + value + '*')

export const isIn = (fieldName: string, ...values: any[]) => new In(fieldName, values)

export const isNotIn = (fieldName: string, ...values: any[]) => new Out(fieldName, values)

export const gt = (fieldName: string, value: any) => new GreaterThan(fieldName, value)

export const ge = (fieldName: string, value: any) => new GreaterOrEqual(fieldName, value)

export const lt = (fieldName: string, value: any) => new LowerThan(fieldName, value)

export const le = (fieldName: string, value: any) => new LowerOrEqual(fieldName, value)

export const isNull = (fieldName: string) => new IsNull(fieldName)

export const isNotNull = (fieldName: string) => new IsNotNull(fieldName)

export const like = (fieldName: string, value: string) => new Like(fieldName, value)

export const notLike = (fieldName: string, value: string) => new NotLike(fieldName, value)

export const iLike = (fieldName: string, value: string) => new ILike(fieldName, value)

export const notILike = (fieldName: string, value: string) => new NotILike(fieldName, value)

export const between = (fieldName: string, lower: any, upper: any) => new Between(fieldName, lower, upper)

export const notBetween = (fieldName: string, lower: any, upper: any) => new NotBetween(fieldName, lower, upper)