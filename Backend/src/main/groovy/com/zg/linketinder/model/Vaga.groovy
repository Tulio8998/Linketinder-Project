package com.zg.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    String area
    Endereco endereco
    List<Competencia> competencias = []
    Empresa empresa
}
