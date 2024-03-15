grammar Archicode;
diagram: 'Diagram {' statement+ '}';

statement: relation | grouping;
relation: object '->' object |
          object '->('relationDirections')' object |
          object '->' object '('objectLayout')' |
          object '->('relationDirections')' object '('objectLayout')';
//relation: object '->(' relationDirections ')' object '(' objectLayout ')';
grouping: 'todo';
object: ID;
//Направление связей
direction: 'left' | 'right' | 'top' | 'bottom';
relationDirections: direction ',' direction;
objectLayout: direction;

ID: [a-zA-Z0-9]+;
NUM: [0-9]+;
WS: [ \t\r\n]+ -> skip;