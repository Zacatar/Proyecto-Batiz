package ArbolSintactico;

public class Multiplicacionx extends Expx {
    private Expx left;
    private Expx right;

    public Multiplicacionx(Expx left, Expx right) {
        this.left = left;
        this.right = right;
    }

    public Expx getLeft() {
        return left;
    }

    public Expx getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + " * " + right + ")";
    }
}
