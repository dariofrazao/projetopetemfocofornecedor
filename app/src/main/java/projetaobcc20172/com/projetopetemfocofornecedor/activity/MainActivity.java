package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Preferencias;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;


public class MainActivity extends AppCompatActivity {

    private TextView mTvTitulo, mTvSubtitulo, mTvSubtitulo2;
    private FirebaseAuth mAutenticacao;
    private DatabaseReference mFirebase;
    private Fornecedor fornecedor;

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
        meusServicos = findViewById(R.id.btnMeusServicos);

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
                String id = (String) dataSnapshot.child("id").getValue();
                String nome = (String) dataSnapshot.child("nome").getValue();
                String nomeBusca = (String) dataSnapshot.child("nomeBusca").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                String senha = (String) dataSnapshot.child("senha").getValue();
                String cpfCnpj = (String) dataSnapshot.child("cpfCnpj").getValue();
                String telefone = (String) dataSnapshot.child("telefone").getValue();
                String horarios = (String) dataSnapshot.child("horarios").getValue();
                Endereco endereco = (Endereco) dataSnapshot.child("endereco").getValue(Endereco.class);
                mTvTitulo.setText(nome);
                mTvSubtitulo.setText("E-mail: " + email);
                mTvSubtitulo2.setText("Fone: " + telefone);
                Preferencias p = new Preferencias(MainActivity.this);
                p.salvarDadosUser(idUsuarioLogado, nome, email);

                fornecedor = new Fornecedor(nome, nomeBusca, email, cpfCnpj, telefone, senha, senha, horarios);
                fornecedor.setEndereco(endereco);
                fornecedor.setId(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
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
        if(id == R.id.action_editar){

            Intent intent = new Intent(MainActivity.this,EditarFornecedorActivity.class);
            intent.putExtra("fornecedor", fornecedor);
            startActivity(intent);
        }
        return true;
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
