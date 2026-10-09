package com.zg.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    Endereco endereco
    List<Competencia> competencias = []
    Empresa empresa
}
