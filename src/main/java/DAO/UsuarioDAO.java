/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Util.FabricaConexao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Papel;
import modelo.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class UsuarioDAO implements Serializable {

    @Inject
    private FabricaConexao fabrica;

    @Inject
    PapelDAO papelDAO;

    @Inject
    GastoDAO gastoDAO;

    public void criarUsuario(Usuario usuario) throws ClassNotFoundException, SQLException {

        ArrayList<String> listaSQLs = new ArrayList();

        if (usuario.getGastos() == null) {

            usuario.setGastos(new ArrayList<Gasto>());
        }

        String sql1 = "INSERT INTO tb_usuario (email, LOGIN, NOME, PASSWORD, PAPEL_IDPAPEL)"
                + " VALUES ('" + usuario.getEmail() + "', '" + usuario.getLogin() + "', "
                + "'" + usuario.getPassword() + "', " + usuario.getPapel().getIdPapel() + ");";
        listaSQLs.add(sql1);

        for (Gasto gasto : usuario.getGastos()) {

            String sql2 = "INSERT INTO tb_usuario_tb_gasto((Usuario_IDUSUARIO, gastos_ID_GASTO)"
                    + "VALUES(" + usuario.getIdUsuario() + ", " + gasto.getId_gasto() + ");";

            listaSQLs.add(sql2);

        }

        if (usuario.getPapel() != null) {

            String sql3 = "INSERT INTO tb_papel_tb_usuario (Papel_IDPAPEL, usuario_IDUSUARIO)"
                    + "VALUES(" + usuario.getPapel().getIdPapel() + ", " + usuario.getIdUsuario() + ");";
            listaSQLs.add(sql3);

            fabrica.executaBatchUpdate(listaSQLs);

        }

    }

    public void editarUsuario(Usuario usuario) throws ClassNotFoundException, SQLException {

        ArrayList<String> listaSQLs = new ArrayList();

        String sql1 = "UPDATE tb_usuario "
                + "SET email = '" + usuario.getEmail()
                + "', LOGIN = '" + usuario.getLogin()
                + "', NOME =  '" + usuario.getNome()
                + "', PASSWORD = '" + usuario.getPassword()
                + "', PAPEL_IDPAPEL = " + usuario.getPapel().getIdPapel()
                + "WHERE id_usuario = " + usuario.getIdUsuario();

        listaSQLs.add(sql1);

        String sql2 = "UPDATE tb_papel_tb_usuario"
                + "SET Papel_IDPAPEL =  " + usuario.getPapel().getIdPapel()
                + "WHERE usuario_IDUSUARIO = " + usuario.getIdUsuario();

        listaSQLs.add(sql2);

        String sql3 = "DELETE FROM tb_usuario_tb_gasto "
                + "WHERE Usuario_IDUSUARIO  = " + usuario.getIdUsuario() + ";";
        listaSQLs.add(sql3);

        List<Gasto> gastosAtuais = usuario.getGastos();

        for (Gasto gasto : gastosAtuais) {

            String sql4 = "INSERT INTO tb_usuario_tb_gasto(Usuario_IDUSUARIO, "
                    + "gastos_ID_GASTO) VALUES (+" + usuario.getIdUsuario() + ", "
                    + gasto.getId_gasto() + ");";
            listaSQLs.add(sql4);

        }

        fabrica.executaBatchUpdate(listaSQLs);

    }



public void removerUsuario(int id) throws SQLException, ClassNotFoundException {

        ArrayList<String> listaSQLs = new ArrayList();


            String sql1 = "DELETE FROM tb_usuario_tb_gasto "
                        + " WHERE id_usuario = "+id+";";
            
            listaSQLs.add(sql1);
            
            String sql2 = "DELETE FROM tb_papel_tb_usuario WHERE usuario_IDUSUARIO = "
                    + id + ";";
            
            listaSQLs.add(sql2);
            
             String sql3 = "DELETE FROM tb_gasto WHERE USUARIO_IDUSUARIO = "
                    + id + ";";
            
            listaSQLs.add(sql3);
            
              String sql4 = "DELETE FROM tb_usuario WHERE IDUSUARIO = "
                    + id + ";";
            
            listaSQLs.add(sql4);
            
            fabrica.executaBatchUpdate(listaSQLs);
    }

    public List<Usuario> consultaUsuarios() throws ClassNotFoundException, SQLException {
   
        String sql = "SELECT * FROM tb_usuario;";
        
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        return this.extrairListaUsuariosResultSet(rs);
        
    
    }


    public Usuario findUsuario(int id) throws ClassNotFoundException, SQLException {


        String sql = "SELECT * FROM tb_usuario WHERE id_usuario = "
                +id+";";
        
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Usuario usuario = this.extraiUsuarioResultSet(rs);
        rs.close();
        
        return usuario;

    }


    public Usuario findByLoginSenha(String login, String senha) throws ClassNotFoundException, SQLException {

            String sql = "SELECT * FROM tb_usuario WHERE LOGIN LIKE '"
                    + login + "' AND PASSWORD LIKE '" + senha + "'";

            ResultSet rs = fabrica.executaQuerieResultSet(sql);
            Usuario usuario  = this.extraiUsuarioResultSet(rs);
            rs.close();
            
            return usuario;

    }
    
    

    public Usuario findByLogin(String login) throws ClassNotFoundException, SQLException {
    
    
        String sql = "SELECT * FROM tb_usuario WHERE LOGIN LIKE '"
                + login + "'";

        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Usuario usuario = this.extraiUsuarioResultSet(rs);
        rs.close();
        
        return usuario;

    }


    public Usuario extraiUsuarioResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(rs.getInt("IDUSUARIO"));
        usuario.setEmail("EMAIL");
        usuario.setLogin(rs.getString("LOGIN"));
        usuario.setPassword(rs.getString("password"));

        Papel papel = papelDAO.findPapelByUsuario(usuario.getIdUsuario());
        usuario.setPapel(papel);

        List<Gasto> listagasto = gastoDAO.listaGastosByUsuario(usuario.getIdUsuario());

        usuario.setGastos(listagasto);

        return usuario;
    }

    public List<Usuario> extrairListaUsuariosResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        List<Usuario> listaUsuarios = new ArrayList();

        while (rs.next()) {
            listaUsuarios.add(this.extraiUsuarioResultSet(rs));
        }
        rs.close();

        return listaUsuarios;

    }

}
