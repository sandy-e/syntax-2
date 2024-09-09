A parser for Leonardo, a program inspired by [Logo](https://en.wikipedia.org/wiki/Logo_%28programming_language%29). 

Commands include:

| Command | Result |
| ------- | ------ |
| FORW `d` | Leonardo moves forward `d` steps (for a positive integer `d`) | 
| BACK `d` | Leonardo moves backwards `d` steps (for a positive integer `d`) |
| LEFT `a` | Leonardo turns left `a` degrees, without changing position |
| RIGHT `a` | Leonardo turns right `a` degrees, without changing position |
| DOWN | Leonardo lowers the pencil so that it leaves a trail when Leonardo moves | 
| UP | Leonardo raises the pencil so that it leaves no trail when Leonardo moves |
| COLOR `c` | changes the color of the pen to the color `c` . Color is specified in hex format, e.g. `#FFA500` for orange.
| REP `r` <REPS> | Leonardo repeats <REPS> `r`  times (for a positive integer `r`). <REPS> is a sequence of one or more instructions, surrounded by quotation marks ('"'). If the sequence consists of only a single instruction, the quotation marks are optional


A formal grammar in Backus–Naur form is also defined. 
