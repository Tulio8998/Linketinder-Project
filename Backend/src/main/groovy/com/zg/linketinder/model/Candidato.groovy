package com.zg.linketinder.model

import java.time.LocalDate

class Candidato extends Pessoa{
    String cpf
    int idade
    LocalDate dataNascimento
    List<Competencia> competencias = []
}
