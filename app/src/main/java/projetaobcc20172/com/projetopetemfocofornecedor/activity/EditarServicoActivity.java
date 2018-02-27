package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
 * Created by LuizAlberes on 23/02/2018.
 */

public class EditarServicoActivity extends AppCompatActivity {

    //@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)//permite que essa variavel seja vista pela classe de teste
    private EditText mEtDescricao, mEtValor;
    private Servico mServico;
    private String mIdUsuarioLogado;
    private Spinner mSpinnerNomeServico, mSpinnerTipoAnimalServico;
    public Button btnEditarServico;
    private String mHabilita = "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_editar_serviço);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("id", "");

        mServico = (Servico) getIntent().getSerializableExtra("Servico");

        // Configura toolbar
        toolbar.setTitle("Editar Serviço");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mEtValor = findViewById(R.id.etEdicaoValorServico);
        Locale mLocal = new Locale("pt", "BR");
        mEtValor.addTextChangedListener(new MascaraDinheiro(mEtValor, mLocal));
        mEtValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtValor.setText(mServico.getValor());

        mEtDescricao = findViewById(R.id.etEdicaoDescricaoServico);
        mEtDescricao.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        mEtDescricao.setText(mServico.getDescricao());

        mSpinnerNomeServico = (Spinner) findViewById(R.id.spEditarNomeServico);
        ArrayAdapter<String> adapter_state_nome = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.servicos));
        mSpinnerNomeServico.setAdapter(adapter_state_nome);
        String nomeServico = mServico.getNome();
        int posicaoNomeServico = adapter_state_nome.getPosition(nomeServico);
        mSpinnerNomeServico.setSelection(posicaoNomeServico);


        mSpinnerTipoAnimalServico = (Spinner) findViewById(R.id.spEdicaoTipoAnimalServico);
        ArrayAdapter<String> adapter_state_tipo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tiposAnimais));
        mSpinnerTipoAnimalServico.setAdapter(adapter_state_tipo);
        String tipoPetServico = mServico.getTipoPet();
        int posicaotipoPetServico = adapter_state_tipo.getPosition(tipoPetServico);
        mSpinnerTipoAnimalServico.setSelection(posicaotipoPetServico);

        btnEditarServico = findViewById(R.id.btnEditarServico);
        btnEditarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chamar o método para editar o pet no banco
                editarServico();
            }
        });

    }

    private boolean verificarCamposPreenchidos(){
        return !mEtValor.getText().toString().isEmpty();
    }

    //Método que edita o pet no banco
    private void editarServico(){
        try {
            //Recuperar id do fornecedor logado
            mIdUsuarioLogado = getPreferences("id", this);

            Servico servico = new Servico();
            servico.setNome(mSpinnerNomeServico.getSelectedItem().toString());
            servico.setTipoPet(mSpinnerTipoAnimalServico.getSelectedItem().toString());
            servico.setValor(mEtValor.getText().toString());
            servico.setDescricao(mEtDescricao.getText().toString());

            VerificadorDeObjetos.vDadosServico(servico,this);

            btnEditarServico.setText("Aguarde...");
            btnEditarServico.setEnabled(false);

            if(!mServico.equals(servico)){
                servico.setmId(mServico.getId());
                servico.setIdFornecedor(mIdUsuarioLogado);
                ServicoDaoImpl servicoDao = new ServicoDaoImpl(EditarServicoActivity.this);
                servicoDao.compararAtualizar(servico,mIdUsuarioLogado);
                ativarButtonAguarde(servico, btnEditarServico);
            } else {
                servico.setmId(mServico.getId());
                servico.setIdFornecedor(mIdUsuarioLogado);
                ServicoDaoImpl servicoDao = new ServicoDaoImpl(EditarServicoActivity.this);
                servicoDao.atualizar(servico,mIdUsuarioLogado);
                finish();
            }

        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }
    }

    private void ativarButtonAguarde(final Servico serv, final Button btn){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("servicos").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if (dados.child("nome").getValue().equals(serv.getNome()) &
                            dados.child("tipoPet").getValue().equals(serv.getTipoPet()) &
                            dados.child("valor").getValue().equals(serv.getValor()) &
                            dados.child("descricao").getValue().equals(serv.getDescricao())){
                        mHabilita = "1";
                        break;
                    }
                }
                if(mHabilita.equals("0")){
                    btn.setText("Aguarde...");
                    btn.setEnabled(false);
                    finish();
                } else {
                    btn.setText("EDITAR");
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

    //Método do ícone para voltar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Método do botão voltar
    @Override
    public void onBackPressed() {
        if (verificarCamposPreenchidos()) confirmarSaida();
        else EditarServicoActivity.super.onBackPressed();
    }

    //Método que exibe pergunta de confirmação ao usuário caso ele clique no botão de voltar com as
    //informações do pet inseridas nos campos
    public void confirmarSaida(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
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

    //Método que recupera o id do usuário logado, para editar o pet no nó do usuário que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
