
package gestorinventari;

public class ProvaStrings {

    public static void main(String[] args) {
        
        String cadena = "Una frase qualsevol";
        String cadena2 = "12:5566";
        System.out.println(cadena.charAt(0));
        System.out.println(cadena.isEmpty());
        System.out.println(cadena.concat(" pot anar aquí."));
        System.out.println("".equals(cadena));
        System.out.println(cadena.equals("qualsevol"));
        System.out.println("qualsevol".equalsIgnoreCase("Qualsevol"));
        System.out.println(cadena.contains("Una"));
        System.out.println(cadena.endsWith("qualsevol") + "\n");
        
        System.out.println("IndexOf de 'a': " + cadena.indexOf("a"));
        System.out.println(cadena2.indexOf(":"));
        System.out.println(cadena2.indexOf(":",7));
        System.out.println(cadena2.lastIndexOf(":")); //retorna l'un¡ltima ocurrencia començant per l'esquerra
        
        System.out.println(cadena2.substring(0,cadena2.indexOf(":")));
        System.out.println(cadena2.substring(cadena2.indexOf(":") + 1)); //si nomes te un valor, comença a contar desde aquest valor
        int numProducte = Integer.parseInt(cadena2.substring(0,cadena2.indexOf(":")));
        int numUnitats =  Integer.parseInt(cadena2.substring(cadena2.indexOf(":") + 1));
        
        System.out.println("ID: " + numProducte + " Stock: " + numUnitats);
    }
    
}
