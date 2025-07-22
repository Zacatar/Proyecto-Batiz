package ArbolSintactico;

import java.util.Vector;

public class Programax {
    private Vector<Declarax> declarations;
    private Statx statements;

    public Programax(Vector<Declarax> declarations, Statx statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    public Vector<Declarax> getDeclarations() {
        return declarations;
    }

    public Statx getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Declaraciones:\n");
        for (Declarax d : declarations) {
            sb.append("- ").append(d.getNombre()).append(" : ").append(d.getTipo().getTypex()).append("\n");
        }
        sb.append("\nSentencias:\n");
        if(statements != null)
            sb.append(statements.toString());
        else
            sb.append("No hay sentencias.");
        return sb.toString();
    }
}