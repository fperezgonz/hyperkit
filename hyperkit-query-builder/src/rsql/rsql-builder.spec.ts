// example.test.ts
import {describe, expect, it} from 'vitest';
import {
    and,
    contains,
    endsWith,
    equals, ge, gt,
    isIn,
    isNotIn, le, lt,
    not,
    notEqual,
    or,
    startsWith
} from '../criteria-builder/criteria-builder';
import {RsqlCriteriaBuilder} from "./rsql-criteria-builder";

describe('RSQL predicates', () => {

    it('equals', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            equals("field", "value")
        )).toBe("field=='value'");
    });

    it('notEquals', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            notEqual("field", "value")
        )).toBe("field!='value'");
    });

    it('startsWith', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            startsWith("field", "value")
        )).toBe("field=='value*'");
    });

    it('endsWith', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            endsWith("field", "value")
        )).toBe("field=='*value'");
    });

    it('contains', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            contains("field", "value")
        )).toBe("field=='*value*'");
    });

    it('isIn', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            isIn("field", "v1", "v2", "v3")
        )).toBe("field=in=('v1','v2','v3')");
    });

    it('isNotIn', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            isNotIn("field", "v1", "v2", "v3")
        )).toBe("field=out=('v1','v2','v3')");
    });

    it('greaterThan', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            gt("field", "value")
        )).toBe("field=gt='value'");
    });

    it('greaterOrEqual', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            ge("field", "value")
        )).toBe("field=ge='value'");
    });

    it('lowerThan', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            lt("field", "value")
        )).toBe("field=lt='value'");
    });

    it('lowerOrEqual', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            le("field", "value")
        )).toBe("field=le='value'");
    });

    it('not greaterThan', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(gt("field", "value"))
        )).toBe("field=le='value'");
    });

    it('not greaterOrEqual', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(ge("field", "value"))
        )).toBe("field=lt='value'");
    });

    it('not lowerThan', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(lt("field", "value"))
        )).toBe("field=ge='value'");
    });

    it('not lowerOrEqual', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(le("field", "value"))
        )).toBe("field=gt='value'");
    });


    it('not Equals', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                equals("field", "v1")
            )
        )).toBe("field!='v1'");
    });

    it('not In', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                isIn("field", "v1", "v2", "v3")
            )
        )).toBe("field=out=('v1','v2','v3')");
    });

    it('not startsWith', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                startsWith("field", "value")
            )
        )).toBe("field!='value*'");
    });

    it('not endsWith', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                endsWith("field", "value")
            )
        )).toBe("field!='*value'");
    });

    it('not contains', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                contains("field", "value")
            )
        )).toBe("field!='*value*'");
    });

    it('and', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            and(
                equals("field1", "value1"),
                equals("field2", "value2")
            ))).toBe("(field1=='value1';field2=='value2')");
    });

    it('or', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            or(
                equals("field1", "value1"),
                equals("field2", "value2")
            )
        )).toBe("(field1=='value1',field2=='value2')");
    });

    it('not and', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                and(
                    equals("field1", "value1"),
                    equals("field2", "value2")
                )
            )
        )).toBe("(field1!='value1',field2!='value2')");
    });

    it('not or', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
            not(
                or(
                    equals("field1", "value1"),
                    equals("field2", "value2")
                )
            )
        )).toBe("(field1!='value1';field2!='value2')");
    });

    it('nested not and/or', () => {
        expect(new RsqlCriteriaBuilder().buildQuery(
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