/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.SQLException;

/**
 *
 * @author Emm
 */
public class CriarInfra {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        
        
        FabricaConexao fabrica = new FabricaConexao();
        
        fabrica.criaBaseDados();
        fabrica.criaInfraestrutura();
    
        
        
    }
    
    
    
    
    
}
