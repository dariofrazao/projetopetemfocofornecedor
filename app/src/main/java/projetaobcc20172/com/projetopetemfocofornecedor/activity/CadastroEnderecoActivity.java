package projetaobcc20172.com.projetopetemfocofornecedor.activity;
/**
 * Created by Alexsandro on 03/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.FornecedorDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.CampoObrAusenteException;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Util;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.ZipCodeListener;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MaskUtil;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Activity de cadastro de endereço
 */
public class CadastroEnderecoActivity extends AppCompatActivity{
    public static LatLng localizacao;
    private EditText mLogradouro, mNumero, mComplemento, mBairro, mLocalidade, mCep;
    private Spinner mSpinnerUf;
    private Fornecedor mFornecedor;
    private Util mUtil;
    private Endereco mEndereco;
    private String mIdUsuarioLogado;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    //permite que essa variavel seja vista pela classe de teste
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_endereco);

        ImageButton mImageButtonEditarGeolocalizacao;

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_endereco);

        mCep = findViewById(R.id.etCadastroCepEndereco);
        mLocalidade = findViewById(R.id.etCadastroLocalidadeEndereco);
        mLogradouro = findViewById(R.id.etCadastroLogradouroEndereco);
        mNumero = findViewById(R.id.etCadastroNumeroEndereco);
        mComplemento = findViewById(R.id.etCadastroComplementoEndereco);
        mBairro = findViewById(R.id.etCadastroBairroEndereco);
        mImageButtonEditarGeolocalizacao = findViewById(R.id.ib_editar_localizacao_geografica);

        mCep.addTextChangedListener(new ZipCodeListener(this));
        mCep.addTextChangedListener(MaskUtil.mask(mCep, MaskUtil.FORMAT_CEP));

        //Preparar o adaptar do Spinner para exibir as UF's do Endereço
        mSpinnerUf = findViewById(R.id.ufSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.uf));
        mSpinnerUf.setAdapter(adapter_state);

        mUtil = new Util(this,
                R.id.etCadastroCepEndereco,
                R.id.etCadastroLogradouroEndereco,
                R.id.etCadastroLocalidadeEndereco,
                R.id.etCadastroNumeroEndereco,
                R.id.etCadastroComplementoEndereco,
                R.id.etCadastroBairroEndereco,
                R.id.ufSpinner);

        //Receber os dados do fornecedor da outra activity
        Intent i = getIntent();
        mFornecedor = (Fornecedor) i.getSerializableExtra("Fornecedor");

        Button mBtnCadastrarEndereco = findViewById(R.id.btnCadastrarEndereco);
        mBtnCadastrarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperar os campos do endereço informados pelo fornecedor
                mEndereco = new Endereco();
                mEndereco.setLogradouro(mLogradouro.getText().toString());
                mEndereco.setNumero(mNumero.getText().toString());
                mEndereco.setComplemento(mComplemento.getText().toString());
                mEndereco.setBairro(mBairro.getText().toString());
                mEndereco.setLocalidade(mLocalidade.getText().toString());
                mEndereco.setCep(mCep.getText().toString());
                mEndereco.setUf(mSpinnerUf.getSelectedItem().toString());

                cadastrarEnderecoFornecedor();

            }
        });

        mImageButtonEditarGeolocalizacao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editarGeolicalizacao(mFornecedor);
            }
        });

        // Configura toolbar
        toolbar.setTitle(R.string.tb_cadastro_endereco);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Método que recupera os dados básicos do fornecedor, adicionando o endereço e chamando o DAO para salvar no banco
    private void cadastrarEnderecoFornecedor(){
            try {
                //Recuperar id do fornecedor logado
                mIdUsuarioLogado = getPreferences("idFornecedor", CadastroEnderecoActivity.this);
                mEndereco.setmLatitude(localizacao.latitude);
                mEndereco.setmLongitude(localizacao.longitude);
                VerificadorDeObjetos.vDadosObrEndereco(mEndereco);
                mFornecedor.setEndereco(mEndereco);
                FornecedorDaoImpl fornecedorDao =  new FornecedorDaoImpl(this);

                //Chamada do DAO para salvar no banco
                fornecedorDao.inserir(mFornecedor, mIdUsuarioLogado);
                //salvarPreferencias("idFornecedor", mFornecedor.getId());
                abrirLoginFornecedor();

            } catch (CampoObrAusenteException e) {
                mToast = Toast.makeText(CadastroEnderecoActivity.this, R.string.erro_cadastro_endereco_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
                mToast.show();
            } catch (Exception e) {
                mToast = Toast.makeText(CadastroEnderecoActivity.this, R.string.erro_cadastro_endereco_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
                mToast.show();
            }
        }


    public void abrirLoginFornecedor() {
        Intent intent = new Intent(CadastroEnderecoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
/*
    //Método que salva o id do fornecedor nas preferências
    private void salvarPreferencias(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    */

    //Método que trava os campos de endereço enquanto a busca pelo cep é realizada
    public void lockFields (boolean isToLock){
        mUtil.lockFields(isToLock);
    }

    //Método que retorna o endereço completo a partir do cep informado
    public String getUriZipCode(){
        return "https://viacep.com.br/ws/"+mCep.getText()+"/json/";
    }

    //Método que insere nos campos de endereço as informações obtidas pela busca (pelo cep)
    public void setDataViews (Endereco mEndereco){
        setFields(R.id.etCadastroLocalidadeEndereco, mEndereco.getLocalidade());
        setFields(R.id.etCadastroBairroEndereco, mEndereco.getBairro());
        setFields(R.id.etCadastroLogradouroEndereco, mEndereco.getLogradouro());
        setFields(R.id.etCadastroComplementoEndereco, mEndereco.getComplemento());
        setSpinner(R.id.ufSpinner, R.array.uf, mEndereco.getUf());
    }

    private void setFields (int id, String data){
        ((EditText)findViewById(id)).setText(data);
    }

    private void setSpinner (int id, int arrayId, String data){
        String[] itens = getResources().getStringArray(arrayId);

        for(int i = 0; i < itens.length; i++){

            if(itens[i].equals(data)){
                ((Spinner)findViewById(id)).setSelection(i);
                return;
            }
        }
        ((Spinner)findViewById(id)).setSelection(0);

    }

    //Método que recupera o id do fornecedor logado, para salvar o endereço no nó do fornecedor que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    //Método que chama a activity para editar geolocalização passando o o fornecedor
    public void editarGeolicalizacao(Fornecedor fornecedor){
        Intent intent = new Intent(CadastroEnderecoActivity.this, CadastroGeolocalizacaoActivity.class);
//        intent.putExtra("Fornecedor", fornecedor);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        // Salva o estado atual do jogo do usuário
        savedInstanceState.putAll(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
