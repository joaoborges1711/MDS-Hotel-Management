package  MDS;

import java.time.LocalDate;


abstract class Person {
    protected int idPessoa;
    protected String nome;
    protected String BI;
    protected LocalDate dataNascimento;

    public Person(int idPessoa, String nome, String BI, LocalDate dataNascimento) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.BI = BI;
        this.dataNascimento = dataNascimento;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBI() {
        return BI;
    }

    public void setBI(String BI) {
        this.BI = BI;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}