package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import projetaobcc20172.com.projetopetemfocofornecedor.R;

//import projetaobcc20172.com.projetopetemfoco.R;
//import projetaobcc20172.com.projetopetemfoco.config.ConfiguracaoFirebase;
//import projetaobcc20172.com.projetopetemfoco.config.ConfiguracoesBuscaServico;
//import projetaobcc20172.com.projetopetemfoco.model.Usuario;
//import projetaobcc20172.com.projetopetemfoco.utils.Localizacao;
//import projetaobcc20172.com.projetopetemfocofornecedor.activity.ServicosActivity;

/*
* Essa classe implementa o navigator Drawer existente na tela de busca*/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , Serializable{

    private ImageView mFoto;
    private TextView mNome, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ConfiguracoesBuscaServico.inicializar();
        //Localizacao.getCurrentLocation(this);
        setContentView(R.layout.activity_main);//Activity em que se encontra o navigator
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//toolbar do navigator
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  (NavigationView)findViewById(R.id.nav_busca);

        displaySelectedScreen(R.id.nav_perfil);//Determina qual tela será aberta primeiro ao entrar

        View header = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        //mNome = header.findViewById(R.id.tvNomeProfile);
        //mFoto = header.findViewById(R.id.imageViewProfile);
        //mEmail = header.findViewById(R.id.tvEmailProfile);

        //Recuperar id do usuário logado
        String mIdUsuarioLogado;
        mIdUsuarioLogado = getPreferences("id", getApplication());

//       DatabaseReference mReferenciaFirebase;
//        mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
//        mReferenciaFirebase.child("usuarios").child(mIdUsuarioLogado).addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.getValue() != null){
//                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
//                    //Exibe a foto de perfil do usuário através do Glide
//                    Glide.with(getApplicationContext()).load(usuario.getmFoto()).asBitmap().into(new BitmapImageViewTarget(mFoto){
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
//                    mNome.setText(usuario.getNome());
//                    mEmail.setText(usuario.getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //vazio
//            }
//        });
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
                //fragment = new ServicosActivity();

            case R.id.nav_sair:
                //this.deslogarUsuario();
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

    //Método para deslogar usuário da aplicação e retornar a tela de Login
//    private void deslogarUsuario(){
//        FirebaseAuth.getInstance().signOut();
//        LoginManager.getInstance().logOut();
//        Intent intent = new Intent(getApplication(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    //Método que recupera o id do usuário logado, para salvar o pet no nó do usuário que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}

