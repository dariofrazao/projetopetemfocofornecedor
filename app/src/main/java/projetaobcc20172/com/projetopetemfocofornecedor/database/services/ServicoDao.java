package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

/**
 * Created by Felipe Oliveira on 08/12/17.
 * <flpdias14@gmail.com>
 */

public interface ServicoDao {

    void inserir(Servico servico, String idFornecedor);
    void remover(Servico servico, String idFornecedor);
    void atualizar(Servico servico, String idFornecedor);
    /*
    List<Servico> buscarPorNome(String nome);
    List<Servico> buscarTodosFornecedor(String idFornecedor);
    */

}
