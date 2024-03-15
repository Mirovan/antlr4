grammar Archicode;
diagram: 'Diagram {' statement+ '}';

statement: relation | grouping;
relation: object '->' object | object '->('relationDirections')' object;
grouping: 'todo';
object: ID;
//Направление связей
direction: 'left' | 'right' | 'top' | 'bottom';
relationDirections: direction ',' direction;

ID: [a-zA-Z0-9]+;
NUM: [0-9]+;
WS: [ \t\r\n]+ -> skip;