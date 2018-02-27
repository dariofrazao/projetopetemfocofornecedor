package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.app.DatePickerDialog;
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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.CupomDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MascaraDinheiro;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by LuizAlberes on 15/02/2018.
 */

public class CadastroCupomActivity extends AppCompatActivity  implements Serializable{

    //@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)//permite que essa variavel seja vista pela classe de teste
    private EditText mEtNome, mEtValor, mEtDataInicio, mEtDataVencimento;
    private DatePickerDialog mDatePickerDialog;
    private Cupom mCupom;
    private String mIdUsuarioLogado;
    private List<String> mServicosCupom, mServicosCupomKey, mServicosCupomKeyFornecedor;
    private Spinner mSpinner;
    //private boolean mIsViewsHabilitadas = true;
    private String mKey, mKeyFornecedor;
    private String mJuncao = "";
    private int mPosicao = 0;
    private String mDataAtual;
    public Button mCadastrarCupom;
    private String mHabilita = "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cupom);
        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_cadastro_cupom);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("id", "");

        toolbar.setTitle("Cadastro do Código");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mEtNome = findViewById(R.id.etCadastroNomeCupom);
        mEtNome.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        mEtValor = findViewById(R.id.etCadastroValorCupom);
        Locale mLocal = new Locale("pt", "BR");
        mEtValor.addTextChangedListener(new MascaraDinheiro(mEtValor, mLocal));
        mEtValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtDataInicio = findViewById(R.id.etCadastroDataCupomInicio);
        mEtDataInicio.setShowSoftInputOnFocus(false);
        mEtDataInicio.setInputType(InputType.TYPE_NULL);
        mEtDataInicio.setFocusable(false);

        mEtDataVencimento = findViewById(R.id.etCadastroDataCupomVencimento);
        mEtDataVencimento.setShowSoftInputOnFocus(false);
        mEtDataVencimento.setInputType(InputType.TYPE_NULL);
        mEtDataVencimento.setFocusable(false);

        mCadastrarCupom = findViewById(R.id.btnCadastrarCupom);

        mServicosCupom = new ArrayList<String>();
        mServicosCupomKey = new ArrayList<String>();
        mServicosCupomKeyFornecedor = new ArrayList<String>();
        mServicosCupom.add("Selecione um serviço");
        mServicosCupomKey.add("0");
        mServicosCupomKeyFornecedor.add("0");

        mSpinner = (Spinner) findViewById(R.id.spCadastroServicoCupom);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mServicosCupom);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mServicosCupom.get(position).equals("Selecione um serviço")){
//                    //vazio
//                } else {
                    mKey = mServicosCupomKey.get(position).toString();
                    mKeyFornecedor = mServicosCupomKeyFornecedor.get(position).toString();
                    mJuncao = mServicosCupom.get(position).toString();
               // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Monta listview e mAdapter

        getDateFromActivityListener();

        mCadastrarCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarCupom();
            }
        });

        this.carregarServicosSpinner();
    }

    private void carregarServicosSpinner(){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("servicos").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    String nomeServico = dados.child("nome").getValue(String.class);
                    String valorServico = dados.child("valor").getValue(String.class);
                    String petServico = dados.child("tipoPet").getValue(String.class);
                    String juncao = nomeServico + " - " + petServico + " - " + valorServico;
                    mServicosCupomKey.add(dados.getKey());
                    mServicosCupomKeyFornecedor.add(dados.child("idFornecedor").getValue(String.class));
                    mServicosCupom.add(juncao);
                }

                if (mServicosCupom.size() == 1){
                    perguntaCriarServico();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }

    private void posicaoSpinner(final Cupom cupom){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("cupons").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if (dados.child("juncao").getValue().equals(mCupom.getJuncao())) {
                        Log.d("USER KEY test", dados.getValue().toString());
                        //mStringSpinner = dados.child("juncao").getValue(String.class);
                        //Log.d("USER KEY POS for", String.valueOf(mPosicao));
                        break;
                    }
                    mPosicao = mPosicao + 1;
                    //Log.d("USER KEY POS fora for", String.valueOf(mPosicao));
                }
                //mSpinner.setSelection(mPosicao);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }

    private void salvarCupom(){
        try {

            //Recuperar id do fornecedor logado
            mIdUsuarioLogado = getPreferences("id", CadastroCupomActivity.this);

            mCupom = new Cupom();
            mCupom.setNome(mEtNome.getText().toString());
            mCupom.setValor(mEtValor.getText().toString());
            mCupom.setDataInicio(mEtDataInicio.getText().toString());
            mCupom.setDataVencimento(mEtDataVencimento.getText().toString());
            mCupom.setIdServico(mKey);
            mCupom.setIdFornecedor(mKeyFornecedor);
            mCupom.setJuncao(mJuncao);

            VerificadorDeObjetos.vDadosCupom(mCupom,this);

            vData(mEtDataInicio.getText().toString(), mEtDataVencimento.getText().toString() ,mDataAtual, CadastroCupomActivity.this);

            mCadastrarCupom.setText("Aguarde...");
            mCadastrarCupom.setEnabled(false);

            //Chamada do DAO para salvar no banco
            CupomDaoImpl cupomDao =  new CupomDaoImpl(this);
            cupomDao.compararInserir(mCupom, mIdUsuarioLogado );

            ativarButtonAguarde(mCupom, mCadastrarCupom);


        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }
    }

    private void ativarButtonAguarde(final Cupom cpn, final Button btn){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("cupons").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if (dados.child("nome").getValue().equals(cpn.getNome()) &
                            dados.child("idServico").getValue().equals(cpn.getIdServico())){
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

    private static void vData (String dataInicio, String dataVencimento, String dataAtual, Context cad) throws ValidacaoException{

        String a = dataInicio;
        String b = dataVencimento;
        String c = dataAtual;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date data1 = new Date(format.parse(a).getTime());
            Date data2 = new Date(format.parse(b).getTime());
            Date data3 = new Date(format.parse(c).getTime());

            if(data1.before(data3)) {
                throw new ValidacaoException(cad.getString(R.string.data_inicio_antes));
            } else if (data2.before(data3)){
                throw new ValidacaoException(cad.getString(R.string.data_vencimento_antes));
            } else if (data1.after(data2)) {
                throw new ValidacaoException(cad.getString(R.string.data_inicio_depois));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getDateFromActivityListener(){
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // current year
        final int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        //inicia o campo com a mData atual
        mEtDataInicio.setText(String.format("%d/%d/%d", mDay, mMonth + 1, mYear));
        mEtDataVencimento.setText(String.format("%d/%d/%d", mDay, mMonth + 1, mYear));
        mDataAtual = mEtDataInicio.getText().toString();

        mEtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(CadastroCupomActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // set day of month , month and year value in the edit text
                                mEtDataInicio.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, mYear, mMonth, mDay);

                mDatePickerDialog.show();
            }
        });

        mEtDataVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(CadastroCupomActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // set day of month , month and year value in the edit text
                                mEtDataVencimento.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
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

    @Override
    public void onBackPressed(){
        if (verificarCamposPreenchidos()) {
            confirmarSaida();
        }
        else{
            CadastroCupomActivity.super.onBackPressed();
            finish();
            //getSupportFragmentManager().beginTransaction()
//                    .add(R.id.content_main, new CupomActivity())
//                    .commit();
        }
    }


    private boolean verificarCamposPreenchidos(){
        return (!mEtNome.getText().toString().isEmpty() ||
                !mEtValor.getText().toString().isEmpty());
    }

    public void confirmarSaida(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        CadastroCupomActivity.super.onBackPressed();
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

    public void perguntaCriarServico(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // Botão sim foi clicado
                    Intent intent = new Intent(CadastroCupomActivity.this, CadastroServicoActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Botão não foi clicado
                    CadastroCupomActivity.super.onBackPressed();
                    finish();
                    break;
                default:
                    break;
            }

            }

        };
        Utils.mostrarPerguntaSimNao(this, getString(R.string.atencao),
                getString(R.string.pergunta_confirma_criar_serviço), dialogClickListener,
                dialogClickListener);
    }

    //Método que recupera o id do fornecedor logado, para salvar o cupom no nó do fornecedor que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
