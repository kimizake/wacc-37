lexer grammar WACCLexer;

// skip whitespace and newlines
WHITESPACE: (' '|'\t')+ -> skip;
COMMENT: HASH COMMENT_CHAR* '\n' -> skip;
EOL: '\n'+ -> skip ;

//binary operators
PLUS: '+' ;
MINUS: '-' ;
GTR_THAN: '>' ;
LSS_THAN: '<' ;
GTR_EQUAL: '>=' ;
LSS_EQUAL: '<=' ;
BIN_AND: '&&' ;
NOT_EQUAL: '!=' ;
EQUAL_TO: '==' ;
BIN_OR: '||' ;
DIVIDE: '/' ;
MULTIPLY: '*' ;
MODULO: '%' ;

//unary operators
BIN_NOT: '!' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

// type keywords
TYPE_INT: 'int' ;
TYPE_CHAR: 'char' ;
TYPE_STRING: 'string' ;
TYPE_BOOL: 'bool' ;

// statement keywords
WACC_SKIP: 'skip' ;
READ: 'read' ;
FREE: 'free' ;
RETURN: 'return' ;
EXIT: 'exit' ;
PRINT: 'print' ;
PRINTLN: 'println' ;
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else';
FI: 'fi' ;
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;
BEGIN: 'begin' ;
END: 'end' ;
ASSIGN: '=' ;
SEQ: ';' ;

// function keywords
IS: 'is' ;
CALL: 'call' ;
COMMA: ',' ;

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE: '[' ;
CLOSE_SQUARE: ']' ;

// single line comment
HASH: '#' ;

// pair keywords
FST: 'fst' ;
SND: 'snd' ;
NEWPAIR: 'newpair' ;
PAIR: 'pair' ;

//numbers
fragment DIGIT: '0'..'9' ;
fragment ESCAPED_CHAR: '0'
| 'b'
| 't'
| 'n'
| 'f'
| 'r'
| '"'
| '\''
| '\\'
;

fragment COMMENT_CHAR: ~('\n') ;

fragment CHARACTER: ~('\\'|'\''|'"')
| '\\'ESCAPED_CHAR
;

INT_LITER: DIGIT+ ;
BOOL_LITER: ('true' | 'false') ;
CHAR_LITER: '\''CHARACTER'\'' ;
STR_LITER: '"'CHARACTER*'"' ;
PAIR_LITER: 'null' ;

IDENT : [a-zA-Z_] [_a-zA-Z0-9]* ;
