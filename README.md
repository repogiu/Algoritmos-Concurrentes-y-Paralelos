# Algoritmos Concurrentes y Paralelos

Implementaciones en Java de ejercicios clásicos sobre concurrencia y paralelismo, con un ejemplo central del problema Productor–Consumidor resuelto mediante monitores y sincronización con `wait()`/`notify()`.

## Estructura del repositorio

- `Buffer/`: proyecto con la simulación de turnos basada en un único buffer, un productor y varios consumidores.
  - `src/Main.java`: punto de entrada mínimo generado por la plantilla del IDE.
  - `src/Buffer.java`: agrupa las clases `Buffer`, `Productor`, `Consumidor` y `ProductorConsumidor`, que implementan la lógica del monitor y los hilos involucrados.

- `monitores/`: variante equivalente del ejercicio dentro de otra estructura de proyecto.
  - `src/Main.java`: contiene las mismas clases que el módulo `Buffer`, listas para ejecutarse en un proyecto distinto.

## Ejecución del ejemplo Productor–Consumidor

1. Compila las clases desde la raíz del repositorio:

   ```bash
   javac Buffer/src/Buffer.java
   ```

2. Ejecuta la simulación:

   ```bash
   java -cp Buffer/src ProductorConsumidor
   ```

   El programa crea un único productor que genera turnos cada 3 segundos y cuatro consumidores (ventanillas) que van atendiendo los números disponibles en el buffer, coordinándose mediante `wait()` y `notify()` para evitar condiciones de carrera.

## Explicación del ejemplo

- **Buffer**: monitor que almacena un solo número, bloqueando a productores o consumidores según corresponda hasta que exista un turno disponible o espacio libre.
- **Productor**: genera números aleatorios (0–40) y los deposita en el buffer, simulando la emisión de turnos.
- **Consumidor**: representa una ventanilla que obtiene el siguiente turno disponible e imprime el número atendido.
- **ProductorConsumidor**: inicializa el monitor y lanza los hilos con la cantidad de ventanillas definida por `CANTIDADCONSUMIDORES`.

## Notas adicionales

- El código está escrito en Java estándar y puede compilarse con cualquier JDK moderno.
- Las clases carecen de declaración de paquete para facilitar la compilación directa desde la consola.
- La carpeta `monitores/` puede usarse como base para experimentar con variantes del monitor o integrar el ejemplo en otro proyecto.
