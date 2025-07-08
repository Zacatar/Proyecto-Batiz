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
}
