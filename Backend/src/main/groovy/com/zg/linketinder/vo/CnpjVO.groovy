package com.zg.linketinder.vo

class CnpjVO {
    String valor

    CnpjVO(String valor) {
        if (valor == null || !valor.matches(/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/)) {
            throw new IllegalArgumentException("Erro: formato de CNPJ invalido")
        }
        this.valor = valor
    }
}