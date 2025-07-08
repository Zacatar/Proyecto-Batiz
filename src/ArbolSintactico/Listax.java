package ArbolSintactico;

public class Listax extends Statx {
    private Statx first;
    private Statx rest;

    public Listax(Statx first, Statx rest) {
        this.first = first;
        this.rest = rest;
    }

    public Statx getFirst() {
        return first;
    }

    public Statx getRest() {
        return rest;
    }

    @Override
    public String toString() {
        if (rest != null) {
            return first.toString() + "\n" + rest.toString();
        } else {
            return first.toString();
        }
    }
}
