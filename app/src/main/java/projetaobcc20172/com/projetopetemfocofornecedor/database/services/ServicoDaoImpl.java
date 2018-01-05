package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import java.util.HashMap;
import java.util.Map;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
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
    public void inserir(Servico servico, final String idFornecedor) {
        mReferenciaFirebase = mReferenciaFirebase.child("fornecedor").child(idFornecedor);
        mReferenciaFirebase.child("servicos").push().setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_cadastro_servico));
                    mReferenciaFirebase.child("servicos").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            try {
                                Servico servico = dataSnapshot.getValue(Servico.class);
                                salvarFornecedorServico(servico,s,idFornecedor);
                            }catch (Exception e){
                                System.out.println("ERRO");
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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
/*
    @Override
    public List<Servico> buscarPorNome(String nome) {
        return null;
    }

    @Override
    public List<Servico> buscarTodosFornecedor(String idFornecedor) {
        return null;
    }
*/
    //Cria um nó chamadao "servico_fornecedor" que relaciona o servico ao fornecedor
    //Permitindo a busca de fornecedores pelo servico
    private void salvarFornecedorServico(Servico servico,String idServico,String idFornecedor){
        mReferenciaFirebase = ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(idServico);
        mReferenciaFirebase.push();
        Map<String, String> mapaNomes = new HashMap<>();
        String nomeServTipoPet = servico.getNome()+"_"+ servico.getTipoPet();
        mapaNomes.put("nome_tipoPet",nomeServTipoPet);
        mapaNomes.put("idFornecedor",idFornecedor);
        mapaNomes.put("valor",servico.getValor());
        mReferenciaFirebase.setValue(mapaNomes);
    }

    private Context getContexto(){
        return this.mContexto;
    }

}
