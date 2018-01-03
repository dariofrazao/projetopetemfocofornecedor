package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import java.util.List;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by dario on 11/12/2017.
 */

public class FornecedorDaoImpl implements FornecedorDao{

    private DatabaseReference mReferenciaFirebase;
    private  final Context mContexto;

    public FornecedorDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
    }

    @Override
    public void inserir(Fornecedor fornecedor, String idFornecedor) {

        //Método para salvar usuário no banco de dados do Firebase


        //Chama a referência do Firebase
        //Cria os nós dos fornecedores no banco de dados
        mReferenciaFirebase.child("fornecedor").child(fornecedor.getId()).setValue(fornecedor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_cadastro_Fornecedor));
                }
                else{
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_cadastrar));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /*
    @Override
    public void remover(Usuario usuario, String idFornecedor) {
        referenciaFornecedor = referenciaFirebase.child("usuarios");
        referenciaFornecedor.child(String.format("%s/%s", "servicos", usuario.getId()))
                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_remocao));
                }
                else{
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.falha_remocao));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void atualizar(Usuario usuario, String idFornecedor) {
        referenciaFornecedor = referenciaFirebase.child("usuarios");
        referenciaFornecedor.child(String.format("%s/%s", "servicos", usuario.getId()))
                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_atualizacao));
                }
                else{
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.falha_atualizacao));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    */
/*
    @Override
    public List<Fornecedor> buscarPorNome(String nome) {
        return null;
    }

    @Override
    public List<Fornecedor> buscarTodosFornecedor(String idFornecedor) {
        return null;
    }
*/

    private Context getContexto(){
        return this.mContexto;
    }
}
