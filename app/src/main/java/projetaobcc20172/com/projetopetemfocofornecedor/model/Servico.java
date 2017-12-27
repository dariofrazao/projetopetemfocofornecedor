package projetaobcc20172.com.projetopetemfocofornecedor.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;

/**
 * Created by Felipe Oliveira on 05/12/17.
 *
 */

public class Servico {
    private String mId;
    private String mNome;
    private String mDescricao;
    private String mValor;

    public Servico() {}

    public Servico(String nome, String valor){
        this.mNome = nome;
        this.mValor = valor;
    }

    @Exclude
    public String getId() {
        return mId;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public String getDescricao() {
        return mDescricao;
    }

    public void setDescricao(String descricao) {
        this.mDescricao = descricao;
    }

    public String getValor() {
        return mValor;
    }

    public void setValor(String Valor) {
        this.mValor = Valor;
    }

    public void salvar(String idEstabelecimento){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("fornecedor").child( idEstabelecimento ).setValue(this);
    }
}
