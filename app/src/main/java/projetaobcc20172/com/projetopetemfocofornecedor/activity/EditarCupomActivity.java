package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDao;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MascaraDinheiro;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by LuizAlberes on 24/02/2018.
 */

public class EditarCupomActivity extends AppCompatActivity {

    //@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)//permite que essa variavel seja vista pela classe de teste
    private EditText mEtNome, mEtValor, mEtDataInicio, mEtDataVencimento;
    private DatePickerDialog mDatePickerDialog;
    private Cupom mCupom;
    private String mIdUsuarioLogado;
    private List<String> mServicosCupom, mServicosCupomKey, mServicosCupomKeyFornecedor;
    private Spinner mSpinner;
    private String mKey, mKeyFornecedor;
    private String mJuncao = "";
    private int mPosicao = 0;
    private String mDataAtual;
    public Button btnEditarCupom;
    private String mHabilita = "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cupom);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_editar_cupom);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("idFornecedor", "");

        mCupom = (Cupom) getIntent().getSerializableExtra("Cupom");

        // Configura toolbar
        toolbar.setTitle("Editar Código");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mEtNome = findViewById(R.id.etEdicaoNomeCupom);
        mEtNome.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        mEtNome.setText(mCupom.getNome());
        mEtValor = findViewById(R.id.etEdicaoValorCupom);
        Locale mLocal = new Locale("pt", "BR");
        mEtValor.addTextChangedListener(new MascaraDinheiro(mEtValor, mLocal));
        mEtValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtValor.setText(mCupom.getValor());
        mEtDataInicio = findViewById(R.id.etEdicaoDataCupomInicio);
        mEtDataInicio.setShowSoftInputOnFocus(false);
        mEtDataInicio.setInputType(InputType.TYPE_NULL);
        mEtDataInicio.setFocusable(false);
        mEtDataInicio.setText(mCupom.getDataInicio());

        mEtDataVencimento = findViewById(R.id.etEdicaoDataCupomVencimento);
        mEtDataVencimento.setShowSoftInputOnFocus(false);
        mEtDataVencimento.setInputType(InputType.TYPE_NULL);
        mEtDataVencimento.setFocusable(false);
        mEtDataVencimento.setText(mCupom.getDataVencimento());

        btnEditarCupom = findViewById(R.id.btnEditarCupom);
        btnEditarCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chamar o método para editar o pet no banco
                editarPet();
            }
        });

        getDateFromActivityListener();

        mServicosCupom = new ArrayList<String>();
        mServicosCupomKey = new ArrayList<String>();
        mServicosCupomKeyFornecedor = new ArrayList<String>();
        mServicosCupom.add("Selecione um serviço");
        mServicosCupomKey.add("0");
        mServicosCupomKeyFornecedor.add("0");

        mSpinner = (Spinner) findViewById(R.id.spEdicaoServicoCupom);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mServicosCupom);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mServicosCupom.get(position).equals("Selecione um serviço")){
                    //vazio
                } else {
                    mKey = mServicosCupomKey.get(position).toString();
                    mKeyFornecedor = mServicosCupomKeyFornecedor.get(position).toString();
                    mJuncao = mServicosCupom.get(position).toString();
                    //Log.d("USER KEY", String.valueOf(mPosicao));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    perguntaCriarServicoEditar();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }


    private boolean verificarCamposPreenchidos(){
        return (!mEtNome.getText().toString().isEmpty() ||
                !mEtValor.getText().toString().isEmpty());
    }

    //Método que edita o pet no banco
    private void editarPet(){
        try {
            //Recuperar id do fornecedor logado
            mIdUsuarioLogado = getPreferences("idFornecedor", EditarCupomActivity.this);

            Cupom cupom = new Cupom();
            cupom.setNome(mEtNome.getText().toString());
            cupom.setValor(mEtValor.getText().toString());
            cupom.setDataInicio(mEtDataInicio.getText().toString());
            cupom.setDataVencimento(mEtDataVencimento.getText().toString());
            cupom.setJuncao(mJuncao);

            VerificadorDeObjetos.vDadosCupom(cupom,this);

            vData(mEtDataInicio.getText().toString(), mEtDataVencimento.getText().toString() ,mDataAtual, EditarCupomActivity.this);

            btnEditarCupom.setText("Aguarde...");
            btnEditarCupom.setEnabled(false);

            if(!mCupom.equals(cupom)){
                cupom.setId(mCupom.getId());
                cupom.setIdServico(mKey);
                cupom.setIdFornecedor(mKeyFornecedor);
                CupomDaoImpl cupomDao = new CupomDaoImpl(EditarCupomActivity.this);
                cupomDao.compararAtualizar(cupom,mIdUsuarioLogado);
                ativarButtonAguarde(cupom, btnEditarCupom);
            }

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
        //mEtDataInicio.setText(String.format("%d/%d/%d", mDay, mMonth + 1, mYear));
        //mEtDataVencimento.setText(String.format("%d/%d/%d", mDay, mMonth + 1, mYear));
        mDataAtual = String.format("%d/%d/%d", mDay, mMonth + 1, mYear);

        mEtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(EditarCupomActivity.this,
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
                mDatePickerDialog = new DatePickerDialog(EditarCupomActivity.this,
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
        else EditarCupomActivity.super.onBackPressed();
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


    public void perguntaCriarServicoEditar(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        Intent intent = new Intent(EditarCupomActivity.this, CadastroServicoActivity.class);
                        intent.putExtra("Cupom", mCupom);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // Botão não foi clicado
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


}
