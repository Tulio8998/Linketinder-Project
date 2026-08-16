package com.zg.linketinder.cli

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Curtida
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CurtidaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import com.zg.linketinder.util.Util

class MenuTerminal {
    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()
    VagaService vagaService = new VagaService(empresaService)
    CurtidaService curtidaService = new CurtidaService()

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
                    println "9. Sair"
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
                            print "Saindo"
                            return
                        default:
                            println "Opcao incorreta! Digite novamante\n"
                    }
                }
            }
    }

    def listarEmpresas() {
        List<Empresa> empresas = empresaService.listarTodos()
        empresas.forEach {
            println(
                    "Cnpj: ${it.cnpj}\n" +
                    "Pais: ${it.pais}\n" +
                    "Nome: ${it.nome}\n" +
                    "Email: ${it.email}\n" +
                    "Estado: ${it.estado}\n" +
                    "Cep: ${it.cep}\n" +
                    "Descrição: ${it.descricao}\n" +
                    "Competencias:"
            )
            it.competencias.forEach {
                print("$it, ")
            }
            println "\n"
        }
    }

    def listarCandidatos() {
        List<Candidato> candidatos = candidatoService.listarTodos()
        candidatos.forEach {
            println(
                    "Cpf: ${it.cpf}\n" +
                    "Idade: ${it.idade}\n" +
                    "Nome: ${it.nome}\n" +
                    "Email: ${it.email}\n" +
                    "Estado: ${it.estado}\n" +
                    "Cep: ${it.cep}\n" +
                    "Descrição: ${it.descricao}\n" +
                    "Competencias:"
            )
            it.competencias.forEach {
                print("$it, ")
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
        empresaService.adicionarEmpresa(empresa)
        println "Dados de empresa salvo!\n"
    }

    def cadastrarCandidato(reader) {
        print "Cpf: "
        String cpf = Util.leStringConsole(reader)
        print "Idade: "
        int idade = Util.leInteiroConsole(reader)
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
        Candidato candidato = new Candidato(cpf: cpf, idade: idade, nome: nome, email: email, estado: estado, cep: cep, descricao: descricao)
        candidatoService.adicionarCandidato(candidato)
        println "Dados de candidato salvo!\n"
    }

    def candidatoCurtirVaga(reader) {
        List<Candidato> candidatos = candidatoService.listarTodos()
        List<Vaga> vagas = vagaService.listarTodos()

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
        curtidaService.curtirVaga(candidato, vaga)
        println "\n${candidato.nome} curtiu a vaga ${vaga.nome}\n"
    }

    def empresaCurtirCandidato(reader) {
        List<Empresa> empresas = empresaService.listarTodos()
        List<Candidato> candidatos = candidatoService.listarTodos()

        println "\nEmpredas"
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
        curtidaService.curtirCandidato(empresa, candidato)
        println "\n${empresa.nome} curtiu ${candidato.nome}!\n"
    }

    def listarMatches() {
        List<Curtida> matches = curtidaService.listarCurtidasDeVolta()

        if (matches.isEmpty()) {
            println "\nNenhum match encontrado\n"
            return
        }

        println "\nMATCHES"
        matches.each {
            println "Candidato: ${it.candidato.nome}"
            println"Vaga: ${it.vaga.nome}"
            println"Empresa: ${it.vaga.empresa.nome}\n"
        }
    }


    def listarVagas() {
        String nome
        String descricao
        String estado
        String cidade
        def competencias = []
        Empresa empresa
        List<Vaga> vagas = vagaService.listarTodos()
        vagas.forEach {
            println(
                    "Nome: ${it.nome}\n" +
                    "Descricao: ${it.descricao}\n" +
                    "Estado: ${it.estado}\n" +
                    "Cidade: ${it.cidade}\n" +
                    "Empresa: ${it.empresa.nome}\n" +
                    "Competencias:"
            )
            it.competencias.forEach {
                print("$it, ")
            }
            println "\n"
        }
    }

}