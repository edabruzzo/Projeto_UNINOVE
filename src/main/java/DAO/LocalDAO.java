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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Projeto;
import modelo.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Local;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class LocalDAO implements Serializable {

    @Inject
    GastoDAO gastoDAO;

    @Inject
    ProjetoDAO projetoDAO;

    @Inject
    FabricaConexao fabrica;

    public void create(Local local) throws SQLException, ClassNotFoundException {
    
        
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "INSERT INTO tb_local(NOME, PROJETO_ID_PROJETO) "
                + "VALUES ('"+local.getNome()
                + "', "+local.getProjeto().getId_projeto()+");";
        
        listaSQLs.add(sql1);
        
        String sql2 = "INSERT INTO tb_projeto_tb_local "
                + "(Projeto_ID_PROJETO, locais_ID_LOCAL) "
                + "VALUES("+local.getProjeto().getId_projeto()+", "
                + " (SELECT last_insert_id() FROM tb_local))";
        
        listaSQLs.add(sql2);
        
        
        for (Gasto gasto : local.getGastos()){
        
         String sql3 = "INSERT INTO tb_local_tb_gasto ((Local_ID_LOCAL, "
                 + "gastos_ID_GASTO) VALUES ((SELECT last_insert_id() FROM tb_local), "
                 + gasto.getId_gasto()+");";
         listaSQLs.add(sql3);
            
        }
        
        
        fabrica.executaBatchUpdate(listaSQLs);
        
        
    }

    public void edit(Local local) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Local persistentLocal = em.find(Local.class, local.getId_local());
            Projeto projetoOld = persistentLocal.getProjeto();
            Projeto projetoNew = local.getProjeto();
            List<Gasto> gastosOld = persistentLocal.getGastos();
            List<Gasto> gastosNew = local.getGastos();
            if (projetoNew != null) {
                projetoNew = em.getReference(projetoNew.getClass(), projetoNew.getId_projeto());
                local.setProjeto(projetoNew);
            }
            List<Gasto> attachedGastosNew = new ArrayList<Gasto>();
            for (Gasto gastosNewGastoToAttach : gastosNew) {
                gastosNewGastoToAttach = em.getReference(gastosNewGastoToAttach.getClass(), gastosNewGastoToAttach.getId_gasto());
                attachedGastosNew.add(gastosNewGastoToAttach);
            }
            gastosNew = attachedGastosNew;
            local.setGastos(gastosNew);
            local = em.merge(local);
            if (projetoOld != null && !projetoOld.equals(projetoNew)) {
                projetoOld.getLocais().remove(local);
                projetoOld = em.merge(projetoOld);
            }
            if (projetoNew != null && !projetoNew.equals(projetoOld)) {
                projetoNew.getLocais().add(local);
                projetoNew = em.merge(projetoNew);
            }
            for (Gasto gastosOldGasto : gastosOld) {
                if (!gastosNew.contains(gastosOldGasto)) {
                    gastosOldGasto.setLocal(null);
                    gastosOldGasto = em.merge(gastosOldGasto);
                }
            }
            for (Gasto gastosNewGasto : gastosNew) {
                if (!gastosOld.contains(gastosNewGasto)) {
                    Local oldLocalOfGastosNewGasto = gastosNewGasto.getLocal();
                    gastosNewGasto.setLocal(local);
                    gastosNewGasto = em.merge(gastosNewGasto);
                    if (oldLocalOfGastosNewGasto != null && !oldLocalOfGastosNewGasto.equals(local)) {
                        oldLocalOfGastosNewGasto.getGastos().remove(gastosNewGasto);
                        oldLocalOfGastosNewGasto = em.merge(oldLocalOfGastosNewGasto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = local.getId_local();
                if (findLocal(id) == null) {
                    throw new NonexistentEntityException("The local with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();

            }
        }
    }

    public void destroy(int id) {

        String sql1 = "DELETE FROM tb_local WHERE "
        
        
    }

    public List<Local> findLocalEntities() throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM tb_local;";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        List<Local> listaLocais = new ArrayList();

        while (rs.next()) {

            listaLocais.add(extraiLocalResultSet(rs));

        }

        return listaLocais;
    }

    public Local findLocal(int id) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_local WHERE ID_LOCAL = " + id
                + ";";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Local local = new Local();

        local = extraiLocalResultSet(rs);

        return local;
    }

    public Local extraiLocalResultSet(ResultSet rs) throws SQLException {

        Local local = new Local();
        Projeto projeto = new Projeto();

        local.setId_local(rs.getInt("ID_LOCAL"));
        local.setNome(rs.getString("NOME"));

        local.setGastos(gastoDAO.listaGastosByLocal(rs.getInt("ID_LOCAL")));

        projeto = projetoDAO.findProjeto(rs.getInt("PROJETO_ID_PROJETO "));

        local.setProjeto(projeto);

        return local;
    }

    public List<Local> findLocalByProjeto(int id_projeto) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_local WHERE PROJETO_ID_PROJETO  = " + id_projeto + ";";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);

        Local local = new Local();

        local = extraiLocalResultSet(rs);

        return local;
    }

    
        public List<Local> extrairListaLocaisResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        List<Local> listaLocais = new ArrayList();

        while (rs.next()) {
            listaLocais.add(this.extraiLocalResultSet(rs));
        }
        rs.close();

        return listaLocais;

    }

    
    
    
}
