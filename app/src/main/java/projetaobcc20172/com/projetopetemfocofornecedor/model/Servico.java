package projetaobcc20172.com.projetopetemfocofornecedor.model;

import java.io.Serializable;

/**
 * Created by Felipe Oliveira on 05/12/17.
 *
 */

public class Servico implements Serializable{
    private String mId;
    private String mNome;
    private String mDescricao;
    private String mValor;

    public Servico() {}

    public Servico(String nome, String valor){
        this.mNome = nome;
        this.mValor = valor;
    }


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

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Servico){
            Servico servico = (Servico) o;
            if( servico.getDescricao().equalsIgnoreCase(this.getDescricao())
                    && servico.getNome().equalsIgnoreCase(this.getNome())
                    && servico.getValor().equalsIgnoreCase(this.getValor())){
                return true;
            }
        }
        return false;
    }


}
