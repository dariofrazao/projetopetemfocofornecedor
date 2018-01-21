package projetaobcc20172.com.projetopetemfocofornecedor.editarfornecedortest;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

/**
 * Created by renat on 17/01/2018.
 */

public class TestToolsEditFornecedor {

    protected static void preencherEclicar(String telefone, String horarios) {
        TestTools.digitarCampoComScroll(R.id.etEditarTelefoneFornecedor, telefone);
        TestTools.selecionarItemSpinnerComScroll(R.id.horariosEditarSpinner, horarios);
        TestTools.clicarBotaoComScroll(R.id.botao_editar_fornecedor);
    }
}
