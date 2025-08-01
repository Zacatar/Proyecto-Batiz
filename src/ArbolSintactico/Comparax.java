package ArbolSintactico;

public class Comparax extends Expx {
    private Expx left;
    private Expx right;

    public Comparax(Expx left, Expx right) {
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
        return left.toString() + " == " + right.toString();
    }
}
