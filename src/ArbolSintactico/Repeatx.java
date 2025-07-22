// ArbolSintactico/Repeatx.java
package ArbolSintactico;


public class Repeatx extends Statx {
    private Statx body; // La sentencia(s) a repetir
    private Expx condition; // La condición a verificar

    public Repeatx(Statx body, Expx condition) {
        this.body = body;
        this.condition = condition;
    }

    public Statx getBody() {
        return body;
    }

    public Expx getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "Repeat " + body + " Until (" + condition + ")";
    }
}