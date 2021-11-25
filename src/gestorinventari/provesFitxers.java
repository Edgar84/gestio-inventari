
package gestorinventari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class provesFitxers {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        File fitxer = new File(".");
        System.out.println(fitxer.getAbsolutePath());
        if(fitxer.isDirectory()){
            //System.out.println("Directory");
        }else {
           // System.out.println("No directori");
        }
        File fitxer2 = new File ("files/ENTRADES PENDENTS");
        fitxer2.mkdirs();
        
        if(fitxer2.isDirectory()){
            File[] fitxers = fitxer2.listFiles();
            for(int i = 0; i < fitxers.length; i++){
                System.out.println(fitxers[i].getName());
                //li passon cada un dels fichers ([i])
                actualitzarFitxerDB(fitxers[i]);
            }
        }
        
        
        File fitxer3 = new File ("files/ENTRADES PROCESSADES");
        fitxer3.mkdirs();
    }
    
    static void actualitzarFitxerDB(File fitxer) throws FileNotFoundException, IOException {
        //Llegeix caracter a caracter el fitxer
        FileReader reader = new FileReader(fitxer);
        //Llegeix línea a línea el fitxer
        BufferedReader buffer = new BufferedReader(reader);
        
        String linea;
        while(( linea = buffer.readLine() ) != null){   //comprobar si la líena es diferent a null (hi ha dades al fitxer)
            System.out.println(linea); // retorna el format del document(idproducte:stock)(int:int)
        }
        
    }

    
}
