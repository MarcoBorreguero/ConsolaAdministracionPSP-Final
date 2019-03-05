package consolaadministracion.model.excepciones;

/**
 * Clase excepción personalizada que trata de manejar las excepciones de forma
 * global
 *
 * @author Marco Borreguero
 */
public class ExcepcionConsola extends Exception {

    /**
     *
     */
    public ExcepcionConsola() {
        super();
    }

    /**
     *
     * @param msg
     */
    public ExcepcionConsola(String msg) {
        super(msg);
    }

}
