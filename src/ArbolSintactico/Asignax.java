package ArbolSintactico;

public class Asignax extends Statx {
    private Idx s1;     // Variable del lado izquierdo (identificador)
    private Expx s2;    // Expresión del lado derecho

    public Asignax(Idx st1, Expx st2) {
        this.s1 = st1;
        this.s2 = st2;
    }

    // Retorna el identificador del lado izquierdo
    public Idx getIdx() {
        return s1;
    }

    // Retorna la expresión del lado derecho
    public Expx getExp() {
        return s2;
    }

    // Alias para navegación del árbol sintáctico
    public Expx getLeft() {
        return s1;
    }

    public Expx getRight() {
        return s2;
    }

    // Para impresión o depuración
    @Override
    public String toString() {
        return s1.toString() + " := " + s2.toString();
    }
    
}
