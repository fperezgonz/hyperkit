

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

export interface QueryBuilder {
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