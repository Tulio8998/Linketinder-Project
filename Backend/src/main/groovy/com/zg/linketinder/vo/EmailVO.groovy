package com.zg.linketinder.vo

class EmailVO {
    String valor

    EmailVO(String valor) {
        if (valor == null || !valor.matches(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/)) {
            throw new IllegalArgumentException("Erro: formato de email invalido")
        }
        this.valor = valor
    }
}
