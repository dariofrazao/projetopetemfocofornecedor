package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Base64Custom;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mSenha;
    private FirebaseAuth mAutenticacao;
    private Fornecedor mFornecedor;
    private String mIdentificadorUserLogado;
    private Toast mToast;
    //private static Boolean loginAutomatico = false;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Verifica se o fornecedor já está logado
        //if(loginAutomatico){
        verificarFornecedorLogado();
        //}


        mEmail = findViewById(R.id.etLoginEmail);
        mSenha = findViewById(R.id.etLoginSenha);
        Button mLogin;
        Button mCadastrarFornecedor;
        mLogin = findViewById(R.id.btnLogin);
        mCadastrarFornecedor = findViewById(R.id.btnCadastrarFornecedor);

        mCadastrarFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroFornecedorActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFornecedor = new Fornecedor();
                mFornecedor.setEmail( mEmail.getText().toString() );
                mFornecedor.setSenha( mSenha.getText().toString() );
                exibirProgresso();
                validarLogin();

            }
        });
    }

    //Método que valida o mLogin do fornecedor junto ao Firebase
    private void validarLogin(){
        try {
            mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            mFirebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
            mAutenticacao.signInWithEmailAndPassword(
                    mFornecedor.getEmail(),
                    mFornecedor.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //Se o mLogin foi realizado com sucesso
                    if (task.isSuccessful()) {
                        mIdentificadorUserLogado = Base64Custom.codificarBase64(mFornecedor.getEmail());

                        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Se o usuário que fez login for um consumidor, exibirá mensagem de erro
                                if (dataSnapshot.child(mIdentificadorUserLogado).child("enderecoUsuario").exists()) {
                                    mToast = Toast.makeText(LoginActivity.this, R.string.erro_login_invalido_Toast, Toast.LENGTH_SHORT);
                                    mToast.show();
                                    mProgressDialog.dismiss();
                                }
                                //Se o usuário não for um consumidor, então será um fornecedor e abrirá a tela principal
                                else {
                                    mToast = Toast.makeText(LoginActivity.this,R.string.sucesso_login_Toast, Toast.LENGTH_SHORT);
                                    mToast.show();
                                    mProgressDialog.dismiss();
                                    //Salva o id do fornecedor logado nas preferências
                                    salvarPreferenciasFornecedor("idFornecedor", mIdentificadorUserLogado);
                                    abrirTelaPrincipalFornecedor();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //vazio
                            }
                        });
                    } else {
                        mToast = Toast.makeText(LoginActivity.this, R.string.erro_login_invalido_Toast, Toast.LENGTH_SHORT);
                        mToast.show();
                        mProgressDialog.dismiss();
                    }

                }
            });
        }catch (Exception e){
            mToast = Toast.makeText(LoginActivity.this, R.string.erro_login_invalido_Toast, Toast.LENGTH_SHORT);
            mToast.show();
            mProgressDialog.dismiss();
        }
    }

    private void abrirTelaPrincipalFornecedor(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

    //Método que verifica se o fornecedor já está logado no app
    private void verificarFornecedorLogado(){
        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if( mAutenticacao.getCurrentUser() != null && getPreferencesKeyFornecedor(this)){
            abrirTelaPrincipalFornecedor();
        }
    }

    /*public static void setLoginAutomatico(Boolean login){
        loginAutomatico = login;
    }*/

    //Método que exibe o progresso do mLogin
    private void exibirProgresso() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Verificando Dados...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    //Método que salva o id do fornecedor nas preferências para login automático ao abrir aplicativo
    private void salvarPreferenciasFornecedor(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Método que recupera o id do fornecedor logado
    public static boolean getPreferencesKeyFornecedor(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idFornecedor");
    }

}
