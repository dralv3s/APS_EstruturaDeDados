package aps.estrutura.dados.Model;

import java.time.LocalDate;
import java.util.Date;

public class Tarefa {

    public Integer Index;
    public LocalDate DataEntrega;

    public String NomeTarefa;

    /* 0 = Pendente, 1 = Concluido, 2 = Atrasado*/
    public Integer Status;

    public Tarefa() {
    }

    public LocalDate getDataEntrega() {
        return DataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        DataEntrega = dataEntrega;
    }

    public String getNomeTarefa() {
        return NomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        NomeTarefa = nomeTarefa;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Integer getIndex() {
        return Index;
    }

    public void setIndex(Integer index) {
        Index = index;
    }
}