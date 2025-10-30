public class Buffer // Es la clase que representa el buffer con capacidad limitada.
{
    private int contenido; // Es una variable privada que almacena el número (turno) actual en el buffer.
    private boolean bufferlleno = Boolean.FALSE; // Es una bandera booleana que indica si el buffer está lleno (TRUE) o vacío (FALSE).

    /**
     * Obtiene de forma concurrente o síncrona el elemento que hay en el buffer
     * @return contenido el buffer
     */
    public synchronized int get() // Es un método sincronizado que devuelve el contenido del buffer. synchronized
    // asegura que solo un hilo puede ejecutar este método a la vez, garantizando la exclusion mutua.
    // Dentro de Buffer, se utilizan wait() y notify() para la sincronización condicional
    // El método get() se utiliza para consumir un número del buffer-
    {
        while (!bufferlleno) // Si el buffer está vacío (bufferlleno es FALSE), el hilo actual espera (wait())
            // hasta que se produzca un elemento. Hace que el hilo actual espere hasta que otro hilo invoque notify().
        {
            try   {   wait();   }
            catch (InterruptedException e) // Captura la excepción si el hilo es interrumpido mientras espera.
            {   System.err.println("Buffer: Error en get -> " + e.getMessage());    }
        }
        bufferlleno = Boolean.FALSE;
        notify(); // Notifica a otros hilos que están esperando que pueden proceder.
        return contenido; // Devuelve el número almacenado en el buffer.
    }

    /**
     * Introduce de forma concurrente o síncrona un elemento en el buffer
     * @param value Elemento a introducir en el contenedor
     */
    public synchronized void put(int value) // Es un método sincronizado que inserta un valor en el buffer.
    {
        while (bufferlleno) // Si el buffer está lleno, el hilo actual espera hasta que haya espacio disponible.
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                System.err.println("Buffer: Error en put -> " + e.getMessage());
            }
        }
        contenido = value;
        bufferlleno = Boolean.TRUE;
        notify(); // Notifica a otros hilos que pueden proceder, ya sea para consumir o producir.
    }
}
// La clase Productor y la clase Consumidor implementan la interfaz Runnable, lo que les permite ser ejecutadas
//por hilos. El Productor produce números aleatoriosy los coloca en el buffer utilizando el metodo put, y el Consumidor toma estos números del buffer.
//Ahora, creamos la clase «Productor»:
public class Productor implements Runnable
{
    private final Random aleatorio;
    private final Buffer contenedor;
    private final int idproductor;
    private final int TIEMPOESPERA = 3000;// equivale a 3 segundos

    /**
     * Constructor de la clase
     * @param contenedor Buffer común a los consumidores y el productor
     * @param idproductor Identificador del productor
     */
    public Productor(Buffer contenedor, int idproductor)
    {
        this.contenedor = contenedor;
        this.idproductor = idproductor;
        aleatorio = new Random();
    }

    @Override
    /**
     * Implementación del hilo
     */
    public void run()
    {
        while(Boolean.TRUE)
        {
            int poner = aleatorio.nextInt(41);//tenemos 41 números por atender.
            contenedor.put(poner);
            System.out.println("El productor " + idproductor + " genera el número: " + poner);
            try
            {
                Thread.sleep(TIEMPOESPERA);
            }
            catch (InterruptedException e)
            {
                System.err.println("Productor " + idproductor + ": Error en run -> " + e.getMessage());
            }
        }
    }
}
//Continuamos con la clase «Consumidor» (Goetz et al., 2006):
// La clase Consumidor también implementa Runnable y consume números del buffer llamando al método get()
public class Consumidor implements Runnable
{
    private final Buffer contenedor;
    private final int idconsumidor;

    /**
     * Constructor de la clase
     * @param contenedor Contenedor común a los consumidores y el productor
     * @param idconsumidor Identificador del consumidor
     */
    public Consumidor(Buffer contenedor, int idconsumidor)
    {
        this.contenedor = contenedor;
        this.idconsumidor = idconsumidor;
    }

    @Override
    /**
     * Implementación del hilo
     */
    public void run()
    {
        while(Boolean.TRUE)
        {
            System.out.println("La ventanilla " + idconsumidor + " llama al número: " + contenedor.get());
        }
    }
}
//Ahora, que tenemos el main de nuestro programa, denominaremos a la clase «ProductorConsumidor» (Goetz et al., 2006):
//  en la clase ProductorConsumidor, se crea un objeto Buffer, un hilo Productor y varios hilos Consumidor.
//  Se inician los hilos para simular la producción
//  y el consumo de números en un entorno concurrente
public class ProductorConsumidor
{
    private static Buffer contenedor;
    private static Thread productor;
    private static Thread [] consumidores;
    /**
     * asumimos que existen 4 ventanillas, las denominamos consumidores
     */
    private static final int CANTIDADCONSUMIDORES = 4;

    public static void main(String[] args)
    {
        contenedor = new Buffer();
        productor = new Thread(new Productor(contenedor, 1));
        consumidores = new Thread[CANTIDADCONSUMIDORES];

        for(int i = 0; i < CANTIDADCONSUMIDORES; i++)
        {
            consumidores[i] = new Thread(new Consumidor(contenedor, i));
            consumidores[i].start();
        }

        productor.start();
    }

}