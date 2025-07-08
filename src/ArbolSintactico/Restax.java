package ArbolSintactico;

public class Restax extends Expx {
    private Expx s1;
    private Expx s2;

    public Restax(Expx st1, Expx st2) {
        s1 = st1;
        s2 = st2;
    }

    public Expx getLeft() {
        return s1;
    }

    public Expx getRight() {
        return s2;
    }

    @Override
    public String toString() {
        // Cuidado: Expx no tiene getIdx(), solo Idx lo tiene. 
        // Necesitas castear o modificar para manejar casos generales:
        String leftStr = (s1 instanceof Idx) ? ((Idx) s1).getIdx() : s1.toString();
        String rightStr = (s2 instanceof Idx) ? ((Idx) s2).getIdx() : s2.toString();

        return "(" + leftStr + " - " + rightStr + ")";
    }
}
