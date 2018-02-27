package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;

/**
 * Created by LuizAlberes on 15/12/2017.
 */

public interface CupomDao {
    void inserir(Cupom cupom, String idFornecedor);
    void compararInserir(Cupom cupom, String idFornecedor);
    void compararAtualizar(Cupom cupom, String idFornecedor);
    void atualizar(Cupom cupom, String idFornecedor);
    void remover(Cupom Cupom, String idFornecedor);
}
