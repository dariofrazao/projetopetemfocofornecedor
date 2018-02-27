package projetaobcc20172.com.projetopetemfocofornecedor.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by renat on 06/12/2017.
 */

public class Fornecedor implements Serializable {

    //Atributos do fornecedor
    private String mId;
    private String mNome;
    private String mNomeBusca;
    private String mEmail;
    private String mTelefone;
    private String mCpfCnpj;
    private String mHorarios;
    private String mEnderecoFornecedor = "1";
    private Endereco mEndereco;
    private String mTipo;
    private String mFoto;
    private ArrayList<Servico> mServicos = new ArrayList<>();

    private float mNota = 0;

    public Fornecedor() {
    }

    public Fornecedor(String nome, String nomeBusca, String email, String cpfCnpj, String telefone, String horarios) {
        this.mNome = nome;
        this.mNomeBusca = nomeBusca;
        this.mEmail = email;
        this.mHorarios = horarios;
        this.mCpfCnpj = cpfCnpj;
        this.mTelefone = telefone;
    }

    @Exclude
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

    public String getNomeBusca() {
        return mNomeBusca;
    }

    public void setNomeBusca(String nomeBusca) {
        this.mNomeBusca = nomeBusca;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getTelefone() {
        return mTelefone;
    }

    public void setTelefone(String telefone) {
        this.mTelefone = telefone;
    }

    public String getCpfCnpj() {
        return mCpfCnpj;
    }

    public void setCpfCnpj(String cpf_cnpj) {
        this.mCpfCnpj = cpf_cnpj;
    }

    public String getHorarios() {
        return mHorarios;
    }

    public void setHorarios(String horarios) {
        this.mHorarios = horarios;
    }

    public Endereco getEndereco() {
        return mEndereco;
    }

    public void setEndereco(Endereco endereco) {
        this.mEndereco = endereco;
    }

    public String getEnderecoFornecedor() {
        return mEnderecoFornecedor;
    }

    public void setEnderecoFornecedor(String valor) {
        this.mEnderecoFornecedor = valor;
    }

    public void addServico(Servico serv) {
        this.mServicos.add(serv);
    }

    public ArrayList<Servico> getServicos() {
        return mServicos;
    }

    public void setServicos(ArrayList<Servico> servicos) {
        this.mServicos = servicos;
    }

    public float getNota() {
        return mNota;
    }

    public void setNota(float mNota) {
        this.mNota = mNota;
    }

    public void setTipo(String tipo){
        this.mTipo = tipo;
    }

    public String getTipo(){
        return this.mTipo;
    }

}
