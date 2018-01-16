package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;

/**
 * Created by dario on 11/12/2017.
 */

public interface FornecedorDao {
    void inserir(Fornecedor fornecedor, String idFornecedor);
    void atualizar(Fornecedor fornecedor, String idFornecedor);
}
