package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Base64Custom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MaskUtil;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by renat on 02/12/2017.
 */

/**
 * Activity de cadastro de fornecedor
 */
public class EditarFornecedorActivity extends AppCompatActivity {

    private EditText mTelefone;
    private Spinner mSpinnerHorarios;
    private Fornecedor mFornecedor;
    private FirebaseAuth mAutenticacao;

    //permite que essa variavel seja vista pela classe de teste
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fornecedor);

        Intent intent = getIntent();

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_cadastro_fornecedor);

        mTelefone = findViewById(R.id.etCadastroTelefoneFornecedor);
        mTelefone.addTextChangedListener(MaskUtil.mask(mTelefone, MaskUtil.FORMAT_FONE));
        mTelefone.setText(intent.getStringExtra("telefone"));

        Button botaoEditar;
        botaoEditar = findViewById(R.id.botao_editar_fornecedor);

        //Preparar o adaptar do Spinner para exibir os horários de atendimento do fornecedor
        mSpinnerHorarios = findViewById(R.id.horariosSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.horariosFornecedor));
        mSpinnerHorarios.setAdapter(adapter_state);
        String horarios = intent.getStringExtra("horarios");
        int posicaoHorarios = adapter_state.getPosition(horarios);
        mSpinnerHorarios.setSelection(posicaoHorarios);

        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFornecedor = new Fornecedor();
                mFornecedor.setTelefone(mTelefone.getText().toString());
                mFornecedor.setHorarios(mSpinnerHorarios.getSelectedItem().toString());
                cadastrarFornecedor();
            }
        });

        // Configura toolbar
        toolbar.setTitle(R.string.tb_editar_fornecedor);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Método para cadastrar o fornecedor no FirebaseAuthentication
    private void cadastrarFornecedor() {
        try {
            VerificadorDeObjetos.vDadosFornecedor(mFornecedor, this);
            mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            mAutenticacao.createUserWithEmailAndPassword(
                    mFornecedor.getEmail(),
                    mFornecedor.getSenha()
            ).addOnCompleteListener(EditarFornecedorActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        String identificadorFornecedor = Base64Custom.codificarBase64(mFornecedor.getEmail());
                        mFornecedor.setId(identificadorFornecedor);
                        mToast = Toast.makeText(EditarFornecedorActivity.this, R.string.sucesso_cadastro_proxima_etapa_Toast, Toast.LENGTH_SHORT);
                        mToast.show();
                        //Aqui será chamado a continuação do cadastro do fornecedor, levando-o ao cadastro do endereço
                        abrirCadastroEndereco(mFornecedor);
                    } else {

                        String erro = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = getResources().getString(R.string.erro_cadastro_fornecedor_senha_invalida_Toast);
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = getResources().getString(R.string.erro_cadastro_fornecedor_email_invalido_Toast);
                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = getResources().getString(R.string.erro_cadastro_fornecedor_email_usado_Toast);
                        } catch (FirebaseNetworkException e) {
                            erro = getResources().getString(R.string.erro_cadastro_fornecedor_sem_conexao);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mToast = Toast.makeText(EditarFornecedorActivity.this, erro, Toast.LENGTH_SHORT);
                        mToast.show();
                    }

                }
            });
        } catch (ValidacaoException e) {
            e.printStackTrace();
            Utils.mostrarMensagemCurta(this, e.getMessage());
        }
    }

    //Método que chama a activity para cadastrar o endereço, passando os dados básicos aqui cadastrados
    public void abrirCadastroEndereco(Fornecedor fornecedor){
        mAutenticacao.signOut();
        Intent intent = new Intent(EditarFornecedorActivity.this, CadastroEnderecoActivity.class);
        intent.putExtra("Fornecedor", fornecedor);
        startActivity(intent);
        finish();
    }

}
