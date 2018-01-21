package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
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
    private EditText mData;
    private EditText mDescricao;
    private EditText mValor;
    private DatePickerDialog mDatePickerDialog;
    private boolean mIsViewsHabilitadas = true;
    private String mIdUsuarioLogado;
    private Promocao mPromocao;


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
        mData = findViewById(R.id.etData);
        mData.setShowSoftInputOnFocus(false);
        mData.setInputType(InputType.TYPE_NULL);
        mData.setFocusable(false);

        Button btnCadastrarPromocao = findViewById(R.id.btnCadastroPromocao);
        Button btnEditarPromocao = findViewById(R.id.btnEditarPromocao);
        //funcao responsavel por preencher o campo de mData com a mData selecionada do datapicker
        getDateFromActivityListener();

        btnCadastrarPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarPromocao();
            }
        });

        btnEditarPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Promocao promocao = new Promocao();
                    promocao.setTitulo(mTitulo.getText().toString());
                    promocao.setData(mData.getText().toString());
                    promocao.setDescricao(mDescricao.getText().toString());
                    promocao.setValor(mValor.getText().toString());

                    VerificadorDeObjetos.vDadosPromocao(promocao,CadastroPromocaoActivity.this);

                    if(!mPromocao.equals(promocao)){
                        promocao.setId(mPromocao.getId());
                        promocao.setFornecedorId(mPromocao.getFornecedorId());
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
            btnCadastrarPromocao.setVisibility(View.GONE);
            btnEditarPromocao.setVisibility(View.VISIBLE);
            mPromocao = (Promocao) intent.getSerializableExtra("promocao");
            setvaluesOnViews();
        }
    }

    private void setvaluesOnViews() {
        if(mPromocao != null){
            mTitulo.setText(mPromocao.getTitulo());
            mValor.setText(mPromocao.getValor()+"");
            mDescricao.setText(mPromocao.getDescricao());
            mData.setText(mPromocao.getData());
        }
    }

    private void salvarPromocao(){
        try {
            mIdUsuarioLogado = getPreferences("idFornecedor", CadastroPromocaoActivity.this);
            mPromocao = new Promocao();
            mPromocao.setTitulo(mTitulo.getText().toString());
            mPromocao.setValor(mValor.getText().toString());
            mPromocao.setDescricao(mDescricao.getText().toString());
            mPromocao.setData(mData.getText().toString());
            mPromocao.setFornecedorId(mIdUsuarioLogado);
            VerificadorDeObjetos.vDadosPromocao(mPromocao,this);
            //Chamada do DAO para salvar no banco
            PromocaoDao servicoDao =  new PromocaoDaoImpl(this);
            servicoDao.inserir(mPromocao, mIdUsuarioLogado);
            abrirTelaPrincipal();

        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }

    }

    private void getDateFromActivityListener(){
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // current year
        final int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        //inicia o campo com a mData atual
        mData.setText(String.format("%d/%d/%d", mDay, mMonth + 1, mYear));

        mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(CadastroPromocaoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // set day of month , month and year value in the edit text
                                mData.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, mYear, mMonth, mDay);

                mDatePickerDialog.show();
            }
        });
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
        Intent intent = new Intent(CadastroPromocaoActivity.this, PromocaoActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}