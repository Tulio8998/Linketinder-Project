package com.zg.linketinder.model

import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO

import java.time.LocalDate

class Recrutador {
    Integer id
    Empresa empresa
    String nome
    String area
    String descricao
    EmailVO email
    CpfVO cpf
    LocalDate dataNascimento
    String senha
    Boolean ativo = true
}
