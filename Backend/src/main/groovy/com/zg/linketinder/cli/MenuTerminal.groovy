package com.zg.linketinder.cli

import com.zg.linketinder.util.Util

class MenuTerminal {
    MenuEmpresa menuEmpresa = new MenuEmpresa()
    MenuCandidato menuCandidato = new MenuCandidato()

    def executaTerminal() {
        System.in.withReader {
            while (true) {
                println "1. Empresa"
                println "2. Candidato"
                println "3. Sair"
                print "Escolha: "
                int opcao = Util.leInteiroConsole(it)
                switch (opcao) {
                    case 1:
                        menuEmpresa.executaEmpresa()
                        break
                    case 2:
                        menuCandidato.executaCandidato()
                        break
                    case 3:
                        print "Saindo"
                        return
                    default:
                        println "Opcao incorreta! Digite novamante\n"
                }
            }
        }
    }
}