public class Dato<E extends Comparable<E>> implements Comparable<Dato<E>>{

    private E dato;
    private int frecuencia;

    public E getDato() {
        return dato;
    }

    public void setDato(E dato) {
        this.dato = dato;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Dato(E dato, int frecuencia) {
        this.dato = dato;
        this.frecuencia = frecuencia;
    }


    @Override
    public int compareTo(Dato o) {
        return frecuencia - o.frecuencia;
    }
}
