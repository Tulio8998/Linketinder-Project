package com.zg.linketinder.util

class Util {
    def static leInteiroConsole(reader) {
        while (true) {
            try {
                return reader.readLine().toInteger()
            } catch (NumberFormatException e) {
                print "Erro ao ler número!\nDigite novamente: "
            }
        }
    }

    def static leStringConsole(reader) {
        while (true) {
            try {
                return reader.readLine().trim()
            } catch (InputMismatchException e) {
                print "Erro ao ler!\nDigite novamente: "
            }
        }
    }
}
