package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;

/**
 * Created by dario on 11/12/2017.
 */

public interface FornecedorDao {
    void inserir(Fornecedor fornecedor, String idFornecedor);
    //void remover(Fornecedor fornecedor,  String idFornecedor);
    //void atualizar(Fornecedor fornecedor,  String idFornecedor);
    /*
    * Comentário feito por Raul: Removi a busca pois ela só faz sentido no contexto
    * do consumidor. O dao de Fornecedor no app fornecedor só precisa inserir,atualizar e remover.
    List<Fornecedor> buscarPorNome(String nome);
    List<Fornecedor> buscarTodosFornecedor(String idFornecedor);
    */

}
