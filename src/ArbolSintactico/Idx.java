package ArbolSintactico;

public class Idx extends Expx {
    private String name;

    public Idx(String name) {
        this.name = name;
    }

    public String getid() {  // <- Método corregido
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
