package com.zg.linketinder.util

import java.util.Locale

class AreaAtuacao {
    static String normalizar(String area) {
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException('A area de atuacao e obrigatoria')
        }
        return area.trim().toUpperCase(Locale.ROOT)
    }
}
