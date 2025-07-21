//package Compiler;

//import GUI.MainFrame;
import javax.swing.JOptionPane;

public class Scanner {
    
//DECLARACIONES
    private int lineaNo, k;
    private final String[] tokens;
    private String tipoToken;
    private String token;

    // Se agregaron "long" y "double"
    private final String[] reservadas = {"if", "then", "else", "begin", "print", "end", "while", "do", "int", "float", "long", "double", "repeat", "until"};

    // Se agregaron "-", "*", "/"
    private final String[] operadores = {"==", ":=", "+", "-", "*", "/"};
    
    private final String delimitador = ";";

//CONSTRUCTOR
    public Scanner(String codigo) {
    tokens = codigo
    .replaceAll("([:=]=|==|:=|\\+|\\-|\\*|/|;)", " $1 ")
    .trim()
    .split("\\s+");
        lineaNo = 0; 
        k=0;
        token = "";
    }
    
//MÉTODO que retorna tokens válidos al parser
    public String getToken(boolean b) {
        boolean tokenValido = false;  
        if (k >= tokens.length) return null;  
        token = tokens[k]; 
        if(b) {
    k++;
    if(k >= tokens.length) return null;
    token = tokens[k];
}

                
        //VERIFICACIÓN LÉXICA
        //Palabras reservadas:
        for (String reservada : reservadas) {
            if (token.equalsIgnoreCase(reservada)) {
                tokenValido = true;
                setTipoToken("Palabra reservada", b);
                break;
            }
        }
            //Operadores:
        if(!tokenValido) {
            for(String operador : operadores) {
                if(token.equals(operador)) {
                    tokenValido = true;
                    setTipoToken("Operador", b);
                    break;
                }
            }
        }
            //Delimitador:
        if(!tokenValido) {
            if(token.equals(delimitador)) {
                tokenValido = true;
                setTipoToken("Delimitador", b);
            }
        }
        
            //Identificadores:
        if(!tokenValido) {
            if(validaIdentificador(token)) {
                tokenValido = true;
                setTipoToken("Identificador", b);
            }
        }
        
            //Error:
        if(!tokenValido) {
            error("el token \"" + token + "\" es inválido para el lenguaje.");
            return "TOKEN INVÁLIDO";
        }
        return token;
    }
    
    public boolean validaIdentificador(String t) {
    if (t == null || t.isEmpty()) return false;

    char[] charArray = t.toCharArray();

    // Validar primer caracter
    if (!((charArray[0] >= 'a' && charArray[0] <= 'z') ||
          (charArray[0] >= 'A' && charArray[0] <= 'Z') ||
          charArray[0] == '_')) {
        return false;
    }

    // Validar resto caracteres
    for (int i = 1; i < charArray.length; i++) {
        char c = charArray[i];
        if (!((c >= 'a' && c <= 'z') ||
              (c >= 'A' && c <= 'Z') ||
              (c == '_') || (c == '-') || (c >= '0' && c <= '9'))) {
            return false;
        }
    }
    return true;
}

    
    public void setTipoToken(String tipo, boolean b) {
        if(b) {
            tipoToken = tipo;
        }
    }
       
    public String getTipoToken() {
        return tipoToken;
    }
    
    public String checkNextToken() {
    if (k >= tokens.length) return null;
    return tokens[k];
}

    
    public void error(String error) {
        switch(JOptionPane.showConfirmDialog(null,
                "Error léxico: " + error + ".\n"
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
}
