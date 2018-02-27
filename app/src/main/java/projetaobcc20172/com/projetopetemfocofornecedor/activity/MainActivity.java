package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Preferencias;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , Serializable {

    private TextView mNome, mEmail;
    private FirebaseAuth mAutenticacao;
    private Fornecedor fornecedor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//toolbar do navigator
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  (NavigationView)findViewById(R.id.nav_busca);

        displaySelectedScreen(R.id.nav_servicos);//Determina qual tela será aberta primeiro ao entrar

        View header = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        mNome = header.findViewById(R.id.tvNomeProfile);
        //mFoto = header.findViewById(R.id.imageViewProfile);
        mEmail = header.findViewById(R.id.tvEmailProfile);

        //Recuperar id do usuário logado
        String mIdUsuarioLogado;
        mIdUsuarioLogado = getPreferences("id", getApplication());
//
//        DatabaseReference mReferenciaFirebase;
//        mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
//        mReferenciaFirebase.child("usuarios").child(mIdUsuarioLogado).addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.getValue() != null){
//                    Fornecedor fornecedor = dataSnapshot.getValue(Fornecedor.class);
//                    //Exibe a foto de perfil do usuário através do Glide
//                    Glide.with(getApplicationContext()).load(fornecedor.getmFoto()).asBitmap().into(new BitmapImageViewTarget(mFoto){
//                        @Override
//                        protected void setResource(Bitmap resource) {
//
//                            //Transforma a foto em formato circular
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            mFoto.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//
//                    mNome.setText(fornecedor.getNome());
//                    mEmail.setText(fornecedor.getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //vazio
//            }
//        });


        // Recuperar serviços do Firebase
        DatabaseReference mFirebase = ConfiguracaoFirebase.getFirebase().child("fornecedor").child(mIdUsuarioLogado);

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
                Endereco end = (Endereco)dataSnapshot.child("endereco").getValue(Endereco.class);
                mNome.setText(nome);
                mEmail.setText(email);
                //mTvSubtitulo2.setText("Fone: " + telefone);
                Preferencias p = new Preferencias(MainActivity.this);
                p.salvarPosicao((float)end.getmLatitude(),(float)end.getmLongitude());
                p.salvarNota(dataSnapshot.child("nota").getValue(Float.class));
                p.salvarDadosUser(mIdUsuarioLogado, nome, email);

                fornecedor = new Fornecedor(nome, nomeBusca, email, cpfCnpj, telefone, horarios);
                fornecedor.setEndereco(end);
                fornecedor.setId(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }


    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_perfil:
                fragment = new EditarFornecedorActivity();
                break;
            case R.id.nav_servicos:
                fragment = new ServicosActivity();
                break;
            case R.id.nav_cupons:
                fragment = new CupomActivity();
                break;
            case R.id.nav_promocoes:
                fragment = new PromocaoActivity();
                break;
            case R.id.nav_sair:
                this.deslogarFornecedor();
                break;
            default:
                break;
        }
        this.fecharTeclado();

        //Instancia o fragmento
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void fecharTeclado(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //Método para deslogar fornecedor da aplicação e retornar a tela de Login
    private void deslogarFornecedor(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
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
