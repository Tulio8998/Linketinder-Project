package com.zg.linketinder.cli

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.util.Util

class MenuTerminal {
    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()

    def executaTerminal() {
            System.in.withReader {
                while (true) {
                    println "1. Listar Empresas"
                    println "2. Listar Candidatos"
                    println "3. Cadastrar Empresa"
                    println "4. Cadastrar Candidato"
                    println "5. Sair"
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

}