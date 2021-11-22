
package gestorinventari;

import java.sql.SQLException;

public class CallMenu {
    static boolean sortir = false;
    static void callMenu() throws SQLException{
        do {
            System.out.println("");
            System.out.println("******MENU GESTOR INVENTARI******");
            System.out.println("1. Gestionar productes");
            System.out.println("2. Actualitzar stock");
            System.out.println("3. Preparar comandes");
            System.out.println("4. analitzar les comandes");
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
                    System.out.println("Opció 2");
                    break;
                case 3:
                    System.out.println("Opció 3");
                    break;
                case 4:
                    System.out.println("Opció 4");
                    break;
                default:
                    System.out.println("--> Opció no válida, per sortir premi 0 <--");
            }
        }while(!sortir);
    }
    // Submenu
    static void callSubMenu() throws SQLException{
        do{
            System.out.println("");
            System.out.println("1.1- Llistar tots els productes");
            System.out.println("1.2- Alta de producte");
            System.out.println("1.3- Modifica el producte");
            System.out.println("1.4- Esborrar");
            System.out.println("1.5- Tornar");
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
                    GestorInventari.altaProducte();
                    break;
                case 3:
                    GestorInventari.modificarProducte();
                    break;
                case 4:
                   GestorInventari.borrarProducte();
                    break;
                case 5:
                    callMenu();
                    break;
                default:
                    System.out.println("--> Opció no válida per tornar enrere premi 5 <-- \n--> Per sortir premi 0 <--");
            }
        }while(!sortir);
    }
}
