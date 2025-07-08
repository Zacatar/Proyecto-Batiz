# LyAII_CodigoBase
Código base para proyecto de Lenguajes y Autómatas II


Hare los cambios de Expx por que eso me limita a solo poder representar operaciones simples como x - y, a + b.Pero no expresiones anidadas o combinadas
Lo cambiare por eso mismo a Idx = Identifiacador en el arbol sintactico.
Idx hereda de Expx, lo que significa que una variable es un tipo de expresion, Esto permite usar un Udx en cualquier lugar donde se espera un Expx.

El nodo Idx no realiza operaciones. Es simplemente un contenedor para decir: "Aqui se hace referencia a l variable llamada x."


Creamos Divisonx, Listax, Multiplicacionx y Restax, En el apartado de arbolSintactico


*Cambios realizados en el Árbol Sintáctico (Parser)
--Separación clara entre análisis de declaraciones y sentencias.

--Manejo correcto de listas de sentencias con bloques begin ... end y separación por ;.

--Soporte completo para sentencias if-then-else, asignaciones e impresión.

--Mejor control y consumo de tokens terminales (end, ;).

--Manejo de errores sintácticos con mensajes claros y parada controlada.

--El error "token (int) no concuerda con la gramática, se espera (if | begin | id | print)" se debe a que el parser encontró una declaración (int) donde esperaba una sentencia.

*La gramática distingue claramente entre:

--Declaraciones (D): deben ir al inicio del programa, antes de las sentencias.

--Sentencias (S): como if, asignaciones, bloques begin ... end y print.