# JLogic

A Java project for parsing and evaluating logical expressions such as those in Discrete Mathematics.

## Process of Parsing

In order to parse a logical expression, JLogic will use the `parseExpression` method in `LogicTree.java` to iterate through the entire expression.
It removes all the spaces beforehand because spaces don't necessarily matter in the parsing.

It will then return an object of the type `Expression` which holds information pertaining to the expressions stored as a `LogicNode` object as the root of a `LogicTree`.
Each value is then parased through (recursively if needed) to create objects to use in the final evaluation.

## Examples of Logical Expressions

When evaluating logical expressions, there is a function, `makeTable()`, that will generate a truth table for you with the order of the expressions in ascending order
that way you can read it as you would write a normal truth table.

Here are some examples (which are also in `Main.java`):

#### `a ^ b`
```
| a | b | a ^ b |
+---+---+-------+
| T | T |   T   |
| T | F |   F   |
| F | T |   F   |
| F | F |   F   |
```

#### `~(a v b)`
```
| a | b | ~(a v b) |
+---+---+----------+
| T | T |    F     |
| T | F |    F     |
| F | T |    F     |
| F | F |    T     |
```

#### `a v b v c v d`
```
| a | b | c | d | a v b | (a v b) v c | ((a v b) v c) v d |
+---+---+---+---+-------+-------------+-------------------+
| T | T | T | T |   T   |      T      |         T         |
| T | T | T | F |   T   |      T      |         T         |
| T | T | F | T |   T   |      T      |         T         |
| T | T | F | F |   T   |      T      |         T         |
| T | F | T | T |   T   |      T      |         T         |
| T | F | T | F |   T   |      T      |         T         |
| T | F | F | T |   T   |      T      |         T         |
| T | F | F | F |   T   |      T      |         T         |
| F | T | T | T |   T   |      T      |         T         |
| F | T | T | F |   T   |      T      |         T         |
| F | T | F | T |   T   |      T      |         T         |
| F | T | F | F |   T   |      T      |         T         |
| F | F | T | T |   F   |      T      |         T         |
| F | F | T | F |   F   |      T      |         T         |
| F | F | F | T |   F   |      F      |         T         |
| F | F | F | F |   F   |      F      |         F         |
```

As you can tell with that last one, parentheses are automatically added because each logical expression is parsed as a `{left} {operator} {right}` value.

## Offline Application

As far as `Java` goes, there is no `.jar` application built yet but I am working on that at the moment. For now, below are links for applications for specific OS's.
 * [Windows](https://www.fellowhashbrown.com/download/pyLogic)

## Feedback, Suggestions, Bugs

Any feedback, suggestions, or bugs can be mentioned in my [Discord Server](https://discord.gg/W8yVrHt).
