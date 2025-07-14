

import javax.swing.JOptionPane;
import ArbolSintactico.*;
import java.util.Vector;
import org.apache.commons.lang3.ArrayUtils;

public class Parser {
    //Declaración de variables----------------
    Programax p = null;
    String[] tipo = null;
    String[] variable;
    String byteString;
    private Vector tablaSimbolos = new Vector();
    private final Scanner s;
    final int ifx=1, thenx=2, elsex=3, beginx=4, endx=5, printx=6, semi=7,
            sum=8, igual=9, igualdad=10, intx=11, floatx=12, id=13, whilex=17, dox=18, rest=14, multiplicacion=15, division=16, 
            longx = 20, doublex = 19;
    private int tknCode, tokenEsperado;
    private String token, tokenActual, log;
    
    //Sección de bytecode
    private int cntBC = 0; // Contador de lineas para el código bytecode
    private String bc; // String temporal de bytecode
    private int jmp1, jmp2, jmp3;
    private int aux1, aux2, aux3;
    private String pilaBC[] = new String[100];
    private String memoriaBC[] = new String[10];
    private String pilaIns[] = new String [50];
    private int retornos[]= new int[10];
    private int cntIns = 0;
    //---------------------------------------------
  
/*     public static void main(String[] args){
        //var1 int ; var2 int; if var1 == var2 then print var1 + var2 else begin	if var1 + var2 then var1 := var2 + var1	else var2 := var1 + var2 end
        new Parser("var1 int ; var2 int ; var1 := var2 + var1 ; print var1 + var2 ;");
    } */

public String getSymbolTableString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TABLA DE SÍMBOLOS\n");
    sb.append("-----------------\n");
    for(int i = 0; i < tablaSimbolos.size(); i++) {
        Declarax dx = (Declarax) tablaSimbolos.get(i);
        sb.append(dx.getNombre()).append(" : ").append(dx.getTipo().getTypex()).append("\n");
    }
    sb.append("-----------------\n");
    return sb.toString();
}


    public Parser(String codigo) {  
        s = new Scanner(codigo);
        token = s.getToken(true);
        tknCode = stringToCode(token);
        p = P();
    }
    
    //INICIO DE ANÁLISIS SINTÁCTICO
    public void advance() {
    token = s.getToken(true);          // Avanza al siguiente token
    tknCode = stringToCode(token);     // Convierte a código
    tokenActual = token;               // Guarda el token actual
}

public void eat(int t) {
    tokenEsperado = t;
    if (tknCode == t) {
        setLog("✔️ Token aceptado: " + token + " (Tipo: " + s.getTipoToken() + ")");
        advance();
    } else {
        error(token, "Se esperaba token de tipo: " + t + ", pero se recibió: " + tknCode);
    }
}

public Programax P() {
    D();          // Declaraciones
    createTable();
    Statx st = null;
    if (tknCode != -1) { // Asegurar que no sea EOF, según tu implementación
        st = S();
        while(tknCode == semi) {
            eat(semi);
            Statx siguiente = S();
            st = new Listax(st, siguiente);
        }
    }
    return new Programax(tablaSimbolos, st);
}



// Declaraciones múltiples (D → id (int | float) ; D | ε)
// Procesa múltiples declaraciones: D -> id (int | float) ; D | ε
public void D() {
    while (tknCode == id) {
        String idToken = token; // Guarda el id actual
        eat(id);                // Consume el id
        if (tknCode == intx || tknCode == floatx || tknCode == longx || tknCode == doublex) {
            Typex tipo = T();   // Consume el tipo (int o float)
            eat(semi);          // Consume ';'
            tablaSimbolos.addElement(new Declarax(idToken, tipo));
        } else {
            error(token, "(int | long | float | double)");
            return; // o lanzar excepción para detener ejecución
        }
    }
    // Cuando no hay más id, es ε (vacío), termina el ciclo
}


    
    public Typex T() {
    if(tknCode == intx) {
        eat(intx);
        return new Typex("int");
    } else if(tknCode == floatx) {
        eat(floatx);
        return new Typex("float");
    } else if(tknCode == longx) {
        eat(longx);
        return new Typex("long");
    } else if(tknCode == doublex) {
        eat(doublex);
        return new Typex("double");
    } else {
        error(token, "(int / float / long / double)");
        return null;
    }
}
    
    public Statx S() {
    switch(tknCode) {
        case ifx:
            Expx e1;
            Statx s1, s2;
            eat(ifx);
            e1 = E();
            eat(thenx);
            s1 = S();
            eat(elsex);
            s2 = S();
            return new Ifx(e1, s1, s2);

        case beginx:
            eat(beginx);
            Statx sentencias = S();   // Primera sentencia
            while (tknCode == semi) { // Mientras haya ';'
                eat(semi);
                Statx siguiente = S();
                sentencias = new Listax(sentencias, siguiente);
            }
            eat(endx);
            return sentencias;

        case id:
            Idx i;
            Expx e;
            eat(id);
            i = new Idx(tokenActual);
            declarationCheck(tokenActual);
            byteCode("igual", tokenActual);
            eat(igual);
            e = E();
            return new Asignax(i, e);

        case printx:
            Expx ex;
            eat(printx);
            ex = E();
            return new Printx(ex);
            
        case whilex:
            eat(whilex);
            Expx cond = E();
            eat(dox);
            Statx cuerpo = S();
            return new Whilex(cond, cuerpo);

        default:
            error(token, "(if | begin | id | print)");
            return null;
    }
}

    
    

    
    public Expx E() {
    Expx left = null;

    if (tknCode == id) {
        String id1 = token;
        declarationCheck(id1);
        eat(id);
        left = new Idx(id1);

        // Si el siguiente token es operador, procesar el segundo operando
        switch (tknCode) {
            case sum:      // +
                eat(sum);
                Expx rightSum = E();  // Recursión para permitir expresiones complejas
                return new Sumax(left, rightSum);
            case 14:       // -
                eat(14);
                Expx rightRest = E();
                return new Restax(left, rightRest);
            case 15:       // *
                eat(15);
                Expx rightMul = E();
                return new Multiplicacionx(left, rightMul);
            case 16:       // /
                eat(16);
                Expx rightDiv = E();
                return new Divisionx(left, rightDiv);
            case igualdad: // ==
                eat(igualdad);
                Expx rightComp = E();
                return new Comparax(left, rightComp);
            default:
                // No hay operador, solo un Idx
                return left;
        }
    } else {
        error(token, "(id)");
        return null;
    }
}

 //FIN DEL ANÁLISIS SINTÁCTICO
    
    
    
    public void error(String token, String t) {
        switch(JOptionPane.showConfirmDialog(null,
                "Error sintáctico:\n"
                        + "El token:("+ token + ") no concuerda con la gramática del lenguaje,\n"
                        + "se espera: " + t + ".\n"
                        + "¿Desea detener la ejecución?",
                "Ha ocurrido un error",
                JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
                double e = 1.1;
                break;
                
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
        }
    }
    
    public int stringToCode(String t) {
        int codigo = 0;
        switch(t) {
            case "if": codigo=1; break;    
            case "then": codigo=2; break;
            case "else": codigo=3; break;
            case "begin": codigo=4; break;
            case "end": codigo=5; break;
            case "print": codigo=6; break;
            case ";": codigo=7; break;
            case "+": codigo=8; break;
            case ":=": codigo=9; break;
            case "==": codigo=10; break;
            case "int": codigo=11; break; // Añadido para soportar tipo int
            case "float": codigo=12; break; // Añadido para soportar tipo float
            case "double" : codigo=19; break; // Añadido para soportar tipo double
            case "long" : codigo=20; break; // Añadido para soportar tipo long
            case "-": codigo = 14 ; break; // Añadido para soportar resta
            case "*": codigo = 15 ; break; // Añadido para soportar multiplicación
            case "/": codigo = 16 ; break; // Añadido para soportar división
            case "while": codigo=17; break; // Añadido para soportar while
            case "do": codigo=18; break; // Añadido para soportar do
            default: codigo=13; break;
        }
        return codigo;
    }
    
    //Métodos para recoger la información de los tokens para luego mostrarla
    public void setLog(String l) {
        if(log == null) {
            log = l + "\n \n";
        }
        else{
            log=log + l + "\n \n";
        }      
    }
    
    public String getLog() {
        return log;
    }
    //-----------------------------------------------
    
    //Recorrido de la parte izquierda del árbol y creación de la tabla de símbolos
   public void createTable() {
    int size = tablaSimbolos.size();
    variable = new String[size];
    tipo = new String[size];

    System.out.println("-----------------");
    System.out.println("TABLA DE SÍMBOLOS");
    System.out.println("-----------------");
    
    for(int i = 0; i < size; i++) {
        Declarax dx = (Declarax) tablaSimbolos.get(i);
        variable[i] = dx.getNombre();
        tipo[i] = dx.getTipo().getTypex();
        System.out.println(variable[i] + ": " + tipo[i]);
    }
    System.out.println("-----------------\n");
}


public void declarationCheck(String s) {
    boolean valido = false;
    for (int i = 0; i < tablaSimbolos.size(); i++) {
        Declarax dx = (Declarax) tablaSimbolos.elementAt(i);
        if(s.equals(dx.getNombre())) {
            valido = true;
            break;
        }
    }
    if(!valido) {
        System.out.println("La variable " + s + " no está declarada.\nSe detuvo la ejecución.");
        javax.swing.JOptionPane.showMessageDialog(null, "La variable [" + s + "] no está declarada", "Error",
              javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}


// Chequeo de tipos consultando la tabla de símbolos
public void compatibilityCheck(String s1, String s2) {
    Declarax elementoCompara1 = null;
    Declarax elementoCompara2 = null;
    
    // Busca elementos en la tabla de símbolos
    for (int i = 0; i < tablaSimbolos.size(); i++) {
        Declarax dx = (Declarax) tablaSimbolos.elementAt(i);
        if (dx.getNombre().equals(s1)) {
            elementoCompara1 = dx;
        }
        if (dx.getNombre().equals(s2)) {
            elementoCompara2 = dx;
        }
    }
    
    if (elementoCompara1 == null) {
        JOptionPane.showMessageDialog(null, "Variable no declarada: " + s1, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (elementoCompara2 == null) {
        JOptionPane.showMessageDialog(null, "Variable no declarada: " + s2, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    System.out.println("CHECANDO COMPATIBILIDAD ENTRE TIPOS (" + s1 + ", " + s2 + ").");
    
    if (!elementoCompara1.getTipo().getTypex().equals(elementoCompara2.getTipo().getTypex())) {
        JOptionPane.showMessageDialog(null, "Incompatibilidad de tipos: " + elementoCompara1.getNombre() + " (" 
            + elementoCompara1.getTipo().getTypex() + "), " + elementoCompara2.getNombre() + " (" 
            + elementoCompara2.getTipo().getTypex() + ").", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    
    public void byteCode(String tipo, String s1,String s2){
        int pos1=-1, pos2=-1;
        
        for(int i=0; i<variable.length; i++) {
            if(s1.equals(variable[i])) {
                pos1 = i;
            }
            if(s2.equals(variable[i])) {
                pos2 = i;
            }
        }
        
        switch(tipo) {
          case "igualdad":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": ifne " + (cntIns+4));
            jmp1 = cntBC;
          break;

          case "suma":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": iadd");
            jmp2 = cntBC;
          break;
        }
    }
    
    public void byteCode(String tipo, String s1) {
        int pos1 = -1;
        for(int i=0; i<variable.length; i++) {
            if(s1.equals(variable[i])) {
                pos1 = i;
            }
        }
        switch(tipo) {
            case "igual":
                pilaBC[cntBC+3] = cntIns+4 + ": istore_" + pos1;
                cntIns++;
                jmp2 = cntBC;
            break;
        }
    }
    
    public void ipbc(String ins) {
    while(cntBC < pilaBC.length && pilaBC[cntBC] != null) {
        cntBC++;
    }
    if (cntBC >= pilaBC.length) {
        // Manejar error o ampliar arreglo
        System.err.println("Error: pilaBC llena, no se puede agregar más instrucciones.");
        return;
    }
    pilaBC[cntBC] = ins;
    cntBC++;
    cntIns++;
}

}

