# SimpleJava Compiler
This project is a Compiler written to compile a simplified version of Java (simpleJava). Note that SimpleJava is similar to Java in many aspects, but it is non-object oriented. The compiler generates MIPS32 assembly code, which can then be run in a MIPS simulator, like Spim. This project was completed
in 2015 by Bryan Relampagos and Junn Geronimo.

#### An example of SimpleJava code
```
/* Factorial ... */

int factorial(int x) {
    if (x == 0)
       return 1;
    return x * factorial(x-1);
}


void main() {

     int x = factorial(Read());
     Print(x);
     Println();
}
```

# Lexical Analysis
Lexical analysis is the conversion of an input file into a stream of valid tokens to be used. The SimpleJava Compiler is using [JavaCC](https://javacc.java.net/) (Java Compiler Compiler) in order to handle generating our Lexical Analyzer. The tokens are defined in simplejava.jj as a set of regular expressions, which is then used as input to JavaCC to generate a lexical analyzer.

```
//  Keywords
TOKEN :
{
  < ELSE: "else">
|	< FOR: "for">
|	< CLASS: "class">
|	< DO: "do">
|	< FALSE: "false">
|	< IF: "if">
|	< TRUE: "true">
|	< WHILE: "while">
|	< RETURN: "return">
|	< NEW: "new">
|	< INTEGER_LITERAL: (["0"-"9"])+>
|	< IDENTIFIER: ["A"-"Z"](["A"-"Z","0"-"9","_"])* >
}
```

# Parsing
Once the input file has been broken up into valid tokens, we go on to the parsing phase of compiling. During this phase, we determine whether or not the sequence of tokens forms a syntactically correct program. Using our context free grammar (CFG) which describes SimpleJava, we define the proper sequence of tokens (simplejava.jj).

```
void program() :
{}
{
    //  Each program will be zero or more declarations
    //  (class definitions, function definitions, or function prototypes)
    //  ending with an EOF token.

    (declaration())* <EOF>
}
...

void statement() :
{}
{
    /* Checks all statements here */

    blockStatement()
|   LOOKAHEAD(2) assignmentStatement()
|   LOOKAHEAD(2) variableDeclarationStatement()
|   LOOKAHEAD(2) functionCallStatement()
|   LOOKAHEAD(2) incDecStatement()
|   ifStatement()
|   whileStatement()
|   doWhileStatement()
|   forStatement()
|   returnStatement()
|   <SEMICOLON>     //  Empty statement case
}
```

# Abstract Syntax Trees
Once we have parsed the file into a valid sequence of tokens, we then create abstract syntax trees of all variables (base, array, class), statements (if, while, declarations), and expressions (integer literals, binary). An example of how the trees are generated can be seen below. The definitions for each abstract syntax tree can be found in the AST*.java files.
```
int x = 3;
```
![AST](/img/ast.png)

# Semantic Analysis
By this point, we have determined our program is syntactically correct. However, we have yet to check for semantic errors. For instance, we want to check for problems such as
```
void bar() {
  if (10 + false) {
     x += true;
  }
}
```
In order to check for semantic errors, we traverse the abstract syntax trees made in the previous step. Some examples of semantic errors that the compiler looks for include:
* Structured Variable Errors (i.e. x.y = A[3] --> requires x to be a class var, y to be a instance var, A to be an array var)
* Function/Method Errors (i.e. bar(5, true) --> bar must be a function, parameters must be int & boolean)
* Type Errors (i.e. +,-,* ,/ --> must be used with integers, myVar = 3 --> myVar must be of type Int)

# Usage
1. Clone this repository and navigate into it.
2. ```java sjc [inputfile].sjava``` will generate [inputfile].s
3. Use a MIPS processor simulator, such as SPIM, on [inputfile].s to run the assembly code.
