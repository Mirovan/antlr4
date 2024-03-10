grammar Archicode;
diagram: 'Diagram {' statement+ '}';
statement: relation | grouping;
relation: object '->' object;
grouping: 'todo';
object: ID;
ID: [a-zA-Z0-9]+;
NUM: [0-9]+;
WS: [ \t\r\n]+ -> skip;