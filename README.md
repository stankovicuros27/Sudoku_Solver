## Sudoku Solver
### Heuristic Sudoku solver done as a part of internship 
##### Better performance than backtracking, especially for sudoku with dimensions larger than 9

#### Using multiple different strategies:
- Naked single (Naked Single means that in a specific cell only one digit remains possible.)
- Hidden single (Hidden Single means that for a given value and section only one field is left to place that value.)
- Locked candidates (If in a box all the candidates of a specific digit are confined to only one row or column, that digit cannot appear outside of that box in the same row or column.)
- Naked groups (Pairs, Triples, Quads)
- Hidden groups (Pairs, Triples, Quads)
- More complex structures (XWing, SwordFish)

###### Useful link : http://www.angusj.com/sudoku/hints.php
