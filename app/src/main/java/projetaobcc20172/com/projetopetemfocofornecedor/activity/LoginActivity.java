package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAutenticacao;
    private Fornecedor mFornecedor;
    //private static Boolean loginAutomatico = false;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        Button mLoginGoogle;

        mLoginGoogle = findViewById(R.id.btnLoginGoogle);

        //Configuração do Google Sign In para login pelo Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Utils.mostrarMensagemCurta(getApplicationContext(), getApplicationContext().getString(R.string.erro_login_invalido_Toast));
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Se o login pelo Google foi realizado com sucesso pela segunda vez na mesma sessão
            if (result.isSuccess() && getPreferencesKeyConsumidorGoogle(this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                salvarPreferencias("id", account.getId());
                Utils.mostrarMensagemCurta(getApplicationContext(), getApplicationContext().getString(R.string.sucesso_login_Toast));
                abrirTelaPrincipalFornecedor();

                //Se o login pelo Google foi realizado com sucesso pela primeira vez na sessão
            } else if (result.isSuccess() && !getPreferencesKeyConsumidorGoogle(this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                salvarPreferencias("idGoogle", account.getId());
                salvarUsuarioGoogle(account);


            } else {
                Utils.mostrarMensagemCurta(getApplicationContext(), getApplicationContext().getString(R.string.erro_login_invalido_Toast));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAutenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //vazio
                    }
                });
    }

    //Abrir tela principal do consumidor
    private void abrirTelaPrincipalFornecedor() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    //Método que verifica se o usuário já está logado no app
    private void verificarUsuarioLogado() {
        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Se já estiver logado pelo Google
        if (mAutenticacao.getCurrentUser() != null && getPreferencesKeyConsumidorVerificarLogado(this)) {
            abrirTelaPrincipalFornecedor();
        }
    }

    //Método que recupera o id do usuário logado para acessar seus dados e salvar suas informações no banco
    public static boolean getPreferencesKeyConsumidorVerificarLogado(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("id");
    }

    //Método que recupera o id do usuário logado pelo Google
    public static boolean getPreferencesKeyConsumidorGoogle(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idGoogle");
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Método que salva o usuário logado pelo Google no banco
    private boolean salvarUsuarioGoogle(GoogleSignInAccount account) {
        try {

            mFornecedor = new Fornecedor();
            mFornecedor.setId(account.getId());
            mFornecedor.setNome(account.getDisplayName());
            mFornecedor.setEmail(account.getEmail());

            salvarPreferencias("id", account.getId());

            //Chamada a intent para o resto do cadastro
            Intent intent = new Intent(LoginActivity.this, CadastroFornecedorTipoActivity.class);
            intent.putExtra("fornecedor", mFornecedor);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
