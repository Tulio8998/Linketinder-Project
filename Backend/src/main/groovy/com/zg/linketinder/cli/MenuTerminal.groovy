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
    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()
    VagaService vagaService = new VagaService()
    CurtidaService curtidaService = new CurtidaService()
    CompetenciaService competenciaService = new CompetenciaService()

    def executaTerminal() {
        System.in.withReader {
            while (true) {
                println "1. Listar Empresas"
                println "2. Listar Candidatos"
                println "3. Cadastrar Empresa"
                println "4. Cadastrar Candidato"
                println "5. Candidato curtir Vaga"
                println "6. Empresa curtir Candidato"
                println "7. Ver Matches"
                println "8. Ver Vagas"
                println "9. Listar Competencias"
                println "10. Sair"
                print "Escolha: "
                int opcao = Util.leInteiroConsole(it)
                switch (opcao) {
                    case 1:
                        listarEmpresas()
                        break
                    case 2:
                        listarCandidatos()
                        break
                    case 3:
                        cadastrarEmpresa(it)
                        break
                    case 4:
                        cadastrarCandidato(it)
                        break
                    case 5:
                        candidatoCurtirVaga(it)
                        break
                    case 6:
                        empresaCurtirCandidato(it)
                        break
                    case 7:
                        listarMatches()
                        break
                    case 8:
                        listarVagas()
                        break
                    case 9:
                        listarCompetencias()
                        return
                    case 10:
                        print "Saindo"
                        return
                    default:
                        println "Opcao incorreta! Digite novamante\n"
                }
            }
        }
    }

    def listarEmpresas() {
        List<Empresa> empresas = empresaService.listarEmpresas()
        empresas.forEach {
            println(
                    "Cnpj: ${it.cnpj}\n" +
                            "Pais: ${it.pais}\n" +
                            "Nome: ${it.nome}\n" +
                            "Email: ${it.email}\n" +
                            "Estado: ${it.estado}\n" +
                            "Cep: ${it.cep}\n" +
                            "Descrição: ${it.descricao}\n"
            )
            println "\n"
        }
    }

    def listarCompetencias() {
        List<Competencia> competencias = competenciaService.listarCompetencia()
        competencias.forEach {
            println(
                    "Nome: ${it.nome}\n"
            )
        }
    }

    def listarCandidatos() {
        List<Candidato> candidatos = candidatoService.listarCandidatos()
        candidatos.forEach {
            println(
                    "Cpf: ${it.cpf}\n" +
                            "Nome: ${it.nome}\n" +
                            "Email: ${it.email}\n" +
                            "Estado: ${it.estado}\n" +
                            "Cep: ${it.cep}\n" +
                            "Descrição: ${it.descricao}\n" +
                            "Competencias:"

            )
            it.competencias?.forEach { comp ->
                print("${comp.nome}, ")
            }
            println "\n"
        }
    }

    def cadastrarEmpresa(reader) {
        println "Cadastro de empresa"
        print "Cnpj: "
        String cnpj = Util.leStringConsole(reader)
        print "Pais: "
        String pais = Util.leStringConsole(reader)
        print "Nome: "
        String nome = Util.leStringConsole(reader)
        print "Email: "
        String email = Util.leStringConsole(reader)
        print "Estado: "
        String estado = Util.leStringConsole(reader)
        print "Cep: "
        String cep = Util.leStringConsole(reader)
        print "Descricao: "
        String descricao = Util.leStringConsole(reader)

        Empresa empresa = new Empresa(cnpj: cnpj, pais: pais, nome: nome, email: email, estado: estado, cep: cep, descricao: descricao)
        empresaService.salvarEmpresa(empresa)
        println "Dados de empresa salvo!\n"
    }

    def cadastrarCandidato(reader) {
        println "Cadastro de candidato"
        print "Cpf: "
        String cpf = Util.leStringConsole(reader)
        print "Idade: "
        int idade = Util.leInteiroConsole(reader)
        print "Data de Nascimento (YYYY-MM-DD): "
        String dataNascimentoStr = Util.leStringConsole(reader)
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        print "Nome: "
        String nome = Util.leStringConsole(reader)
        print "Email: "
        String email = Util.leStringConsole(reader)
        print "Senha: "
        String senha = Util.leStringConsole(reader)
        print "Pais: "
        String pais = Util.leStringConsole(reader)
        print "Estado: "
        String estado = Util.leStringConsole(reader)
        print "Cidade: "
        String cidade = Util.leStringConsole(reader)
        print "Cep: "
        String cep = Util.leStringConsole(reader)
        print "Descricao: "
        String descricao = Util.leStringConsole(reader)

        Candidato candidato = new Candidato(cpf: cpf, idade: idade, data_nascimento: dataNascimento, nome: nome, email: email, senha: senha, pais: pais, cidade: cidade, estado: estado, cep: cep, descricao: descricao)
        candidatoService.salvarCandidato(candidato)
        println "Dados de candidato salvo!\n"
    }

    def candidatoCurtirVaga(reader) {
        List<Candidato> candidatos = candidatoService.listarCandidatos()
        List<Vaga> vagas = vagaService.listarTodasVagas()

        println "\nCandidatos"
        candidatos.eachWithIndex { candidato, index ->
            println "${index + 1}. ${candidato.nome}"
        }

        print "Escolha o cansidato: "
        int opcaoCandidato = Util.leInteiroConsole(reader)
        if (opcaoCandidato < 1 || opcaoCandidato > candidatos.size()) {
            println "Candidato invalido!\n"
            return
        }

        Candidato candidato = candidatos[opcaoCandidato - 1]
        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome} - ${vaga.empresa.nome}"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalida!\n"
            return
        }

        Vaga vaga = vagas[opcaoVaga - 1]
        curtidaService.curtirVagaComoCandidato(candidato, vaga, true)
        println "\n${candidato.nome} curtiu a vaga ${vaga.nome}\n"
    }

    def empresaCurtirCandidato(reader) {
        List<Empresa> empresas = empresaService.listarEmpresas()
        List<Candidato> candidatos = candidatoService.listarCandidatos()

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome} - ${empresa.cnpj}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }
        Empresa empresa = empresas[opcaoEmpresa - 1]

        List<Vaga> vagasDaEmpresa = vagaService.listarVaga(empresa.id)
        if (vagasDaEmpresa.isEmpty()) {
            println "Esta empresa não possui vagas cadastradas!\n"
            return
        }
        println "\nSuas Vagas"
        vagasDaEmpresa.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Para qual vaga deseja curtir um candidato? "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagasDaEmpresa.size()) {
            println "Vaga invalida!\n"
            return
        }
        Vaga vagaSelecionada = vagasDaEmpresa[opcaoVaga - 1]

        println "\nCandidatos"
        candidatos.eachWithIndex { candidato, index ->
            println "${index + 1}. ${candidato.nome}"
        }

        print "Escolha o candidato: "
        int opcaoCandidato = Util.leInteiroConsole(reader)
        if (opcaoCandidato < 1 || opcaoCandidato > candidatos.size()) {
            println "Candidato invalido!\n"
            return
        }
        Candidato candidato = candidatos[opcaoCandidato - 1]

        curtidaService.curtirVagaComoEmpresa(empresa, candidato, vagaSelecionada, true)
        println "\n${empresa.nome} curtiu ${candidato.nome}!\n"
    }

    def listarMatches() {
        List<Match> matches = curtidaService.listarMatch()

        if (matches.isEmpty()) {
            println "\nNenhum match encontrado\n"
            return
        }

        println "\nMATCHES"
        matches.each {
            println "Candidato: ${it.candidato.nome}"
            println"Vaga: ${it.vaga.nome}"
            println"Empresa: ${it.empresa.nome}\n"
        }
    }


    def listarVagas() {
        List<Vaga> vagas = vagaService.listarTodasVagas()
        vagas.forEach {
            println(
                    "Nome: ${it.nome}\n" +
                            "Descricao: ${it.descricao}\n" +
                            "Estado: ${it.estado}\n" +
                            "Cidade: ${it.cidade}\n" +
                            "Empresa: ${it.empresa.nome}\n" +
                            "Competencias:"
            )
            it.competencias?.forEach {
                print("$it.nome, ")
            }
            println "\n"
        }
    }

}