package ArbolSintactico;

public class Sumax extends Expx {
    private Expx s1;
    private Expx s2;
    
    public Sumax(Expx st1, Expx st2){ 
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
        return "(" + s1.toString() + " + " + s2.toString() + ")";
    }
}
