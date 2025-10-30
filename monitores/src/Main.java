public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
public class Buffer
{
    private int contenido;
    private boolean bufferlleno = Boolean.FALSE;

    /**
     * Obtiene de forma concurrente o síncrona el elemento que hay en el buffer
     * @return contenido el buffer
     */
    public synchronized int get()
    {
        while (!bufferlleno)
        {
            try   {   wait();   }
            catch (InterruptedException e)
            {   System.err.println("Buffer: Error en get -> " + e.getMessage());    }
        }
        bufferlleno = Boolean.FALSE;
        notify();
        return contenido;
    }

    /**
     * Introduce de forma concurrente o síncrona un elemento en el buffer
     * @param value Elemento a introducir en el contenedor
     */
    public synchronized void put(int value)
    {
        while (bufferlleno)
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
        notify();
    }
}
Ahora, creamos la clase «Productor» (Goetz et al., 2006):
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
    Continuamos con la clase «Consumidor» (Goetz et al., 2006):
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
Ahora, que tenemos el main de nuestro programa, denominaremos a la clase «ProductorConsumidor» (Goetz et al., 2006):
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