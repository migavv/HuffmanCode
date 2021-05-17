class ExceptionNodo extends Exception {
    public ExceptionNodo(String s) {
        super(s);
    }
}

public class NodoB <E extends Comparable<E>> implements Comparable<NodoB<E>>{
    protected E llave;
    protected NodoB<E> hijoIzq;
    protected NodoB<E> hijoDer;


    public NodoB(E llave) {
        super();
        this.llave = llave;
    }

    public NodoB() {

    }
    public NodoB(E llave, NodoB<E> hijoIzq, NodoB<E> hijoDer) {
        super();
        this.llave = llave;
        this.hijoIzq = hijoIzq;
        this.hijoDer = hijoDer;
    }
    public E getLlave() {
        return llave;
    }
    public void setLlave(E llave) {
        this.llave = llave;
    }
    public NodoB<E> getHijoIzq() {
        return hijoIzq;
    }
    public void setHijoIzq(NodoB<E> hijoIzq) {
        this.hijoIzq = hijoIzq;
    }
    public NodoB<E> getHijoDer() {
        return hijoDer;
    }
    public void setHijoDer(NodoB<E> hijoDer) {
        this.hijoDer = hijoDer;
    }

    public void insertarAbb(E valor) throws ExceptionNodo {
        if(llave==valor) {
            throw new ExceptionNodo("El nodo ya esta");
        }if(llave.compareTo(valor)>0) {
            if(hijoIzq==null) {
                hijoIzq= new NodoB<E>(valor);
            }else {
                hijoIzq.insertarAbb(valor);
            }
        }else {
            if(hijoDer==null) {
                hijoDer= new NodoB<E>(valor);
            }
            else {
                hijoDer.insertarAbb(valor);
            }
        }
    }

    @Override
    public int compareTo(NodoB<E> o) {
        return llave.compareTo(o.getLlave());
    }
}