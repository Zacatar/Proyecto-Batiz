package ArbolSintactico;

public class Declarax {
    private String nombre;    // nombre de la variable
    private Typex tipo;       // tipo asociado
    
    public Declarax(String nombre, Typex tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public Typex getTipo() {
        return tipo;
    }
    
    @Override
    public String toString() {
        return nombre + " : " + tipo.getTypex();
    }

    public Object getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
}
