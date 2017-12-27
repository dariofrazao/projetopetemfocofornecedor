package projetaobcc20172.com.projetopetemfocofornecedor.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase; //Variável para acesso ao banco de dados do Firebase
    private static FirebaseAuth autenticacao; //Variável para acesso a autenticação de usuário no Firebase

    public static DatabaseReference getFirebase(){ //Método para obter uma instância do banco de dados do Firebase

        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){ //Método para obter uma instância da Autenticação do Firebase
        if( autenticacao == null ){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
