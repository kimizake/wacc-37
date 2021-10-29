parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

//comment: HASH COMMENT_CHAR* EOL ;
//newLine: EOL* -> skip;
arrayLiter: OPEN_SQUARE (expr (COMMA (expr))*)? CLOSE_SQUARE ;
arrayElem: IDENT (OPEN_SQUARE expr CLOSE_SQUARE)+ ;

type: baseType #basicType
| type OPEN_SQUARE CLOSE_SQUARE #arrayType
| PAIR OPEN_PARENTHESES (PAIR| type) COMMA (PAIR | type) CLOSE_PARENTHESES  #pairType
;

baseType: TYPE_INT | TYPE_CHAR | TYPE_STRING | TYPE_BOOL ;

pairElem: FST expr | SND expr ;
argList: expr (COMMA expr)* ;

assignRHS: expr
| arrayLiter
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
| pairElem
| CALL IDENT OPEN_PARENTHESES argList? CLOSE_PARENTHESES
;

assignLHS: IDENT
| arrayElem
| pairElem
;

unaryOper: BIN_NOT | MINUS | LEN | ORD | CHR ;


expr: OPEN_PARENTHESES expr CLOSE_PARENTHESES
| (MINUS|PLUS)?INT_LITER
| unaryOper expr
| expr (MULTIPLY | DIVIDE | MODULO) expr
| expr (PLUS | MINUS) expr
| expr (GTR_THAN | LSS_THAN | GTR_EQUAL | LSS_EQUAL | EQUAL_TO | NOT_EQUAL) expr
| expr (BIN_OR | BIN_AND) expr
| BOOL_LITER
| CHAR_LITER
| STR_LITER
| PAIR_LITER
| IDENT
| arrayElem
;

stat: stat_s;
stat_s: WACC_SKIP #statSkip
| type IDENT ASSIGN assignRHS #statIdentAssign
| assignLHS ASSIGN assignRHS #statAssignLHSAssign
| READ assignLHS #statRead
| FREE expr #statFree
| RETURN expr #statReturn
| EXIT expr #statExit
| PRINT expr #statPrint
| PRINTLN expr #statPrintLn
| IF expr THEN stat_s ELSE stat_s FI #statIf
| WHILE expr DO stat_s DONE #statWhile
| BEGIN stat_s END #statBegin
| stat_s SEQ stat_s #statSeq
;

param: type IDENT ;
paramList: param (COMMA param)* ;

func: type IDENT OPEN_PARENTHESES (paramList)? CLOSE_PARENTHESES IS stat END ;


// EOF indicates that the program must consume to the end of the input.
prog: BEGIN func* stat END EOF;
