
package gestorinventari;

import static gestorinventari.CallMenu.callMenu;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorInventari {
    
    //Variables Globales
    static Scanner teclat = new Scanner(System.in);
    static boolean sortir = false;
    static Connection connectionBD = null;
    
    public static void main(String[] args) {
        try {
            conectionDB();  //Conectar con DDBB
            callMenu();//Iniciar menú
        } catch (SQLException ex) {
            System.out.println("Ha hagut algún problema amb la conexió a la BD");
            ex.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ha hagut algún problema amb la aplicació");
            e.printStackTrace();
        }
        
        
    }//end main 
    
    /*************************************************************/
    // Menu
    
    //DB conection
    static void conectionDB() throws SQLException{
        //String server = "jdbc:mysql://192.168.18.55:3306/";   //Insti
        String server = "jdbc:mysql://192.168.1.55:3306/";      //Casa
        String schema = "db_projecte";
        String user = "proinv";
        String pass = "12345";
        connectionBD = DriverManager.getConnection(server + schema, user, pass);
        //Podem comprobar la conexió desde aquí o desde on es crida el mètode
        /*
        try {
            connectionBD = DriverManager.getConnection(server + schema, user, pass);
        } catch (SQLException ex) {
            System.out.println("Ha hagut algún problema amb la conexió a la BD");
            ex.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ha hagut algún problema amb la aplicació");
            e.printStackTrace();
        }
        */
    }
    //Llistar els productes
    static void llistarTotsProductes() throws SQLException{
        //Preparem la consulta
        String consulta = "SELECT * FROM productes ORDER BY nom;";
        PreparedStatement ps = connectionBD.prepareStatement(consulta);
        //Llencem consulta
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            System.out.println("--------------");
            System.out.println("Id: " + rs.getInt("id"));
            System.out.println("Nom: " + rs.getString("nom"));
            System.out.println("Any: " + rs.getInt("any"));
            System.out.println("Tipus: " + rs.getString("tipus"));
            System.out.println("Preu: " + rs.getDouble("preu"));
            System.out.println("Descripció: " + rs.getString("desc"));
        }
    }
    //Donar d'alta un producte
    static void altaProducte() throws SQLException{
        System.out.println("Alta nou Producte");
        System.out.println("Emplena totes les dades:");
        boolean continua;
        teclat.nextLine();
        
            /************ NOM *************/
            
        System.out.println("NOM:");
        String nom = teclat.nextLine();
        
            /************ ANY *************/
            
        int any = 0;
        do{
            try {
                System.out.println("ANY:");
                any = teclat.nextInt();
                while (String.valueOf(any).length() < 4 || String.valueOf(any).length() > 4 || String.valueOf(any).length() == 0 ) {                    
                    System.out.println("L'any ha de tenir 4 caracters");
                    any = teclat.nextInt();
                }
                continua = true;
            }catch (InputMismatchException ex){
                System.out.println("Només pot ingresar números enters");
                teclat.next();
                continua = false;
            }
        }while (!continua);
        
        teclat.nextLine(); // Després de llegir un Int, s'ha d'afegir aquesta línea
        
            /************ DESCRIPCIÓ *************/
        
        System.out.println("DESCRIPCIÓ (max 250 caracters)");
        String descripcio = teclat.nextLine();
        while(descripcio.length() > 250 || descripcio.length() == 0){
            System.out.println("La descripció es massa llarga, no pot exedir de 250 caracters.");
            descripcio = teclat.nextLine();
        }
        
            /************ TIPUS *************/
        
        String tipus = "";
        int opcioMenu;
        do {
            try {
                while ("".equals(tipus)) {   
                    System.out.println("TIPUS (Escull una opció)");
                    System.out.println("1- DVD doble capa");
                    System.out.println("2- Caixa slim");
                    System.out.println("3- Caixa metàl·lica");
                    System.out.println("4- DVD Amb llibret");
                    opcioMenu = teclat.nextInt();
                    continua = true;

                    switch(opcioMenu){
                        case 1:
                            System.out.println("Tipus: DVD doble capa");
                            tipus = "doble capa";
                            break;
                        case 2:
                            System.out.println("Tipus: Caixa slim");
                            tipus = "Caixa slim";

                            break;
                        case 3:
                            System.out.println("Tipus: Caixa metàl·lica");
                            tipus = "Caixa metàl·lica";
                            break;
                        case 4:
                            System.out.println("Tipus: DVD Amb llibret");
                            tipus = "DVD Amb llibret";
                            break;
                        default:
                            System.out.println("Opció incorrecta");
                            tipus = "";
                            break;
                    }
                }
            } catch(InputMismatchException ex){
                System.out.println("Només pots escollir entre els valors mostrats");
                teclat.next();
                continua = false;
            }
        }while (!continua);
        
        teclat.nextLine();
        
            /************ PREU *************/
            
        double preu = 0.0;  
        do{
            try{
                System.out.println("PREU");
                preu = teclat.nextDouble();
                continua = true;
            } catch (InputMismatchException ex){
                System.out.println("Valor incorrecte, només valors numèrics");
                System.out.println("Recorda utilitzar ',' (coma) en comptes de '.' (punt) per els decimals");
                teclat.next();
                continua = false;
            }
        } while (!continua);
        
        teclat.nextLine();
        
        /************ STOCK *************/
        int stock = 1;
        do {  
            try {
                System.out.println("STOCK");
                stock = teclat.nextInt();
                continua = true;
            } catch (InputMismatchException ex) {
                System.out.println("Només pots insertar números enters");
                teclat.next();
                continua = false;
            }
            
        } while (!continua);
        
        teclat.nextLine();
        
        String newProduct = "INSERT INTO productes(`nom`,`any`,`desc`,`preu`,`tipus`,`stock`) VALUES (?,?,?,?,?,?);";
        PreparedStatement insert = null;
        
        try {
            insert = connectionBD.prepareStatement(newProduct);
            insert.setString(1, nom);    //Nom
            insert.setInt(2, any);           //Any
            insert.setString(3, descripcio);      //Descripció
            insert.setDouble(4, preu);      //Preu
            insert.setString(5, tipus);      //Tipus
            insert.setInt(6, stock);      //Stock
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        if (insert.executeUpdate() != 0) {
            System.out.println("Nou producte donat d'alta:");
            System.out.println("--------------------------");
            System.out.println("Nom: " + nom);
            System.out.println("Any: " + any);
            System.out.println("Descripció: " + descripcio);
            System.out.println("Tipus: " + tipus);
            System.out.println("Preu: " + preu);
            System.out.println("Stock: " + stock);
            System.out.println("--------------------------");
        }else {
            System.out.println("Error");
        }
    }
    
    static void modificarProducte() throws SQLException{
        
        System.out.println("Modificar un producte\n Quin producte vol editar?");
        teclat.nextLine();
            
        System.out.println("Escriu el NOM:");
        String modificarProd = teclat.nextLine();

        String consulta = "SELECT * FROM productes WHERE nom LIKE '%" + modificarProd + "%';";
        PreparedStatement ps = connectionBD.prepareStatement(consulta);
        //Llencem consulta
        ResultSet rs = ps.executeQuery();
        //Contem els registres i els mostrem
        int count = 0;
        int idProAModificar = 0;
        while (rs.next()){
            count++;
            System.out.println("--------------");
            System.out.print("Id: " + rs.getInt("id") + " | ");
            idProAModificar = rs.getInt("id");
            System.out.print("Nom: " + rs.getString("nom") + " | ");
            System.out.print("Any: " + rs.getInt("any") + " | ");
            System.out.print("Tipus: " + rs.getString("tipus") + " | ");
            System.out.print("Preu: " + rs.getDouble("preu") + "\n");
        }
        System.out.println("--------------");
        
        if(count > 1){
            System.out.println("S'han trobat " + count + " registres.\n");

            //Preguntem per l'ID a modificar
            System.out.println("Indica el ID del porducte a modificar");
            System.out.println("ID:");
            idProAModificar = teclat.nextInt();
            teclat.nextLine();
        }
        /*if (count == 1){
            if(rs.next()){
                idProAModificar = rs.getInt("id");
            }
            teclat.nextLine();
        }*/
        
        //Indiquem el producte escollit i el guardem per poder-lo modificar
        System.out.println("El registre a modificar es " + idProAModificar);
        String productePerModificar = "SELECT * FROM productes WHERE id =" + idProAModificar + ";";
        ps = connectionBD.prepareStatement(productePerModificar);
        rs = ps.executeQuery();
        
        //Guardem els valos en variables, les inicialitzem i després els hi donem el valor de l'ultim Resultset
        String nom = ""; 
        String descripcio = ""; 
        String tipus = "";
        int id = 00;
        int any = 00;
        int stock = 00;
        double preu = 00;
        
        while (rs.next()){
            id = rs.getInt("id");
            nom = rs.getString("nom");
            descripcio = rs.getString("desc");
            tipus = rs.getString("tipus");
            any = rs.getInt("any");
            stock = rs.getInt("stock");
            preu = rs.getDouble("preu");
        }
        nom =nom;
        
        boolean continua = false;
        String modificarCamp = "no";
        
        //Començem a preguntar quins camps vol modificar i quin será el nou valor
        
            /************ NOM *************/
            
        teclat.nextLine();
        System.out.println("Modificar NOM? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){
            teclat.nextLine();
            System.out.println("Nou camp NOM:");
            nom = teclat.nextLine();
        }
        
            /************ ANY *************/
            
        System.out.println("Modificar ANY? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){
            do{
                try {
                    System.out.println("Nou camp ANY:");
                    any = teclat.nextInt();
                    while (String.valueOf(any).length() < 4 || String.valueOf(any).length() > 4 || String.valueOf(any).length() == 0 ) {                    
                        System.out.println("L'any ha de tenir 4 caracters");
                        any = teclat.nextInt();
                    }
                    continua = true;
                }catch (InputMismatchException ex){
                    System.out.println("Només pot ingresar números enters");
                    teclat.next();
                    continua = false;
                }
            }while (!continua);
        }
        teclat.nextLine();
        
            /************ DESCRIPCIÓ *************/
            
        System.out.println("Modificar DESCRIPCIÓ? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){
            teclat.nextLine();
            System.out.println("Nou camp DESCRIPCIÓ (max 250 caracters)");
            descripcio = teclat.nextLine();
            while(descripcio.length() > 250 || descripcio.length() == 0){
                System.out.println("La descripció es massa llarga, no pot exedir de 250 caracters.");
                descripcio = teclat.nextLine();
            }    
        }    
            /************ TIPUS *************/
        
        System.out.println("Modificar TIPUS? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){
            tipus = "";
            int opcioMenu;
            do {
                try {
                    while ("".equals(tipus) ) {   
                        System.out.println("Nou camp TIPUS (Escull una opció)");
                        System.out.println("1- DVD doble capa");
                        System.out.println("2- Caixa slim");
                        System.out.println("3- Caixa metàl·lica");
                        System.out.println("4- DVD Amb llibret");
                        opcioMenu = teclat.nextInt();
                        continua = true;

                        switch(opcioMenu){
                            case 1:
                                tipus = "DVD doble capa";
                                break;
                            case 2:
                                tipus = "Caixa slim";

                                break;
                            case 3:
                                tipus = "Caixa metàl·lica";
                                break;
                            case 4:
                                tipus = "DVD Amb llibret";
                                break;
                            default:
                                System.out.println("Opció incorrecta");
                                tipus = "";
                                break;
                        }
                    }
                } catch(InputMismatchException ex){
                    System.out.println("Només pots escollir entre els valors mostrats");
                    teclat.next();
                    continua = false;
                }
            }while (!continua);
        }
        teclat.nextLine();
        
            /************ PREU *************/
            
        System.out.println("Modificar el PREU? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){
            do{
                try{
                    System.out.println("PREU");
                    preu = teclat.nextDouble();
                    continua = true;
                } catch (InputMismatchException ex){
                    System.out.println("Valor incorrecte, només valors numèrics");
                    System.out.println("Recorda utilitzar ',' (coma) en comptes de '.' (punt) per els decimals");
                    teclat.next();
                    continua = false;
                }
            } while (!continua);
        }
        teclat.nextLine();
        
            /************ STOCK *************/
            
        System.out.println("Modificar l'STOCK? (Si o No)");
        modificarCamp = teclat.next();
        if(!"No".equals(modificarCamp) && !"no".equals(modificarCamp) && !"n".equals(modificarCamp)){    
            do {  
                try {
                    System.out.println("STOCK");
                    stock = teclat.nextInt();
                    continua = true;
                } catch (InputMismatchException ex) {
                    System.out.println("Només pots insertar números enters");
                    teclat.next();
                    continua = false;
                }

            } while (!continua);
        }
        teclat.nextLine();
        
        //creem la consulta
        productePerModificar = "UPDATE productes SET nom = ?, `desc` = ?, any = ?, preu = ?, tipus = ?, stock = ? where id = ?;";
        
        try {
            ps = connectionBD.prepareStatement(productePerModificar);
            ps.setString(1, nom);
            ps.setString(2, descripcio);
            ps.setInt(3, any);
            ps.setDouble(4, preu);
            ps.setString(5, tipus);
            ps.setInt(6, stock);
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (ps != null)
                System.out.println("\nS´ha modificat correcatment:");
                System.out.println("ID: " + id);
                System.out.println("Nom: " + nom);
                System.out.println("Any: " + any);
                System.out.println("Descripció: " + descripcio);
                System.out.println("Tipus: " + tipus);
                System.out.println("Preu: " + preu);
                System.out.println("Stock: " + stock);
                try {
                    ps.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }
    
    static void borrarProducte() throws SQLException{
        System.out.println("Esborrar un producte\n Quin producte vol eliminar?");
        teclat.nextLine();
            
        System.out.println("Escriu el NOM:");
        String elimianrProd = teclat.nextLine();

        String consulta = "SELECT * FROM productes WHERE nom LIKE '%" + elimianrProd + "%';";
        PreparedStatement ps = connectionBD.prepareStatement(consulta);
        //Llencem consulta
        ResultSet rs = ps.executeQuery();
        //Contem els registres i els mostrem
        int count = 0;
        int idProAEliminar = 0;
        String nomProAEliminar = "";
        while (rs.next()){
            count++;
            System.out.println("--------------");
            System.out.print("Id: " + rs.getInt("id") + " | ");
            idProAEliminar = rs.getInt("id");
            System.out.print("Nom: " + rs.getString("nom") + " | ");
            nomProAEliminar = rs.getString("nom");
            System.out.print("Any: " + rs.getInt("any") + " | ");
            System.out.print("Tipus: " + rs.getString("tipus") + " | ");
            System.out.print("Preu: " + rs.getDouble("preu") + "\n");
        }
        System.out.println("--------------");
        
        if(count > 1){
            System.out.println("S'han trobat " + count + " registres.\n");

            //Preguntem per l'ID a modificar
            System.out.println("Indica el ID del porducte que vol eliminar");
            System.out.println("ID:");
            idProAEliminar = teclat.nextInt();
            teclat.nextLine();
        }
        
        System.out.println("Estás segur d'esborrar '" + nomProAEliminar + "' amb ID " + idProAEliminar + "?");
        String confirmEliminar = teclat.next();
        if(!"No".equals(confirmEliminar) && !"no".equals(confirmEliminar) && !"n".equals(confirmEliminar)){
            String productePerEliminar = "DELETE FROM productes WHERE id = ?;";
            try{
                ps = connectionBD.prepareStatement(productePerEliminar);
                ps.setInt(1,idProAEliminar );
                ps.executeUpdate();
            }catch(SQLException sqle){
                sqle.printStackTrace();
            }finally {
                if (ps != null)
                    System.out.println("\nProducte eliminat correcatment:");
                    try {
                        ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    
    }
    
    
}//end class
