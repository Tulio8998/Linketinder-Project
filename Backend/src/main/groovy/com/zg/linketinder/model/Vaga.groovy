package com.zg.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    String estado
    String pais
    String cidade
    List<Competencia> competencias = []
    Empresa empresa
}
