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

    public void create(Local local) {
        if (local.getGastos() == null) {
            local.setGastos(new ArrayList<Gasto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Projeto projeto = local.getProjeto();
            if (projeto != null) {
                projeto = em.getReference(projeto.getClass(), projeto.getId_projeto());
                local.setProjeto(projeto);
            }
            List<Gasto> attachedGastos = new ArrayList<Gasto>();
            for (Gasto gastosGastoToAttach : local.getGastos()) {
                gastosGastoToAttach = em.getReference(gastosGastoToAttach.getClass(), gastosGastoToAttach.getId_gasto());
                attachedGastos.add(gastosGastoToAttach);
            }
            local.setGastos(attachedGastos);
            em.persist(local);
            if (projeto != null) {
                projeto.getLocais().add(local);
                projeto = em.merge(projeto);
            }
            for (Gasto gastosGasto : local.getGastos()) {
                Local oldLocalOfGastosGasto = gastosGasto.getLocal();
                gastosGasto.setLocal(local);
                gastosGasto = em.merge(gastosGasto);
                if (oldLocalOfGastosGasto != null) {
                    oldLocalOfGastosGasto.getGastos().remove(gastosGasto);
                    oldLocalOfGastosGasto = em.merge(oldLocalOfGastosGasto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();

            }
        }
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

}
