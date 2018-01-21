package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;

/**
 * Created by Cloves on 19/01/2018.
 */

public interface PromocaoDao {

    void inserir(Promocao promocao,String idFornecedor);
    void remover(Promocao promocao,String idFornecedor);
    void atualizar(Promocao promocao,String idFornecedor);
}
