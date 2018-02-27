package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDao;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MascaraDinheiro;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

public class CadastroPromocaoActivity extends AppCompatActivity {
    private EditText mTitulo;
    private EditText mDescricao;
    private EditText mValor;
    private boolean mIsViewsHabilitadas = true;
    private String mIdUsuarioLogado;
    private Promocao mPromocao;
    private Boolean mEdicao = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_promocao);
        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CadastroPromocaoActivity.this);
        mIdUsuarioLogado = preferences.getString("id", "");

        // Configura toolbar
        toolbar.setTitle("Cadastro de Promoção");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mTitulo = findViewById(R.id.etTitulo);
        mDescricao = findViewById(R.id.etDescricao);
        mValor = findViewById(R.id.etValor);
        Locale mLocal = new Locale("pt", "BR");
        mValor.addTextChangedListener(new MascaraDinheiro(mValor, mLocal));
        mValor.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button btnCalendarioPromocao = findViewById(R.id.btnCalendarioPromocao);
        if(getIntent().hasExtra("promocao")){
            btnCalendarioPromocao.setText("CALENDARIO");
        }
        final Button btnEditarPromocao = findViewById(R.id.btnEditarPromocao);

        btnCalendarioPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(getIntent().hasExtra("promocao")){
                        mPromocao = (Promocao) getIntent().getSerializableExtra("promocao");
                    }else{
                        mPromocao = new Promocao();
                    }
                    mIdUsuarioLogado = getPreferences("id", CadastroPromocaoActivity.this);
                    mPromocao.setTitulo(mTitulo.getText().toString());
                    mPromocao.setValor(mValor.getText().toString());
                    mPromocao.setDescricao(mDescricao.getText().toString());
                    mPromocao.setFornecedorId(mIdUsuarioLogado);
                    VerificadorDeObjetos.vDadosPromocao(mPromocao,CadastroPromocaoActivity.this);
                    abrirCalendario();
                } catch (ValidacaoException e) {
                    e.printStackTrace();
                    Utils.mostrarMensagemCurta(CadastroPromocaoActivity.this, e.getMessage());
                }
            }
        });

        btnEditarPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Promocao promocao = new Promocao();
                    promocao.setTitulo(mTitulo.getText().toString());
                    promocao.setDescricao(mDescricao.getText().toString());
                    promocao.setValor(mValor.getText().toString());

                    VerificadorDeObjetos.vDadosPromocao(promocao,CadastroPromocaoActivity.this);

                    if(!mPromocao.equals(promocao)){
                        promocao.setId(mPromocao.getId());
                        promocao.setFornecedorId(mPromocao.getFornecedorId());
                        promocao.setDatas(mPromocao.getDatas());
                        PromocaoDao promocaoDao = new PromocaoDaoImpl(CadastroPromocaoActivity.this);
                        promocaoDao.atualizar(promocao,mIdUsuarioLogado);
                    }
                    abrirTelaPrincipal();

                }catch (ValidacaoException e){
                    e.printStackTrace();
                    Utils.mostrarMensagemLonga(CadastroPromocaoActivity.this, e.getMessage());
                }
            }
        });
        Intent intent = getIntent();
        if(intent.hasExtra("promocao")){
            toolbar.setTitle("Editar Promocao");
            btnEditarPromocao.setVisibility(View.VISIBLE);
            mPromocao = (Promocao) intent.getSerializableExtra("promocao");
            mEdicao = true;
            setvaluesOnViews();
        }
    }

    private void setvaluesOnViews() {
        if(mPromocao != null){
            mTitulo.setText(mPromocao.getTitulo());
            mValor.setText(mPromocao.getValor()+"");
            mDescricao.setText(mPromocao.getDescricao());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void confirmarSaida(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        CadastroPromocaoActivity.super.onBackPressed();
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

    @Override
    public void onBackPressed(){
        if (verificarCamposPreenchidos() && mIsViewsHabilitadas) confirmarSaida();
        else{
            //CadastroServicoActivity.super.onBackPressed();
            this.abrirTelaPrincipal();
            finish();
        }
    }

    private boolean verificarCamposPreenchidos(){
        return (!mDescricao.getText().toString().isEmpty() ||
                !mTitulo.getText().toString().isEmpty() ||
                !mValor.getText().toString().isEmpty());
    }

    private void abrirTelaPrincipal() {
//        Intent intent = new Intent(CadastroPromocaoActivity.this, CadastroServicoActivity.class);
//        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }

    private void abrirCalendario(){

        if(mEdicao){
            Intent intent = new Intent(CadastroPromocaoActivity.this, CalendarioPromocoesViewActivity.class);
            intent.putExtra("promocao",mPromocao);
            intent.putExtra("edicao",mEdicao);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(CadastroPromocaoActivity.this, CalendarioPromocoesPickerActivity.class);
            intent.putExtra("promocao",mPromocao);
            startActivity(intent);
            finish();
        }

    }

    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}