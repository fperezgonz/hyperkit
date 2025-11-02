import {describe, it, expect} from 'vitest';
import {HyperkitSortBuilder} from './hyperkit-sort-builder';
import {asc, desc} from "../sort/sort-builder";

describe('HyperKitSortBuilder', () => {

    const builder = new HyperkitSortBuilder()

    it('should add ascending sort', () => {
        const sort = asc("name")
        expect(builder.buildSort(sort))
            .toEqual("name:ASC")
    });

    it('should add descending sort', () => {
        const sort = desc("age")
        expect(builder.buildSort(sort))
            .toEqual("age:DESC")
    });

    it('should chain multiple sorts', () => {
        const sort = asc('name')
            .desc('age')
        expect(builder.buildSort(sort))
            .toEqual("name:ASC;age:DESC")
    })

})