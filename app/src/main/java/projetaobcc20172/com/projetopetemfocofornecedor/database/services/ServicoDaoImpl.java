package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDao;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by Felipe Oliveira on 07/12/17.
 * <flpdias14@gmail.com>
 */

public class ServicoDaoImpl implements ServicoDao{


    private DatabaseReference mReferenciaFirebase;
    private final Context mContexto;

    public ServicoDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
    }

    @Override
    public void inserir(Servico servico, String idFornecedor) {
        mReferenciaFirebase = mReferenciaFirebase.child("fornecedor").child(idFornecedor);
        mReferenciaFirebase.child("servicos").push().setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_cadastro_servico));
                }
                else{
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.erro_cadastro_servico));
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
    public void remover(Servico servico, String idFornecedor) {
        mReferenciaFirebase = mReferenciaFirebase.child("fornecedor").child(idFornecedor);
        mReferenciaFirebase.child(String.format("%s/%s", "servicos", servico.getId()))
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
    public void atualizar(Servico servico, String idFornecedor) {
        mReferenciaFirebase = mReferenciaFirebase.child("fornecedor").child(idFornecedor);
        mReferenciaFirebase.child(String.format("%s/%s", "servicos", servico.getId()))
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

    @Override
    public List<Servico> buscarPorNome(String nome) {
        return null;
    }

    @Override
    public List<Servico> buscarTodosFornecedor(String idFornecedor) {
        return null;
    }


    private Context getContexto(){
        return this.mContexto;
    }

}
