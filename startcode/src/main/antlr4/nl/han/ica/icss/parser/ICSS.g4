grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---
stylesheet: (variableAssignment | stylerule)* EOF;

variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;

stylerule: selector OPEN_BRACE bodyItem* CLOSE_BRACE;

// things that can be inside { } of a stylerule, if or else
bodyItem: declaration
        | ifClause
        | variableAssignment
        ;

ifClause: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE bodyItem* CLOSE_BRACE elseClause?;

elseClause: ELSE OPEN_BRACE bodyItem* CLOSE_BRACE;

selector: ID_IDENT      #idSelector
        | CLASS_IDENT   #classSelector
        | LOWER_IDENT   #tagSelector
        ;

declaration: LOWER_IDENT COLON expression SEMICOLON;

// order matters here: multiply comes first so it binds tighter than plus/min
// plus and min are in one alternative, otherwise 10px - 5px + 2px goes wrong
expression: expression MUL expression               #multiplyOperation
          | expression op=(PLUS | MIN) expression   #addOrSubtractOperation
          | literal                                 #literalExpression
          | CAPITAL_IDENT                           #variableReference
          ;

literal: COLOR          #colorLiteral
       | PIXELSIZE      #pixelLiteral
       | PERCENTAGE     #percentageLiteral
       | SCALAR         #scalarLiteral
       | TRUE           #boolLiteral
       | FALSE          #boolLiteral
       ;

