package com.zg.linketinder.model

import java.time.LocalDate

class Candidato extends Pessoa{
    String cpf
    int idade
    LocalDate data_nascimento
    List<Competencia> competencias = []
}
