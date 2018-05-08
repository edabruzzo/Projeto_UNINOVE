/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.GastoDAO;
import DAO.LocalDAO;
import DAO.ProjetoDAO;
import DAO.UsuarioDAO;
import Util.ContextoJSF;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Gasto;
import modelo.Local;
import modelo.Projeto;
import modelo.Usuario;



/**
 *
 * @author Emm
 */
@Named
@SessionScoped
public class GastoBean implements Serializable{
    
    @Inject
    private Gasto gasto;
    @Inject 
    private Local local;
    @Inject
    private Usuario usuario;
    private int projetoID;
    private int localID;
    //este é o usuário logado
   
    private boolean canEdit = false;
    private int pesquisaByMes;
    private int pesquisaByAno;
    private List<Gasto> listaGastosFiltrados;

  
    private boolean mostrarTabelaPesquisas = false;
    private boolean houveErro = false;
    private Integer IDUsuarioPesquisado;
    private Gasto gastoEditado = new Gasto();
    private String consultaSQL;
    private boolean permiteConsultaSQL = false;
    private List<Gasto> listaGastosTotais;
    private double gastosFiltrados;
    private double gastosTotais;
   private List<Gasto> listaGastosPesquisa;
   private boolean mostraTotal = false;
   
   @Inject
   private ContextoJSF contextoJSF;
   
   
   public boolean isMostraTotal() {
        return mostraTotal;
    }

    public void setMostraTotal(boolean mostraTotal) {
        this.mostraTotal = mostraTotal;
    }

    
    
      public List<Gasto> getListaGastosFiltrados() {
        return listaGastosFiltrados;
    }

    public void setListaGastosFiltrados(List<Gasto> listaGastosFiltrados) {
        this.listaGastosFiltrados = listaGastosFiltrados;
    }


    public List<Gasto> getListaGastosTotais() throws ClassNotFoundException, SQLException {
        GastoDAO gastoDAO = new GastoDAO();
        this.listaGastosTotais = gastoDAO.findGastoEntities();
        return listaGastosTotais;
        
    }

    public void setListaGastosTotais(List<Gasto> listaGastosTotais) {
        this.listaGastosTotais = listaGastosTotais;
    }

    public double getGastosFiltrados() {
     
        return this.gastosFiltrados;
    }

    public void setGastosFiltrados(double gastosFiltrados) {
        this.gastosFiltrados = gastosFiltrados;
    }
    

    public boolean isPermiteConsultaSQL() {
        return permiteConsultaSQL;
    }


    public Integer getIDUsuarioPesquisado() {
        return IDUsuarioPesquisado;
    }

    public void setIDUsuarioPesquisado(Integer IDUsuarioPesquisado) {
        this.IDUsuarioPesquisado = IDUsuarioPesquisado;
    }

   

    public boolean isHouveErro() {
        return houveErro;
    }

    public void setHouveErro(boolean houveErro) {
        this.houveErro = houveErro;
    }

    public boolean isMostrarTabelaPesquisas() {
        return mostrarTabelaPesquisas;
    }

    public void setMostrarTabelaPesquisas(boolean mostrarTabelaPesquisas) {
        this.mostrarTabelaPesquisas = mostrarTabelaPesquisas;
    }

    public List<Gasto> getListaGastosPesquisa() {
        return listaGastosPesquisa;
    }

    public void setListaGastosPesquisa(List<Gasto> listaGastosPesquisa) {
        this.listaGastosPesquisa = listaGastosPesquisa;
    }
    
    

    public int getProjetoID() {
        return projetoID;
    }

    public void setProjetoID(int projetoID) {
        this.projetoID = projetoID;
    }

    public int getPesquisaByMes() {
        return pesquisaByMes;
    }

    public void setPesquisaByMes(int pesquisaByMes) {
        this.pesquisaByMes = pesquisaByMes;
    }

    public int getPesquisaByAno() {
        return pesquisaByAno;
    }

    public void setPesquisaByAno(int pesquisaByAno) {
        this.pesquisaByAno = pesquisaByAno;
    }
  
    

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

   
    
  
    public double getGastosTotais() {
        
        return this.gastosTotais;
    }

    public void setGastosTotais(double gastosTotais) {
        this.gastosTotais = gastosTotais;
    }


    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }
    
    



    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }
    
      public Gasto getGastoEditado() {
        return gastoEditado;
    }

    public void setGastoEditado(Gasto gastoEditado) {
        this.gastoEditado = gastoEditado;
    }

    /**
     * Creates a new instance of GastoBean
     */
    public GastoBean() {
    }
    
    public void limparTabelaView(){
        this.mostrarTabelaPesquisas = false;
        this.listaGastosPesquisa = null;
        this.gastosTotais = 0;
       
    }
    
    public void mostraMensagemErro(String message){
        
    this.houveErro = true;
    contextoJSF.adicionaMensagem("erro", message);
    
    }
    
    
    public List<Gasto> listaGastosPesquisados(){
        
        if(this.listaGastosPesquisa == null){
            this.setMostrarTabelaPesquisas(false);
        }
        
        return this.listaGastosPesquisa;
        
    }
    

    
    public void verificaGastosMes() throws ClassNotFoundException, SQLException {
        
       LoginFilter lf = new LoginFilter();
       Usuario usuarioLogado = new Usuario();
       usuarioLogado = lf.getUsuario();
       GastoDAO gastoDAO = new GastoDAO();
        
       if(usuarioLogado.getPapel().isPrivAdmin()){
            
        this.gastosTotais = gastoDAO.calculaGastosMensais(this.pesquisaByMes, this.pesquisaByAno);
        this.listaGastosPesquisa = gastoDAO.listaGastosByMes(this.pesquisaByMes, this.pesquisaByAno);
        
       }else{
           
           this.gastosTotais = gastoDAO.calculaGastosMensaisUsuarioLogado(this.pesquisaByMes, this.pesquisaByAno, usuarioLogado.getIdUsuario());
           this.listaGastosPesquisa = gastoDAO.listaGastosByMesUsuarioLogado(this.pesquisaByMes, this.pesquisaByAno, usuarioLogado.getIdUsuario());
       }
       
       if(!houveErro){
     
           this.mostrarTabelaPesquisas = true;

       }
       
             
    }
    
    
      public void verificaGastosLocal() throws ClassNotFoundException, SQLException{
        
       LoginFilter lf = new LoginFilter();
       Usuario usuarioLogado = new Usuario();
       usuarioLogado = lf.getUsuario();
        GastoDAO gastoDAO = new GastoDAO();
        
        if(usuarioLogado.getPapel().isPrivAdmin()){
            
        this.gastosTotais = gastoDAO.calculaGastosByLocal(this.localID);        
        this.listaGastosPesquisa = gastoDAO.listaGastosByLocal(this.localID);
        
        }else {
            this.gastosTotais = gastoDAO.calculaGastosByLocalUsuarioLogado(localID, usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByLocalUsuarioLogado(this.localID, usuarioLogado.getIdUsuario());
        }
        
        if (!houveErro){
             this.mostrarTabelaPesquisas = true;
        }
       
       
    }
      
      
       public void verificaGastosUsuario() throws ClassNotFoundException, SQLException{
           
       LoginFilter lf = new LoginFilter();
       Usuario usuarioLogado = new Usuario();
       usuarioLogado = lf.getUsuario();
       GastoDAO gastoDAO = new GastoDAO();
        
        if(usuarioLogado.getPapel().isPrivAdmin()){
            
           this.gastosTotais = gastoDAO.calculaGastosByUsuario(this.IDUsuarioPesquisado);
           this.listaGastosPesquisa = gastoDAO.listaGastosByUsuario(this.IDUsuarioPesquisado);
          
       }else {
            this.gastosTotais = gastoDAO.calculaGastosByUsuario(usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByUsuarioLogado(usuarioLogado.getIdUsuario());
        }
        
        this.mostrarTabelaPesquisas = true;
        
        
       }
      
      
        public void verificaGastosProjeto() throws ClassNotFoundException, SQLException{
        
        LoginFilter lf = new LoginFilter();
        Usuario usuarioLogado = new Usuario();
        usuarioLogado = lf.getUsuario();
        GastoDAO gastoDAO = new GastoDAO();
        
        if(usuarioLogado.getPapel().isPrivAdmin()){
            
           this.gastosTotais = gastoDAO.calculaGastosByProjeto(this.projetoID);
           this.listaGastosPesquisa = gastoDAO.listaGastosByProjeto(this.projetoID);
            
        }else {
        this.gastosTotais = gastoDAO.calculaGastosByProjetoUsuarioLogado(this.projetoID, usuarioLogado.getIdUsuario());
        this.listaGastosPesquisa = gastoDAO.listaGastosByProjetoUsuarioLogado(this.projetoID, usuarioLogado.getIdUsuario());
        }
        
        if(!houveErro){
            this.mostrarTabelaPesquisas = true;
        }
       
    }
    
    
    
    public void adicionarGasto() throws ClassNotFoundException, SQLException{
    
        GastoDAO gastoDAO = new GastoDAO();
        boolean gravado = false;
          
         
          gasto.setLocal(local);
         
     
        if(gasto.getLocal()==null & gasto.getUsuario()==null){
            String mensagem1 = null;
             mensagem1 = "HOUVE UM PROBLEMA E O GASTO NÃO FOI GRAVADO POIS "
                     + "O LOCAL E/OU USUÁRIO ESTÃO NULOS";

             contextoJSF.adicionaMensagem("erro", mensagem1);


        }else{
         gravado =  gastoDAO.criarGasto(gasto);
        }
         String mensagem = null;
           
               
        if  (!gravado){
            mensagem = "HOUVE UM PROBLEMA E O GASTO NÃO FOI GRAVADO";
             contextoJSF.adicionaMensagem("erro", mensagem);
        }else {
            
            mensagem = "O GASTO FOI GRAVADO COM SUCESSO";
            contextoJSF.adicionaMensagem("erro", mensagem);

        }
        
       //this.gasto = new Gasto();
        }
    
        public void gravaLocal() throws SQLException, ClassNotFoundException{
            
             LocalDAO localDAO = new LocalDAO();
            
            
        this.local = localDAO.findLocal(localID);
        this.gasto.setLocal(local);
                    
        }
    
        public List<Local> selecionaLocais() throws SQLException, ClassNotFoundException{
            
            LocalDAO locaisDAO = new LocalDAO();
           
            List<Local> listaLocais =  locaisDAO.findLocalEntities();
            
            return listaLocais;
        }
        
        
        public List<Projeto> selecionaProjetos() throws ClassNotFoundException, SQLException{
            
            ProjetoDAO projetoDAO = new ProjetoDAO();
            List<Projeto> listaProjetos = projetoDAO.findProjetoEntities();
            return listaProjetos;
        }
        
        
           public List<Usuario> selecionaUsuarios() throws ClassNotFoundException, SQLException{
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
           
            List<Usuario> listaUsuarios =  usuarioDAO.consultaUsuarios();
            
            return listaUsuarios;
        }
           
             public void gravaUsuario() throws ClassNotFoundException, SQLException{
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            this.usuario = usuarioDAO.findUsuario(IDUsuarioPesquisado);
            gasto.setUsuario(usuario);
        }
             
             public void editaGasto() throws Exception{
                 LoginFilter lf = new LoginFilter();
                 boolean possuiPrivilegio = lf.verificaPrivilegio();
                 
                 if(possuiPrivilegio){
                 this.canEdit = true;
                 }else {
            
            String mensagem = "DESCULPE, MAS VOCÊ NÃO TEM PRIVILÉGIO DE ADMINISTRADOR PARA EXECUTAR ESTA AÇÃO!";
            contextoJSF.adicionaMensagem("alerta", mensagem);
               }
                 
             }
             
             
             /*
             Estou recebendo neste método um gasto como parâmetro que está 
             vindo do dataTable. Ou seja, este gasto que vem como parâmetro é 
             a variável item lá do dataTable.
             
             Uma outra foma de editar seria carregar os datos modificados no 
             objeto gastoEditado desta classe aqui, através de um elemento 
             
                <h:commandButton value="SALVAR GASTO EDITADO" 
                             action="#{gastoBean.salvarGastoEditado()}"
                             rendered="#{gastoBean.canEdit}">
                             
                <f:setPropertyActionListener target="#{gastoBean.gastoEditado}"
             value="#{item}"/> 
          
            </h:commandButton>
             
             Neste caso eu não passaria o item como parâmetro do método salvarGastoEditado().
             
             */
            public void salvarGastoEditado() throws Exception{
                
                /*Só é necessário se eu não utilizar o 
                <f:setPropertyActionListener /> no dataTable
                Só que neste caso tenho que passar a variável do dataTable (que 
                é um Gasto, ou seja, um item da lista de gastos carregada) 
                como parâmetro do método.
                
                this.gastoEditado = gasto; 
                 */
                
                 LoginFilter lf = new LoginFilter();
                 boolean possuiPrivilegio = lf.verificaPrivilegio();
                 
                 if(possuiPrivilegio & this.gastoEditado != null){
                 GastoDAO gastoDAO = new GastoDAO();
                 gastoDAO.edit(this.gastoEditado);
                 this.canEdit = false;
                 this.listaGastosTotais = gastoDAO.findGastoEntities();
            
               }
                
                
            }
        
        
             public void deletaGasto() throws ClassNotFoundException, SQLException {
                 
                 LoginFilter lf = new LoginFilter();
                 boolean possuiPrivilegio = lf.verificaPrivilegio();
                 
                 if(possuiPrivilegio & this.gasto != null){
                 
                 GastoDAO gastoDAO = new GastoDAO();
                 gastoDAO.destroy(this.gasto.getId_gasto());
                 this.mostrarTabelaPesquisas = false;
                 this.listaGastosPesquisa = null;
                 this.gastosTotais = 0;
    
                 }
                 
             }
        
        public String verificaGastosTotais() throws ClassNotFoundException, ClassNotFoundException, SQLException{
            
            GastoDAO gastoDAO = new GastoDAO();
            LoginFilter lf = new LoginFilter();
            boolean isAdministrador = lf.verificaPrivilegio();
            Usuario usuarioLogado = lf.getUsuario();
           
            if(isAdministrador){
            this.listaGastosPesquisa = gastoDAO.findGastoEntities(); 
            this.gastosTotais = gastoDAO.calculaGastosTotais();
            }else{
                this.listaGastosPesquisa = gastoDAO.listaGastosByUsuarioLogado(usuarioLogado.getIdUsuario()); 
                this.gastosTotais  = gastoDAO.calculaGastosTotaisUsuarioLogado(usuarioLogado.getIdUsuario());
            }
                if(!houveErro){
                    this.mostrarTabelaPesquisas = true;
                }
            return "/restricted/gastos?faces-redirect=true" ;
        }
        
         // http://respostas.guj.com.br/9399-primefaces-datatable-listener-para-calculo-apos-filtro
        //        https://groups.google.com/forum/#!topic/javasf/24reJNQo-eQ  

 /*   public void filterListener(){
        
             for (Gasto gasto : this.listaGastosFiltrados){
                
                this.gastosFiltrados += gasto.getValorGasto();
                
            }
       }

/*
        public void calcularTotaisFiltrados(List<Gasto> listaGastosFiltrada){
            
            for (Gasto gasto : this.listaGastosFiltrados){
                
                this.gastosFiltrados += gasto.getValorGasto();
                
            }
            
        }
     */  
        
        
    public boolean valorEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) { //java.util.Locale
   
    //tirando espaços do filtro
        String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

        System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

        // o filtro é nulo ou vazio?
        if (textoDigitado == null || textoDigitado.equals("")) {
            return true;
        }

        // elemento da tabela é nulo?
        if (valorColuna == null) {
            return false;
        }

        try {
            // fazendo o parsing do filtro para converter para Double
            Double valorDigitado = Double.valueOf(textoDigitado);
            Double valorXColuna = (Double) valorColuna;

            // comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro
            return valorXColuna.compareTo(valorDigitado) < 0;

        } catch (NumberFormatException e) {

            // usuario nao digitou um numero
            return false;
        }
    
    
    }

    public void calculaGastos(){
         
        this.mostraTotal = true;
         
        if (this.listaGastosFiltrados != null){
           
            this.gastosFiltrados = 0;
            for (Gasto gasto : this.listaGastosFiltrados){
             this.gastosFiltrados += gasto.getValorGasto();
              }
        }else {
            this.gastosFiltrados = 0;
            for (Gasto gasto : this.listaGastosTotais){
            this.gastosFiltrados += gasto.getValorGasto();
                }
            }
        

       }
       
    

}
