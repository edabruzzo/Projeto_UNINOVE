/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import DAO.UsuarioDAO;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class CriptografiaSenha implements Serializable {

    private static final long serialVersionUID = -1070161903182392139L;
    
             
            
            public String convertStringToMd5(String valor) {
               MessageDigest mDigest;
               try { 
                      //Instanciamos o nosso HASH MD5, poder�amos usar outro como
                      //SHA, por exemplo, mas optamos por MD5.
                      mDigest = MessageDigest.getInstance("MD5");
                      
                      //Convert a String valor para um array de bytes em MD5
                      byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));
                      
                      //Convertemos os bytes para hexadecimal, assim podemos salvar
                      //no banco para posterior compara��o se senhas
                      StringBuffer sb = new StringBuffer();
                      for (byte b : valorMD5){
                             sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
                      }
   
                      return sb.toString();
                      
               } catch (NoSuchAlgorithmException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return null;
               } catch (UnsupportedEncodingException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return null;
               }

            }
            
            
            
            
         public void criptografaSenhaUsuario(Usuario usuario) throws Exception{
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
             
            String senhaCriptografada = convertStringToMd5(usuario.getPassword());
            usuario.setPassword(senhaCriptografada);
            usuarioDAO.editarUsuario(usuario);
                 
             }
            
        
             
             
             public void gerarNovaSenha(Usuario usuario) throws Exception {
               String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                             "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                             "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                             "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                             "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                             "W", "X", "Y", "Z" };
   
               String novaSenha = "";
   
               for (int x = 0; x < 7; x++) {
                      int j = (int) (Math.random() * carct.length);
                      novaSenha += carct[j];
   
               }
               String novaSenhaCriptografada = convertStringToMd5(novaSenha);
               usuario.setPassword(novaSenhaCriptografada);
               
               UsuarioDAO usuarioDAO = new UsuarioDAO();
               usuarioDAO.editarUsuario(usuario);
               
               enviarNovaSenhaEmailUsuario(novaSenha);
         }
             
             
           public void enviarNovaSenhaEmailUsuario(String novaSenha){
               
               //IMPLANTAR ENVIO DE E-MAIL
               System.out.println("A NOVA SENHA É : "+novaSenha);
               
           }
            
             
    }
    
    

        
    
    
    
    
    

