package projetaobcc20172.com.projetopetemfocofornecedor.model;

import java.io.Serializable;

/**
 * Created by LuizAlberes on 15/02/2018.
 */

public class Cupom implements Serializable{
    private String mId;
    private String mNome;
    private String mValor;
    private String mIdFornecedor;
    private String mIdServico;
    private String mNomeServico;
    private String mDataInicio;
    private String mDataVencimento;
    private String mJuncao;

    public Cupom(){

    }

    public Cupom(String dataInicio, String dataVencimento, String idFornecedor, String idServico, String juncao, String nome, String valor) {
        this.mDataInicio = dataInicio;
        this.mDataVencimento = dataVencimento;
        this.mIdFornecedor = idFornecedor;
        this.mIdServico = idServico;
        this.mJuncao = juncao;
        this.mNome = nome;
        this.mValor = valor;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public String getIdFornecedor() {
        return mIdFornecedor;
    }

    public void setIdFornecedor(String idFornecedor) {
        this.mIdFornecedor = idFornecedor;
    }

    public String getValor() {
        return mValor;
    }

    public void setValor(String valor) {
        this.mValor = valor;
    }

    public String getDataInicio() {
        return mDataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.mDataInicio = dataInicio;
    }

    public String getDataVencimento() {
        return mDataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.mDataVencimento = dataVencimento;
    }

    public String getIdServico() {
    return mIdServico;
}

    public void setIdServico(String idServico) {
        this.mIdServico = idServico;
    }

    public String getNomeServico() {
        return mNomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.mNomeServico = nomeServico;
    }

    public String getJuncao() {
        return mJuncao;
    }

    public void setJuncao(String juncao) {
        this.mJuncao = juncao;
    }

}
