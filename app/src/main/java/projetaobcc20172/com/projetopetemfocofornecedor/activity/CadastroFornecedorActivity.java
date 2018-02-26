package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MaskUtil;

/**
 * Created by renat on 02/12/2017.
 */

/**
 * Activity de cadastro de fornecedor
 */
public class CadastroFornecedorActivity extends AppCompatActivity {

    private EditText mNome, mEmail, mTelefone, mCpfCnpj;
    private Spinner mSpinnerHorarios;
    private Fornecedor mFornecedor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fornecedor);

        mFornecedor = (Fornecedor) getIntent().getSerializableExtra("fornecedor");

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_cadastro_fornecedor);
        mNome = findViewById(R.id.etCadastroNomeFornecedor);
        mEmail = findViewById(R.id.etCadastroEmailFornecedor);
        mTelefone = findViewById(R.id.etCadastroTelefoneFornecedor);
        mTelefone.addTextChangedListener(MaskUtil.mask(mTelefone, MaskUtil.FORMAT_FONE));
        mCpfCnpj = findViewById(R.id.etCadastroCpfCnpjFornecedor);

        mCpfCnpj.addTextChangedListener(getMascara(mFornecedor.getTipo()));
        Button botaoCadastrar;
        botaoCadastrar = findViewById(R.id.btnCadastrarFornecedor);

        mNome.setText(mFornecedor.getNome());
        mNome.setShowSoftInputOnFocus(false);
        mNome.setInputType(InputType.TYPE_NULL);
        mNome.setFocusable(false);

        mEmail.setText(mFornecedor.getEmail());
        mEmail.setShowSoftInputOnFocus(false);
        mEmail.setInputType(InputType.TYPE_NULL);
        mEmail.setFocusable(false);

        //Preparar o adaptar do Spinner para exibir os horários de atendimento do fornecedor
        mSpinnerHorarios = findViewById(R.id.servicoSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.horariosFornecedor));
        mSpinnerHorarios.setAdapter(adapter_state);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFornecedor.setNome(mNome.getText().toString() );
                mFornecedor.setNomeBusca(mNome.getText().toString().toLowerCase());
                mFornecedor.setTelefone(mTelefone.getText().toString());
                mFornecedor.setCpfCnpj(mCpfCnpj.getText().toString());
                mFornecedor.setHorarios(mSpinnerHorarios.getSelectedItem().toString());


                abrirCadastroEndereco();
            }
        });

        // Configura toolbar
        toolbar.setTitle(R.string.tb_cadastro_fornecedor);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);
    }

    private TextWatcher getMascara(String tipoFornecedor){
        TextView tvCadastroCpfCnpjFornecedor = findViewById(R.id.tvCadastroCpfCnpjFornecedor);

        String mascara = "";
        if("Autônomo".equals(tipoFornecedor)){
            tvCadastroCpfCnpjFornecedor.setText("CPF");
            mCpfCnpj.setHint("Digite seu CPF");
            mascara = MaskUtil.FORMAT_CPF;
        }else if("Estabelecimento".equals(tipoFornecedor)){
            tvCadastroCpfCnpjFornecedor.setText("CNPJ");
            mCpfCnpj.setHint("Digite seu CNPJ");
            mascara = MaskUtil.FORMAT_CNPJ;
        }
        return MaskUtil.mask(mCpfCnpj, mascara);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    //Método que chama a activity para cadastrar o endereço, passando os dados básicos aqui cadastrados
    public void abrirCadastroEndereco(){
        Intent intent = new Intent(CadastroFornecedorActivity.this, CadastroEnderecoActivity.class);
        intent.putExtra("Fornecedor", mFornecedor);
        startActivity(intent);
        finish();
    }

}
