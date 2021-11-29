
package gestorinventari;

import static gestorinventari.CallMenu.callMenu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GestorInventari {
    
    //Variables Globales
    static Scanner teclat = new Scanner(System.in);
    static boolean sortir = false;
    static Connection connectionBD = null;
    static String pathPendents = "files/ENTRADES PENDENTS/";
    static String pathProcesades = "files/ENTRADES PROCESSADES/";
    static String pathComandes = "files/COMANDES/";
    
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

    /***** 1- MENU ******/
    
    //DB conection
    static void conectionDB() throws SQLException{
        //String server = "jdbc:mysql://192.168.18.55:3306/";   //Insti
        String server = "jdbc:mysql://192.168.1.55:3306/";      //Casa
        String schema = "db_projecte";
        String user = "proinv";
        String pass = "12345";
        connectionBD = DriverManager.getConnection(server + schema, user, pass);
        //Podem comprobar la conexió desde aquí o desde on es crida el mètode
        //Si ho fem desde on es crida, s'haurà d'afegir "throws SQLException" al nom del mètode
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
        PreparedStatement ps = null;
        try{
            //Preparem la consulta
            String consulta = "SELECT * FROM productes ORDER BY nom;";
            ps = connectionBD.prepareStatement(consulta);
            //Llencem consulta
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                System.out.println("--------------");
                System.out.print("Id: " + rs.getInt("id") + " | ");
                System.out.print("Nom: " + rs.getString("nom") + " | ");
                System.out.print("Any: " + rs.getInt("any") + " | ");
                System.out.print("Tipus: " + rs.getString("tipus") + " | ");
                System.out.print("Preu: " + rs.getDouble("preu") + " | ");
                System.out.println("Descripció: " + rs.getString("desc") + " | ");
            }
            System.out.println("--------------");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
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
            System.out.print("Nom: " + nom + " | ");
            System.out.print("Any: " + any + " | ");
            System.out.print("Tipus: " + tipus + " | ");
            System.out.print("Preu: " + preu + " | ");
            System.out.println("Stock: " + stock + " | ");
            System.out.println("Descripció: " + descripcio);
            System.out.println("--------------------------");
        }else {
            System.out.println("Error");
        }
    }
    //Consultar un producte
    static void consultarUnProducte() throws SQLException{
        System.out.println("Consultar un producte\n");
        teclat.nextLine();
        
        boolean continua = false;
        String consulta = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int consultarProd = 0;
        do {  
            try {
                System.out.println("Si coneixes l'ID introdueix-la, si no, premeu 0:");
                consultarProd = teclat.nextInt();
                continua = true;
            } catch (InputMismatchException ex) {
                System.out.println("Només pots insertar números enters");
                teclat.next();
                continua = false;
            }
            
        } while (!continua);
        teclat.nextLine();
        if(consultarProd == 0){
            System.out.println("Escriu el nom del producte a consultar:");
            String consultarNomProd = teclat.nextLine();
            consulta = "SELECT * FROM productes WHERE nom LIKE '%" + consultarNomProd + "%';";
            
            ps = connectionBD.prepareStatement(consulta);
            rs=ps.executeQuery();
            int count = 0;
            while (rs.next()){
                count++;
                System.out.println("--------------");
                System.out.println("Id: " + rs.getInt("id") + " ---> " + rs.getString("nom"));
            }
            System.out.println("--------------");
            System.out.println("\nS'han trobat " + count + " registres.\n");
            System.out.println("Indica el ID del porducte a consultar");
            System.out.println("ID:");
            consultarProd = teclat.nextInt();
            teclat.nextLine();
        }
        
        consulta = "SELECT * FROM productes WHERE id = " + consultarProd + ";";
        try{
            ps = connectionBD.prepareStatement(consulta);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println("\n--------------");
                System.out.println("Id: " + rs.getInt("id"));
                System.out.println("Nom: " + rs.getString("nom"));
                System.out.println("Any: " + rs.getInt("any"));
                System.out.println("Descripció: " + rs.getString("desc"));
                System.out.println("Tipus: " + rs.getString("tipus"));
                System.out.println("Preu: " + rs.getDouble("preu"));
                System.out.println("Stock: " + rs.getString("stock"));
                System.out.println("--------------");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }
    //Modificar un producte
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
        
        //Si hi ha més d'un registre preguntem per l'ID a modificar
        if(count > 1){
            System.out.println("S'han trobat " + count + " registres.\n");
            System.out.println("Indica el ID del porducte a modificar");
            System.out.println("ID:");
            idProAModificar = teclat.nextInt();
            
        }
        
        //Indiquem el producte escollit i el guardem per poder-lo modificar
        //System.out.println("El registre a modificar es " + idProAModificar);
        String productePerModificar = "SELECT * FROM productes WHERE id =" + idProAModificar + ";";
        ps = connectionBD.prepareStatement(productePerModificar);
        rs = ps.executeQuery();
        
        //Guardem els valos en variables, les inicialitzem i després els hi donem el valor de l'ultim ResultSet
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
        
        boolean continua = false; //Boolea per avaluar els Try-Catch
        String modificarCamp = "no";
        
        //Començem a preguntar quins camps vol modificar i quin será el nou valor
        
            /************ NOM *************/
        System.out.println("El registre a modificar es el " + id + " -> " + nom + " (" + any + ").");    
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
                    while (tipus.equals("") ) {   
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
            if (ps != null){
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
    }
    //Borrar un producte
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
        
        //Si hi ha més d'un registre preguntem per l'ID a eliminar
        if(count > 1){
            System.out.println("S'han trobat " + count + " registres.\n");
            System.out.println("Indica el ID del porducte que vol eliminar");
            System.out.println("ID:");
            idProAEliminar = teclat.nextInt();
            teclat.nextLine();
        }
        //Confirmació i executar consulta
        System.out.println("Estás segur d'esborrar '" + nomProAEliminar + "' amb ID " + idProAEliminar + "?");
        String confirmEliminar = teclat.next();
        
        if(!"No".equals(confirmEliminar) && !"no".equals(confirmEliminar) && !"n".equals(confirmEliminar)){
            //Primer borrem les associacións del producte
            String dependeciaCatEliminar = "DELETE FROM pertany WHERE id_prod = ?;";
            String dependeciaSemEliminar = "DELETE FROM semblaça WHERE id_prod_one = ? OR id_prod_two = ?;";
            //Despres borrem el producte
            String productePerEliminar = "DELETE FROM productes WHERE id = ?;";
            try{
                //borrar dependecies
                ps = connectionBD.prepareStatement(dependeciaCatEliminar);
                ps.setInt(1,idProAEliminar );
                ps.executeUpdate();
                ps = connectionBD.prepareStatement(dependeciaSemEliminar);
                ps.setInt(1,idProAEliminar );
                ps.setInt(2,idProAEliminar );
                ps.executeUpdate();
                //Borrar productes
                ps = connectionBD.prepareStatement(productePerEliminar);
                ps.setInt(1,idProAEliminar );
                ps.executeUpdate();
            }catch(SQLException sqle){
                sqle.printStackTrace();
            }finally {
                if (ps != null){
                    System.out.println("\nProducte eliminat correcatment:");
                    try {
                        ps.close();
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    /***** 2- MENU ******/
    
    static void actualitzarStock() throws SQLException, IOException{
        
        System.out.println("ACTUALITZAR STOCK");
        File fitxer = new File(pathPendents); //pathPendents en global
        
        if(fitxer.isDirectory()){
            File[] fitxers = fitxer.listFiles();
            if(fitxers.length == 0) {
                System.out.println("\nNo hi ha arxius per actualitzar");
            }else{
                //Recorrem tots els fitxers de la carpeta ENTRADES PENDENTS
                for(int i = 0; i < fitxers.length; i++){
                    //Llegir linia a liniea i actualitzar fitxer a la Base de dades
                    //System.out.println(fitxers[i].getName());
                    //li passo cada un dels fichers a actualitzar([i]) i a moure([i])
                    actualitzarFitxerDB(fitxers[i]);
                    moureFitxerDB(fitxers[i]);
                }
            }
        }
        
    }
    
    static void actualitzarFitxerDB(File fitxer) throws FileNotFoundException, IOException{
        
        FileReader fr = new FileReader(fitxer);             //Llegeix caracter a caracter cada línea del fitxer
        BufferedReader buffer = new BufferedReader(fr);     //Junta els caracters del FileReader per crear una cadena de caracters
        
        String linea;
        while(( linea = buffer.readLine() ) != null){       //comprobar si la líena es diferent a null (hi ha dades al fitxer)
            //System.out.println(linea);                    // retorna el format del document(idproducte:stock)(int:int)
            int numProducte = Integer.parseInt(linea.substring(0,linea.indexOf(":")));
            int numUnitats =  Integer.parseInt(linea.substring(linea.indexOf(":") + 1));
            System.out.println("ID: " + numProducte + " STOCK: " + numUnitats);    
            actualitzarStockFitxer(numProducte,numUnitats);
        }
        //Tamquem el FileReader i el BufferReader per poder seguir utilitzant el fitxer (moure'l a un altre carpeta)
        buffer.close();
        fr.close();
    }
    
    static  void actualitzarStockFitxer(int idProducte,int stock) {
        
        String updateStock = "UPDATE productes SET stock = stock + ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connectionBD.prepareStatement(updateStock);
            ps.setInt(1, stock);        //Stock  
            ps.setInt(2, idProducte);   //Id producte
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (ps != null){
                System.out.println("\nL'estock s´ha actualitzat correcatment:");
                try {
                    ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }
    
    static void moureFitxerDB(File fitxer) throws IOException{
        
        FileSystem sistemaDeFitxers = FileSystems.getDefault();
        Path origen = sistemaDeFitxers.getPath(pathPendents + fitxer.getName());    //pathPendents en global
        Path desti = sistemaDeFitxers.getPath(pathProcesades + fitxer.getName());   //pathProcesades en global
        
        Files.move(origen,desti,StandardCopyOption.REPLACE_EXISTING);   //Carpeta d'origen, capeta de destí, remplaçar si existeix
        System.out.println("El fitxer " + fitxer.getName() + " s'ha mogut a ENTRADES PROCESSADES");
    }
   
    /***** 3- MENU ******/
    
    static void generarComandes() throws SQLException, IOException{
        
        String dadesEmpresa = "";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String prodMenys20 = "select pro.id, pro.nom, pro.stock, prov.id, prov.nom \n" +
                                "from productes as pro \n" +
                                "inner join porta as por on pro.id = por.id_prod\n" +
                                "inner join proveidor as prov on prov.id = por.id_prov \n" +
                                "where pro.stock < 20 order by prov.nom;";
            ps = connectionBD.prepareStatement(prodMenys20);
            rs = ps.executeQuery();
            
            String proveidor = "";
            if(rs.next()){
                proveidor = rs.getString("prov.nom");
            }
            
            while (rs.next()){
                if(!proveidor.equals(rs.getString("prov.nom"))){
                    proveidor = rs.getString("prov.nom");
                    System.out.println("------------");
                    System.out.println("Canviat de proveidor a: " + proveidor); 
                }
                System.out.println("--------------");
                System.out.print("Id: " + rs.getInt("pro.id") + " | ");
                System.out.print("Nom: " + rs.getString("pro.nom") + " | ");
                System.out.print("Nom proveidor: " + rs.getString("prov.nom") + " | ");
                System.out.println("Stock: " + rs.getInt("pro.stock"));
            }
            System.out.println("--------------");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
           /* if (ps != null){
                try {
                    ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }*/
        }
        
        //crearComandes(rs);
    }
    
    static void crearComandes(ResultSet rs) throws SQLException, IOException{
        
        //File fitxer = new File(pathComandes);
        
        while (rs.next()) {
            System.out.println("AAAAAAAAA");
            System.out.println(rs.getInt("pro.id"));
            System.out.println(rs.getString("pro.nom"));
            //FileWriter fr = new FileWriter(fitxer);
            //BufferedWriter comanda = new BufferedWriter(fr); 
            //String nomProveidor = rs.getString("prov.nom");
            
            //if(nomProveidor == null ? nomProveidor != null : !nomProveidor.equals(nomProveidor)){
            //    System.out.println(nomProveidor);
            //}
            
            //comanda.write(Integer.toString(rs.getInt("pro.id")) + ", ");
            //comanda.write(rs.getString("pro.nom") + ", ");
            //comanda.write(rs.getString("prov.nom") + ", ");
            //comanda.write(rs.getInt("pro.stock") + 82 );
        }
    
    }
        
        
    
    static void analitzarComandes(){
        System.out.println("Per fer encara");
    }
    
    
}//end class
