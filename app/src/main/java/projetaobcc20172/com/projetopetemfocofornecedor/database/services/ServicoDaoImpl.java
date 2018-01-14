package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Preferencias;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by Felipe Oliveira on 07/12/17.
 * <flpdias14@gmail.com>
 */

public class ServicoDaoImpl implements ServicoDao{


    private DatabaseReference mReferenciaFirebase;
    private final Context mContexto;
    private final Preferencias mPreferencias;

    public ServicoDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
        this.mPreferencias =  new Preferencias(this.mContexto);
    }

    @Override
    public void inserir(final Servico servico, final String idFornecedor) {
        servico.setIdFornecedor(idFornecedor);
        DatabaseReference ref = mReferenciaFirebase.child("servicos").push();
        final String id = ref.getKey();
        ref.setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                if(task.isComplete()){
                    //cria um "fornecedor_servico" para todos os novos servicos add
                    servico.setmId(id);
                    salvarFornecedorServico(servico,servico.getId(),servico.getIdFornecedor());
                }

            }
        });

    }

    @Override
    public void remover(final Servico servico, String idFornecedor) {
        mReferenciaFirebase.child(String.format("%s/%s", "servicos", servico.getId())).setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            removerServicoFornecedor(servico.getId()+"");
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
    public void atualizar(final Servico servico, final String idFornecedor) {
        servico.setIdFornecedor(idFornecedor);
        mReferenciaFirebase.child(String.format("%s/%s", "servicos", servico.getId()))
                .setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    atualizarFornecedorServico(servico);
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

    /*
    */
    //Cria um nó chamadao "servico_fornecedor" que relaciona o servico ao fornecedor
    //Permitindo a busca de fornecedores pelo servico
    private void salvarFornecedorServico(Servico servico,String idServico,String idFornecedor){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(idServico);
        ref.push();
        Map<String, String> mapaNomes = new HashMap<>();
        String nomeServTipoPet = servico.getNome()+"_"+ servico.getTipoPet();
        mapaNomes.put("nome_tipoPet",nomeServTipoPet);
        mapaNomes.put("idFornecedor",idFornecedor);
        mapaNomes.put("valor",servico.getValor());
        mapaNomes.put("nomeFornecedor",mPreferencias.getNome());
        mapaNomes.put("servico",servico.getNome());
        mapaNomes.put("pet",servico.getTipoPet());
        ref.setValue(mapaNomes);
    }

    private void atualizarFornecedorServico(final Servico servico){
        ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(servico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salvarFornecedorServico(servico,servico.getId(),servico.getIdFornecedor());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void removerServicoFornecedor(String idServico){
        ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(idServico).setValue(null);

    }
    private Context getContexto(){
        return this.mContexto;
    }

}
