package com.zg.linketinder.model

import com.zg.linketinder.vo.CpfVO

import java.time.LocalDate

class Candidato extends Pessoa{
    CpfVO cpf
    int idade
    LocalDate dataNascimento
    List<Competencia> competencias = []
}
