package com.zg.linketinder.cli

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CompetenciaService
import com.zg.linketinder.service.CurtidaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import com.zg.linketinder.util.Util
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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