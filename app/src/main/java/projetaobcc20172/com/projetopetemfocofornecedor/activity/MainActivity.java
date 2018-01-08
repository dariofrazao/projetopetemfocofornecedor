package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTitulo, mTvSubtitulo, mTvSubtitulo2;
    private FirebaseAuth mAutenticacao;
    private DatabaseReference mFirebase;
    private static FirebaseDatabase mDatabase;
    Fornecedor fornecedorSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        Button sair;
        Button meusServicos;

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);
        final Fornecedor fornecedorAtual;

        // Configura toolbar
        toolbar.setTitle("Pet Em Foco");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        sair = findViewById(R.id.botao_sair);
        meusServicos =  findViewById(R.id.botao_meus_servicos);

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
                mTvTitulo.setText(nome);
                mTvSubtitulo.setText("E-mail: " + email);
                mTvSubtitulo2.setText("Fone: " + telefone);
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


        toolbar.setOnMenuItemClickListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    //chama o menu de edição de perfil de fornecedor
    @Override
    @SuppressLint("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.layout.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_editar){
            return true;
        }
        if (id == R.id.action_eliminar){
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }
}
