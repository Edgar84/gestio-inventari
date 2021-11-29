
package gestorinventari;


import static gestorinventari.GestorInventari.actualitzarStock;
import static gestorinventari.GestorInventari.analitzarComandes;
import static gestorinventari.GestorInventari.generarComandes;
import java.io.IOException;
import java.sql.SQLException;

public class CallMenu {
    
    static boolean sortir = false;
    
    static void callMenu() throws SQLException, IOException{
        
        do {
            System.out.println("");
            System.out.println("******MENU GESTOR INVENTARI******");
            System.out.println("1. Gestionar productes"); //Accés a DDBB Scanner
            System.out.println("2. Actualitzar stock"); //Lectura de fitxers
            System.out.println("3. Generació de comandes"); //Escritura de firxers
            System.out.println("4. Analitzar comandes proveidors"); //arrays
            System.out.println("\nTRIA UNA OPCIÓ");

            int opcioMenu = GestorInventari.teclat.nextInt();

            switch (opcioMenu){
                case 0:
                    sortir = true;
                    break;
                case 1:
                    callSubMenu();
                    break;
                case 2:
                    actualitzarStock();
                    break;
                case 3:
                    generarComandes();
                    break;
                case 4:
                    analitzarComandes();
                    break;
                default:
                    System.out.println("--> Opció no válida, per sortir premi 0 <--");
            }
        }while(!sortir);
    }
    
    // Submenu
    static void callSubMenu() throws SQLException, IOException{
        
        do{
            System.out.println("");
            System.out.println("1.1- Llistar tots els productes");
            System.out.println("1.2- Consultar un producte");
            System.out.println("1.3- Alta de producte");
            System.out.println("1.4- Modifica el producte");
            System.out.println("1.5- Esborrar");
            System.out.println("1.6- Tornar");
            System.out.println("\nTRIA UNA OPCIÓ");
            
            int opcioMenu = GestorInventari.teclat.nextInt();
            switch (opcioMenu){
                case 0:
                    sortir = true;
                    break;
                case 1:
                    GestorInventari.llistarTotsProductes();
                    break;
                case 2:
                    GestorInventari.consultarUnProducte();
                    break;
                case 3:
                    GestorInventari.altaProducte();
                    break;
                case 4:
                    GestorInventari.modificarProducte();
                    break;
                case 5:
                   GestorInventari.borrarProducte();
                    break;
                case 6:
                    callMenu();
                    break;
                default:
                    System.out.println("--> Opció no válida per tornar enrere premi 6 <-- \n--> Per sortir premi 0 <--");
            }
        }while(!sortir);
    }
}
