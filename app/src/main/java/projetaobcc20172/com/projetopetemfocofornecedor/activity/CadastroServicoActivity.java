package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MascaraDinheiro;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Activity de cadastro de serviços
 */
public class CadastroServicoActivity extends AppCompatActivity {

    private EditText mEtValor, mEtDescricao;
    private Spinner mSpinnerServico;
    private Spinner mSpinnerTipoAnimal;
    private String mIdUsuarioLogado;
    private Servico mServico;
    private boolean mIsViewsHabilitadas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_servico);
        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_cadastro_serviço);

        mIdUsuarioLogado = getPreferences("id", CadastroServicoActivity.this);

        // Configura toolbar
        toolbar.setTitle(R.string.tb_cadastro_serviço);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mSpinnerServico = findViewById(R.id.spinner_nome_servico);
        mSpinnerTipoAnimal = findViewById(R.id.tipoAnimalSpinner);

        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.servicos));
        mSpinnerServico.setAdapter(adapter_state);

        ArrayAdapter<String> adapterTipoAnimais = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tiposAnimais));
        mSpinnerTipoAnimal.setAdapter(adapterTipoAnimais);

        mEtValor = findViewById(R.id.etCadastroValorServico);
        mEtDescricao = findViewById(R.id.etCadastroDescricaoServico);

        Locale mLocal = new Locale("pt", "BR");

        mEtValor.addTextChangedListener(new MascaraDinheiro(mEtValor, mLocal));

        Button btnCadastrar = findViewById(R.id.btnCadastrarServico);
        final Button btnEditar = findViewById(R.id.btnEditarServico);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarServico();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Servico servico = new Servico();
                    servico.setValor(mEtValor.getText().toString());
                    servico.setDescricao(mEtDescricao.getText().toString());
                    servico.setNome(mSpinnerServico.getSelectedItem().toString());
                    servico.setTipoPet(mSpinnerTipoAnimal.getSelectedItem().toString());

                    VerificadorDeObjetos.vDadosServico(servico, CadastroServicoActivity.this);

                    if(!mServico.equals(servico)){
                        servico.setmId(mServico.getId());
                        ServicoDaoImpl servicoDao = new ServicoDaoImpl(CadastroServicoActivity.this);
                        servicoDao.atualizar(servico, mIdUsuarioLogado);

                    }
                    abrirTelaPrincipal();
                } catch (ValidacaoException e) {
                    e.printStackTrace();
                    Utils.mostrarMensagemLonga(CadastroServicoActivity.this, e.getMessage());
                }
            }
        });


        // Verifica se há algum extra na intent, caso haja será uma edição de dados.
        Intent intent = getIntent();
        if(intent.hasExtra("servico")){
            toolbar.setTitle(R.string.title_activity_editar_servico);
            btnCadastrar.setVisibility(View.GONE);
            btnEditar.setVisibility(View.VISIBLE);
            mServico = (Servico) intent.getSerializableExtra("servico");
            setvaluesOnViews();

        }
    }

    private void setvaluesOnViews() {
        if(mServico != null){
            String[] listaServicos = getResources().getStringArray(R.array.servicos);
            String[] listaPets = getResources().getStringArray(R.array.tiposAnimais);
            int i = 0;
            for(String s: listaServicos){
                if(s.equalsIgnoreCase(mServico.getNome())) break;
                i++;
            }
            mSpinnerServico.setSelection(i);
            i= 0;
            for(String s:listaPets){
                if(s.equalsIgnoreCase(mServico.getTipoPet())) break;
                i++;
            }
            mSpinnerTipoAnimal.setSelection(i);
            mEtValor.setText(mServico.getValor());
            mEtDescricao.setText(mServico.getDescricao());
        }

    }


    private boolean verificarCamposPreenchidos(){
        return (!mEtValor.getText().toString().isEmpty()||
                !mEtDescricao.getText().toString().isEmpty());
    }

    private void salvarServico(){
        try {

            //Recuperar id do fornecedor logado
            mIdUsuarioLogado = getPreferences("id", CadastroServicoActivity.this);
            mServico = new Servico();
            mServico.setNome(mSpinnerServico.getSelectedItem().toString());
            mServico.setValor(mEtValor.getText().toString());
            mServico.setDescricao(mEtDescricao.getText().toString());
            mServico.setTipoPet(mSpinnerTipoAnimal.getSelectedItem().toString());

            VerificadorDeObjetos.vDadosServico(mServico,this);
            //Chamada do DAO para salvar no banco
            ServicoDaoImpl servicoDao =  new ServicoDaoImpl(this);
            servicoDao.inserir(mServico, mIdUsuarioLogado);
            abrirTelaPrincipal();

        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Método do botão voltar
    @Override
    public void onBackPressed(){
        if (verificarCamposPreenchidos() && mIsViewsHabilitadas) confirmarSaida();
        else{
            //CadastroServicoActivity.super.onBackPressed();
            this.abrirTelaPrincipal();
            finish();
        }
    }

    //Método que exibe pergunta de confirmação ao fornecedor caso ele clique no botão de voltar com as
    //informações do serviço inseridas nos campos
    public void confirmarSaida(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        CadastroServicoActivity.super.onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Botão não foi clicado
                        break;
                    default:
                        break;
                }
            }
        };

        Utils.mostrarPerguntaSimNao(this, getString(R.string.atencao),
                getString(R.string.pergunta_confirma_dados_serao_perdidos), dialogClickListener,
                dialogClickListener);
    }

    //Método que recupera o id do fornecedor logado, para salvar o Serviço no nó do fornecedor que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(CadastroServicoActivity.this, ServicosActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
