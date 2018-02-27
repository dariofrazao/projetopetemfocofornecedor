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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Locale;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MascaraDinheiro;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Activity de cadastro de serviços
 */
public class CadastroServicoActivity extends AppCompatActivity implements Serializable{

    private EditText mEtValor, mEtDescricao;
    private Spinner mSpinnerServico;
    private Spinner mSpinnerTipoAnimal;
    private String mIdUsuarioLogado;
    private Servico mServico;
    private boolean mIsViewsHabilitadas = true;
    public Button mCadastrarServico;
    private String mHabilita = "0";

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

        mCadastrarServico = findViewById(R.id.btnCadastrarServico);

        mCadastrarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarServico();
            }
        });

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

            mCadastrarServico.setText("Aguarde...");
            mCadastrarServico.setEnabled(false);

            ServicoDaoImpl servicoDao =  new ServicoDaoImpl(this);
            servicoDao.compararInserir(mServico, mIdUsuarioLogado);

            ativarButtonAguarde(mServico, mCadastrarServico);
//            Intent intent = new Intent(CadastroServicoActivity.this, ServicosActivity.class);
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }
    }


    private void ativarButtonAguarde(final Servico servico, final Button btn){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("servicos").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if (dados.child("nome").getValue().equals(servico.getNome()) &
                            dados.child("tipoPet").getValue().equals(servico.getTipoPet()) &
                            dados.child("valor").getValue().equals(servico.getValor()) &
                            dados.child("descricao").getValue().equals(servico.getDescricao())){
                        mHabilita = "1";
                        break;
                    }
                }
                if(mHabilita.equals("0")){
                    btn.setText("Aguarde...");
                    btn.setEnabled(false);
                    finish();
                } else {
                    btn.setText("CADASTRAR");
                    btn.setEnabled(true);
                    mHabilita = "0";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
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
            CadastroServicoActivity.super.onBackPressed();
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
                        CadastroServicoActivity.super.onBackPressed();
                        finish();
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

    private void abrirTelaPets() {
        onBackPressed();
    }
}
