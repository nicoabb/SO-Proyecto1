# SO-Proyecto1
Primer proyecto de la materia Sistemas Operativos

  La prestigiosa compañía sueca de mueblería Ikea te ha contratado como desarrollador
de software y tu primera tarea es realizar un programa donde se simule la línea de
producción de su escritorio de última colección PAHL. El mismo deberá de ser hecho en
Java, utilizando hilos, semáforos y soluciones de tipo productor/consumidor y/o
escritor/lector.

  En la simulación se ensamblará un escritorio, donde, para ello se deben producir 4
patas, 40 tornillos, y 1 tabla, luego se le pasará a un ensamblador las piezas para que
pueda armar el escritorio para su pronta entrega a las tiendas IKEA de todo el mundo.
Para esto, se deben seguir los siguientes lineamientos de la empresa:

+ Sobre los productores: Los productores deben comenzar trabajando y poseen
una cantidad de almacenamiento limitada para cada parte producida, la misma
será especificada más adelante. Antes de producir cualquier pieza, primero se
debe revisar en el almacén si hay disponibilidad para su creación.

  - Productor de patas: Un productor de este estilo puede hacer 2 patas por
día y el almacén sólo permite 34 patas. La empresa sólo puede costear un
máximo de 4 productores de patas.
  - Productor de tornillos: Este productor puede hacer 30 tornillos por día y
en el almacén sólo caben 300 tornillos. La empresa puede costear máximo
3 productores de tornillos.
  - Productor de tablas: A este productor le toma 3 días hacer una tabla
perfecta y en el almacén sólo caben 12 tablas. La empresa sólo puede
costear un máximo de 4 productores para las tablas.

+ Ensambladores: Después de que se hayan producido las piezas necesarias, el
ensamblador debe entrar a cada almacén para retirarlas, y le toma 2 días producir
un escritorio. Al terminar, el ensamblador debe aumentar el contador de escritorios
PAHL que se han producidos. La empresa sólo puede contratar como máximo 3
ensambladores. No hay un máximo de escritorios que se puedan producir.

+ Jefe: La única tarea del jefe es registrar el paso de los días. El jefe posee un
contador inicializado en el número de días restantes para la entrega general. Cada
día, el jefe disminuye el contador en una unidad, lo que le toma 8 horas. Si hay
alguien leyendo el contador cuando el jefe va a modificarlo, él debe esperar a que
el lector termine. Cuando el contador vaya a pasar el valor a 0. Solo hay 1 jefe en
la compañía y solo 1 contador. El resto del tiempo, el jefe está dormido.

+ Gerente: Cada día se dirige al contador, para verificar cuantos días faltan para
las entregas. Si el jefe está modificando el contador en ese momento, el gerente
espera a que él terminé antes de leer. Si el contador es distinto a cero, el gerente
va a dormir, por 8 horas. Si el contador es igual a 0, el gerente inicia las entregas
de los Panas, reinicializando el contador para el siguiente lote.

Finalmente, se le notifica que la empresa cuenta con una cantidad inicial de: 1
productor de patas, 1 productor de tornillos, 1 productor de tablas, y 1 ensamblador.
Su programa deberá hacer uso de una interfaz gráfica que permita observar y controlar
el sistema. Se debe poder conocer en cualquier momento:
- La cantidad de productores de cada tipo.
- La cantidad de patas, tornillos, y tablas disponibles en el almacén.
- La cantidad de ensambladores.
- La cantidad de escritorios PAHL terminados disponibles.
- Los días que faltan para la entrega.
- Qué está haciendo el jefe.
- Qué estás haciendo el gerente.
- Cualquier otro dato que considere relevante.

La simulación debe permitir, en tiempo de ejecución:
- Contratar o despedir un productor de cualquiera de los tres tipos.
- Contratar o despedir un ensamblador.
Además, a través de un archivo (texto, objeto, CSV o JSON), se le debe poder indicar
al programa los siguientes parámetros:
- Tiempo, en segundos, que dura un día en el programa, tomando en cuenta que
de forma inicial 1 día debe ser 1 segundo.
- Cantidad de días entre despachos.
- Las capacidades máximas para cada tipo de almacenes (No tome en cuenta el
almacén de escritorios PAHL terminados, para efectos de la simulación se
considerará capaz de contener cualquier cantidad de escritorios)
- La cantidad inicial de productores de cada tipo.
- La cantidad máxima de productores de cada tipo.
- La cantidad inicial de ensambladores.
- La cantidad máxima de ensambladores.
Nota: Las cantidades iniciales que están especificadas en la sección anterior, son las
que deben colocar en el archivo.
