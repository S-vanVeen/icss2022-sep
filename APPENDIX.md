# ICSS Compiler - Bijlage

## Behaalde requirements

| Req | Wat | Waar |
|-----|-----|------|
| PA00 | HANStack (op basis van een List), HANQueue, HANLinkedList + unit tests | `nl.han.ica.datastructures`, tests in `src/test/.../datastructures` |
| PA01 | Level 0: stylerules, id-/class-/tag-selectors, kleur- en pixelwaarden | `ICSS.g4`, `ASTListener` |
| PA02 | Level 1: variabelen toekennen en gebruiken, bool-, percentage- en scalar-literals | `ICSS.g4`, `ASTListener` |
| PA03 | Level 2: `+`, `-`, `*` met de juiste voorrang | `ICSS.g4` (volgorde van de alternatieven), `ASTListener` |
| PA04 | Level 3: `if[...]` / `else`, ook genest | `ICSS.g4`, `ASTListener` |
| CH01 | Gebruik van een niet-gedefinieerde variabele geeft een fout | `Checker.getVariableType` |
| CH02 | `+` en `-` alleen met gelijke types, `*` alleen met een scalar | `Checker.getOperationType` |
| CH03 | Geen kleuren (en ook geen bools) in operaties | `Checker.getOperationType` |
| CH04 | `width`/`height` alleen px of %, `color`/`background-color` alleen een kleur | `Checker.checkDeclaration` |
| CH05 | De conditie van een if moet een bool zijn | `Checker.checkIfClause` |
| CH06 | Scopes (lijst van HashMaps, binnenste scope eerst) | `Checker.openScope` / `lookupVariable` |
| TR01 | Variabelen en berekeningen omzetten naar literals | `Evaluator.evaluate` |
| TR02 | If/else verwijderen, de body van de gekozen tak blijft over | `Evaluator.transformIfClause` |
| GE01/GE02 | Ingesprongen CSS genereren uit de getransformeerde AST | `Generator` |

## Tests

`mvn test`: 35 tests (HANStackTest, HANQueueTest, HANLinkedListTest, ParserTest, CheckerTest, GeneratorTest).

## Uitbreiding

Geen eigen uitbreiding gemaakt, er is dus ook geen aantal punten met de docent afgesproken.
