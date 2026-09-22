package com.zg.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    String estado
    String cidade
    List<String>  competencias = []
    Empresa empresa
}
