package com.zg.linketinder.dto

import com.zg.linketinder.model.Endereco

import java.time.LocalDate

class CandidatoPerfilDTO {
    Integer id
    String nome
    String email
    String cpf
    LocalDate dataNascimento
    String descricao
    Endereco endereco
}
