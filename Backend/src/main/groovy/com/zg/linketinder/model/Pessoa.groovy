package com.zg.linketinder.model

import javax.xml.crypto.Data

class Pessoa {
    Integer id
    String nome
    String email
    String senha
    String pais
    String cidade
    String estado
    String cep
    String descricao
    List<String> competencias = ["Python", "Java", "Spring Framework", "Angular", "Groovy", "JavaScript", "TypeScript"]
}
