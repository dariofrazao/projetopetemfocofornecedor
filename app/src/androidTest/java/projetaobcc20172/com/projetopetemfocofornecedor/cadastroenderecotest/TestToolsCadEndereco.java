package projetaobcc20172.com.projetopetemfocofornecedor.cadastroenderecotest;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

/**
 * Created by raul on 09/12/17.
 */

public class TestToolsCadEndereco {

        public static void preencher(String logradouro,String numero,String complemento,
                                            String bairro,String localidade,String uf,String cep){

            TestTools.digitarCampo(R.id.etCadastroCepEndereco,cep);
            TestTools.digitarCampo(R.id.etCadastroLogradouroEndereco,logradouro);
            TestTools.digitarCampo(R.id.etCadastroNumeroEndereco,numero);
            TestTools.digitarCampo(R.id.etCadastroComplementoEndereco,complemento);
            TestTools.digitarCampo(R.id.etCadastroBairroEndereco,bairro);
            TestTools.digitarCampo(R.id.etCadastroLocalidadeEndereco,localidade);
            TestToolsCadEndereco.selecionarEstado(uf);
        }

        public static void selecionarEstado(String estado){
            TestTools.selecionarItemSpinnerComScroll(R.id.ufSpinner,estado);
        }

        public static void clicarCadastrar(){
            TestTools.clicarBotaoComScroll(R.id.btnCadastrarEndereco);
        }
}
