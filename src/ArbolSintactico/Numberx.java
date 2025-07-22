package ArbolSintactico;

public class Numberx extends Expx {
    private String value;

    public Numberx(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}