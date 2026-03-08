# Hyperkit Query Builder

A lightweight TypeScript library for building structured queries and exporting them to formats like RSQL.

## Features

- **Fluent API**: Build complex criteria using intuitive functions.
- **RSQL Support**: Generate RSQL strings from your criteria.
- **Type-Safe**: Written in TypeScript with full type definitions.
- **Extensible**: Easily add support for more query formats.

## Installation

```bash
npm install hyperkit-query-builder
```

## Usage

### Building Criteria

```typescript
import { and, or, equals, gt, contains } from 'hyperkit-query-builder';

const criteria = and(
  equals('status', 'active'),
  or(
    gt('age', 18),
    contains('name', 'John')
  )
);
```

### Exporting to RSQL

```typescript
import { RsqlCriteriaBuilder } from 'hyperkit-query-builder';

const rsqlBuilder = new RsqlCriteriaBuilder();
const rsql = rsqlBuilder.buildQuery(criteria);
// Result: (status=='active';(age=gt='18',name=='*John*'))
```

### Sorting

```typescript
import { asc, desc, HyperkitSortBuilder } from 'hyperkit-query-builder';

const sort = asc('name').desc('createdAt');
const sortString = new HyperkitSortBuilder().buildSort(sort);
// Result: name:ASC;createdAt:DESC
```

### Available Operators

- Logical: `and`, `or`, `not`
- Comparison: `equals`, `notEqual`, `gt`, `ge`, `lt`, `le`, `between`, `notBetween`
- String: `startsWith`, `endsWith`, `contains`, `like`, `notLike`, `iLike`, `notILike`
- Nullability: `isNull`, `isNotNull`
- Collections: `isIn`, `isNotIn`
