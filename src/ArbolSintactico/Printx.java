package ArbolSintactico;

public class Printx extends Statx {
    private Expx expr;

    public Printx(Expx expr) {
        this.expr = expr;
    }

    public Expx getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "print " + expr.toString();
    }
}
