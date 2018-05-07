package Default;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author internet
 */
public class FabricaConexao {

    private String URL;
    
    public void criaBaseDados() throws ClassNotFoundException{
        
            Class.forName("com.mysql.jdbc.Driver");
            String USER = "root";
            String PASSWORD = "root";
            this.setURL("jdbc:mysql://localhost:3306/controleFinanceiroUNINOVE");

            Connection conn = null;
        //STEP 3: Open a connection
        System.out.println("Conectando ao servidor com a seguinte URL : "+ URL);
       
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
    }
    
    
    public Connection criaConexao() throws ClassNotFoundException  {

            Class.forName("com.mysql.jdbc.Driver");
            String USER = "root";
            String PASSWORD = "root";
            this.setURL("jdbc:mysql://localhost:3306/controleFinanceiroUNINOVE");

            Connection conn = null;
        //STEP 3: Open a connection
        System.out.println("Conectando ao servidor com a seguinte URL : "+ URL);
       
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;

    }

    public void fecharConexao(Connection conn) {
        
        System.out.println("Fechando a conexão com o banco de dados");
        try {
            if (conn != null){
            conn.close();
           System.out.println("Conexão com o banco de dados fechada com sucesso");
            }

        } catch (SQLException ex) {
            Logger.getLogger(FabricaConexao.class.getName()).log(Level.SEVERE, null, ex);
           System.out.println("********ATENÇÃO ! Conexão com o banco de dados não foi fechada !");


        }
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}
