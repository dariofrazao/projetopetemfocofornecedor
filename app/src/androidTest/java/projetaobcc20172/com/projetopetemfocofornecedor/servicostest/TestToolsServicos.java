package projetaobcc20172.com.projetopetemfocofornecedor.servicostest;


import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;


public class TestToolsServicos {

    private static void preencher(String nome, String valor,String descricao,String tipoAnimal){
        TestTools.selecionarItemSpinnerComScroll(R.id.spinner_nome_servico, nome);
        TestTools.digitarCampo(R.id.etCadastroValorServico, valor);
        TestTools.digitarCampoComScroll(R.id.etCadastroDescricaoServico, descricao);
        TestTools.selecionarItemSpinnerComScroll(R.id.tipoAnimalSpinner,tipoAnimal);
    }

    protected static void preencherEclicarCadastrar(String nome, String valor,String descricao) {
        TestToolsServicos.preencher(nome,valor,descricao,"Selecionar");
        TestTools.clicarBotaoComScroll(R.id.btnCadastrarServico);
    }

    protected static void preencherEditar(String nome, String valor,String descricao,String pet){
        TestToolsServicos.preencher(nome,valor,descricao,pet);
        TestTools.clicarBotaoComScroll(R.id.btnEditarServico);
    }
    public static void preencherEClicar(String nomeServico, String valor, String descricao,String tipoAnimal){
        TestToolsServicos.preencher(nomeServico,valor,descricao,tipoAnimal);
        TestTools.clicarBotaoComScroll(R.id.btnCadastrarServico);
    }

    protected  static void clicarEditar(int indice){
        TestTools.clicarEmItemDentroListView(R.id.lv_serviços,0,R.id.ibEditarServico);
    }

    protected static void clicarExcluir(int indice) throws InterruptedException {
        TestTools.clicarEmItemDentroListView(R.id.lv_serviços,0,R.id.ibRemoverServico);
        Thread.sleep(1000);
        onView(withId(android.R.id.button1)).perform(click());//Clica na opção "sim" da dialog
    }

    protected static ListView getListView(){
        ListView lv = TestTools.activityAtual().findViewById(R.id.lv_serviços);
        return  lv;
    }

    protected static int getTamanhoList(){
        return TestTools.getTamanhoListView(TestTools.activityAtual(),R.id.lv_serviços);
    }

    protected static void verificarServicoEhIgual(String nome,String valor,String descricao,String pet){
        Spinner sNome = TestTools.activityAtual().findViewById(R.id.spinner_nome_servico);
        Spinner sPet = TestTools.activityAtual().findViewById(R.id.tipoAnimalSpinner);
        TextView tvValor = TestTools.activityAtual().findViewById(R.id.etCadastroValorServico);
        TextView tvDesc = TestTools.activityAtual().findViewById(R.id.etCadastroDescricaoServico);
        assertEquals(nome,sNome.getSelectedItem().toString());
        assertEquals(valor,tvValor.getText().toString());
        assertEquals(descricao,tvDesc.getText().toString());
        assertEquals(pet,sPet.getSelectedItem().toString());
    }

    public static  void clicarMeusServicos(){
        TestTools.clicarBotao(R.id.btnMeusServicos);
    }

    public static void clicarAddServico(){
        TestTools.clicarBotao(R.id.btnCadastrarServico);
    }
}
