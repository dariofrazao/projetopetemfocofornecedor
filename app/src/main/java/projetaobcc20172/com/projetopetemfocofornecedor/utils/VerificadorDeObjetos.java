package projetaobcc20172.com.projetopetemfocofornecedor.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.CampoObrAusenteException;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

/**
 * Created by raul on 10/12/17.
 * Essa classe é responsavel por validar os dados que são recebidos pelo controller
 */

public class VerificadorDeObjetos {

    //Verifica se as informações do fornecedor estão corretas
    public static void vDadosFornecedor(Fornecedor fornecedor, Context cad) throws ValidacaoException {
        if(fornecedor.getNome().isEmpty()
                || fornecedor.getEmail().isEmpty()
                || fornecedor.getTelefone().isEmpty()
                || fornecedor.getCpfCnpj().isEmpty()
                || fornecedor.getHorarios().isEmpty()){
            throw new ValidacaoException(cad.getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
        }
    }

    //Método responsável por avaliar se um obj endereco possui todas os atributos obrigatorios
    public static void vDadosObrEndereco(Endereco end) throws CampoObrAusenteException {
        if (
                end.getLogradouro().isEmpty()
                        ||
                        end.getBairro().isEmpty()
                        ||
                        end.getLocalidade().isEmpty()
                        ||
                        end.getUf().isEmpty()
                        ||
                        end.getCep().isEmpty()
                        ||
                        end.getmLatitude() == 0
                        ||
                        end.getmLongitude() == 0
                ) {
            throw new CampoObrAusenteException();
        }
    }

    public static void vDadosServico(Servico serv, Context cad) throws ValidacaoException {
        if(serv.getNome().equalsIgnoreCase("Selecionar") || serv.getNome().equalsIgnoreCase("")){
            throw new ValidacaoException(cad.getString(R.string.error_selecione_um_servico));
        }
        else if(serv.getValor().equals("") || serv.getValor().equalsIgnoreCase("R$0,00")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_valor));
        }
        else if(serv.getTipoPet().equalsIgnoreCase("Selecionar") || serv.getTipoPet().equalsIgnoreCase("")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_tipoAnimal));
        }
        //else if(serv.getDescricao().isEmpty()){
         //   throw new ValidacaoException(cad.getResources().getString(R.string.preencha_campo_descricao));
        //}
    }

    public static void vDadosCupom(Cupom cupom, Context cad) throws ValidacaoException {
        if(cupom.getJuncao().equals("Selecione um serviço")){
            throw new ValidacaoException(cad.getString(R.string.error_selecione_um_servico));
        }
        else if(cupom.getNome().equals("")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_nome));
        }
        else if(cupom.getValor().equals("") || cupom.getValor().equalsIgnoreCase("R$0,00")){
            throw new ValidacaoException(cad.getString(R.string.preencha_campo_valor));
        }
        else if(!cupom.getNome().equals("")){
            String s = cupom.getNome();
            if (s.length() < 6){
                throw new ValidacaoException(cad.getString(R.string.seis_caracteres));
            }
            else if (s.length() > 15){
                throw new ValidacaoException(cad.getString(R.string.quinze_caracteres));
            }
            char c = ' ';
            char d = '\n';
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == c) {
                    throw new ValidacaoException(cad.getString(R.string.contem_espaco));
                }
                else if (s.charAt(i) == d) {
                    throw new ValidacaoException(cad.getString(R.string.contem_quebra_de_linha));
                }
            }
        }
    }

    public static void vDadosPromocao(Promocao promo, Context cad) throws ValidacaoException{
        if(promo.getTitulo().equals("")){
            throw new ValidacaoException("Informe um título para promoção");
        }else if(promo.getDescricao().equals("")){
            throw new ValidacaoException("Informe uma descrição para promoção");
        }else if(promo.getValor().equals("") || promo.getValor().equalsIgnoreCase("R$0,00")){
            throw new ValidacaoException("Informe um valor para promoção");
        }
    }

    //Método responsável por avaliar se as coordenadas geograficas esta preencidas
    public static void vDadosObrCoordenadasGeograficas(LatLng localizacao) throws ValidacaoException {
        if (localizacao.latitude == 0 || localizacao.longitude == 0) {
            throw new ValidacaoException();
        }
    }
}
