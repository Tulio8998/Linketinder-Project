package com.zg.linketinder.vo

class CpfVO {
    String valor

    CpfVO(String valor) {
        if (valor == null || !valor.matches(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/)) {
            throw new IllegalArgumentException("Erro: formato de CPF invalido")
        }
        this.valor = valor
    }
}
