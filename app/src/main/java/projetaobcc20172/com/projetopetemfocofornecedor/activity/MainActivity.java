package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Preferencias;


public class MainActivity extends AppCompatActivity {

    private TextView mTvTitulo, mTvSubtitulo, mTvSubtitulo2;
    private FirebaseAuth mAutenticacao;
    private DatabaseReference mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        Button sair;
        Button meusServicos;

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Pet Em Foco");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        sair = findViewById(R.id.btnSair);
        meusServicos =  findViewById(R.id.btnMeusServicos);

        mTvTitulo = findViewById(R.id.tvTituloFornecedor);
        mTvSubtitulo = findViewById(R.id.tvSubtituloFornecedor);
        mTvSubtitulo2 = findViewById(R.id.tvSubtitulo2Fornecedor);

        //Recuperar id do fornecedor logado
        final String idUsuarioLogado;
        idUsuarioLogado = getPreferences("idFornecedor", this);

        // Recuperar serviços do Firebase
        mFirebase = ConfiguracaoFirebase.getFirebase().child("fornecedor").child(idUsuarioLogado);

        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nome = (String)dataSnapshot.child("nome").getValue();
                String email = (String)dataSnapshot.child("email").getValue();
                String telefone = (String)dataSnapshot.child("telefone").getValue();
                double latitude = (double)dataSnapshot.child("mLatitude").getValue();
                double longitude = (double)dataSnapshot.child("mLongitude").getValue();
                mTvTitulo.setText(nome);
                mTvSubtitulo.setText("E-mail: " + email);
                mTvSubtitulo2.setText("Fone: " + telefone);
                Preferencias p = new Preferencias(MainActivity.this);
                p.salvarPosicao((float)latitude,(float)longitude);
                p.salvarDadosUser(idUsuarioLogado,nome,email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        meusServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ServicosActivity.class);
                startActivity(intent);
            }
        });

        //Ação do botão de deslogar o fornecedor
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deslogarFornecedor();
            }
        });

    }

    //Método para deslogar fornecedor da aplicação e retornar a tela de Login
    private void deslogarFornecedor(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("idFornecedor");
        editor.apply();
        mAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Método que recupera o id do fornecedor logado, para salvar o endereço no nó do fornecedor que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
