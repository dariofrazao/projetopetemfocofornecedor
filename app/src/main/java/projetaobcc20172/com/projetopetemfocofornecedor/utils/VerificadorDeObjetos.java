package projetaobcc20172.com.projetopetemfocofornecedor.utils;

import android.content.Context;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.CampoObrAusenteException;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
/**
 * Created by raul on 10/12/17.
 * Essa classe é responsavel por validar os dados que são recebidos pelo controller
 */

public class VerificadorDeObjetos {

    //Verifica se as informações do fornecedor estão corretas
    public static void vDadosFornecedor(Fornecedor fornecedor, Context cad) throws ValidacaoException {
        if(fornecedor.getNome().isEmpty()
                || fornecedor.getSenha().isEmpty()
                || fornecedor.getEmail().isEmpty()
                || fornecedor.getTelefone().isEmpty()
                || fornecedor.getCpfCnpj().isEmpty()
                || fornecedor.getHorarios().isEmpty()){
            throw new ValidacaoException(cad.getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
        }
        else if(!fornecedor.getSenha().equals(fornecedor.getSenha2())){
            throw new ValidacaoException(cad.getString(R.string.erro_cadastro_fornecedor_senhas_diferentes_Toast));
        }

    }

    //Método responsável por avaliar se um obj endereco possui todas os atributos obrigatorios
    public static void vDadosObrEndereco(Endereco end) throws CampoObrAusenteException {
        if (        end.getLogradouro().isEmpty()
                    ||
                    end.getBairro().isEmpty()
                    ||
                    end.getLocalidade().isEmpty()
                    ||
                    end.getUf().isEmpty()
                    || end.getCep().isEmpty()
                    ) {
                throw new CampoObrAusenteException();
            }
    }

    public static void vDadosServico(Servico serv, Context cad) throws ValidacaoException {
        if(serv.getNome().isEmpty()){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_nome));
        }
        else if(serv.getValor().equals("")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_valor));
        }
        else if(serv.getTipoPet().equals("")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_tipoAnimal));
        }
        //else if(serv.getDescricao().isEmpty()){
         //   throw new ValidacaoException(cad.getResources().getString(R.string.preencha_campo_descricao));
        //}
    }


}
