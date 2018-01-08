package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroServicoActivity;

/**
 * Created by Felipe Oliveira on 05/01/18.
 * <flpdias14@gmail.com>
 */

public class EditarServicoActivityTest {


    @Rule
    public ActivityTestRule<CadastroServicoActivity> edicaoCadastroServicoActivityRule= new ActivityTestRule<>(CadastroServicoActivity.class);

    @Test
    public void removerTodosInfos(){
        TestToolsCadServicos.clicarEditar();
        TestToolsCadServicos.preencherEClicarSalvar("Selecionar","","");
        TestTools.checarToast(edicaoCadastroServicoActivityRule.getActivity()
                .getString(R.string.error_selecione_um_servico));

    }

    @Test
    public void removerNome(){
        TestToolsCadServicos.clicarEditar();
        TestToolsCadServicos.preencherEClicarSalvar("Selecionar","","");
        TestTools.checarToast(edicaoCadastroServicoActivityRule.getActivity()
                .getString(R.string.error_selecione_um_servico));

    }
}
