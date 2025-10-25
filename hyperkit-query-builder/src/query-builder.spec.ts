// example.test.ts
import {describe, expect, it} from 'vitest';
import {
    and,
    contains,
    endsWith,
    equals,
    isIn,
    isNotIn,
    not,
    notEqual,
    or,
    startsWith
} from './query-builder';
import {RsqlQueryBuilder} from "./rsql-query-builder";

describe('RSQL predicates', () => {

    it('equals', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            equals("field", "value")
        )).toBe("field=='value'");
    });

    it('notEquals', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            notEqual("field", "value")
        )).toBe("field!='value'");
    });

    it('startsWith', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            startsWith("field", "value")
        )).toBe("field=='value*'");
    });

    it('endsWith', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            endsWith("field", "value")
        )).toBe("field=='*value'");
    });

    it('contains', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            contains("field", "value")
        )).toBe("field=='*value*'");
    });

    it('isIn', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            isIn("field", "v1", "v2", "v3")
        )).toBe("field=in=('v1','v2','v3')");
    });

    it('isNotIn', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            isNotIn("field", "v1", "v2", "v3")
        )).toBe("field=out=('v1','v2','v3')");
    });

    it('not Equals', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                equals("field", "v1")
            )
        )).toBe("field!='v1'");
    });

    it('not In', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                isIn("field", "v1", "v2", "v3")
            )
        )).toBe("field=out=('v1','v2','v3')");
    });

    it('not startsWith', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                startsWith("field", "value")
            )
        )).toBe("field!='value*'");
    });

    it('not endsWith', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                endsWith("field", "value")
            )
        )).toBe("field!='*value'");
    });

    it('not contains', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                contains("field", "value")
            )
        )).toBe("field!='*value*'");
    });

    it('and', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            and(
                equals("field1", "value1"),
                equals("field2", "value2")
            ))).toBe("(field1=='value1';field2=='value2')");
    });

    it('or', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            or(
                equals("field1", "value1"),
                equals("field2", "value2")
            )
        )).toBe("(field1=='value1',field2=='value2')");
    });

    it('not and', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                and(
                    equals("field1", "value1"),
                    equals("field2", "value2")
                )
            )
        )).toBe("(field1!='value1',field2!='value2')");
    });

    it('not or', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            not(
                or(
                    equals("field1", "value1"),
                    equals("field2", "value2")
                )
            )
        )).toBe("(field1!='value1';field2!='value2')");
    });

    it('nested not and/or', () => {
        expect(new RsqlQueryBuilder().buildQuery(
            and(
                equals("andf", "andv"),
                not(
                    or(
                        equals("orf", "orv"),
                        equals("orf2", "orv2"),
                        and(
                            equals("and2f", "and2v"),
                            equals("and2f2", "and2v2")
                        ),
                        not(
                            or(
                                equals("or2f", "or2v"),
                                equals("or2f2", "or2v2")
                            ))
                    )
                ),
                and(
                    equals("and3f", "and3v"),
                    equals("and3f2", "and3v2"),
                    equals("and3f3", "and3v3")
                )
            ))).toBe("(andf=='andv';(orf!='orv';orf2!='orv2';(and2f!='and2v',and2f2!='and2v2');(or2f=='or2v',or2f2=='or2v2'));(and3f=='and3v';and3f2=='and3v2';and3f3=='and3v3'))");
    });


});