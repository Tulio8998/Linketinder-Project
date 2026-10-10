package com.zg.linketinder.cli

import com.zg.linketinder.controller.CandidatoController
import com.zg.linketinder.controller.CompetenciaController
import com.zg.linketinder.controller.CurtidaController
import com.zg.linketinder.controller.VagaController
import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.util.Util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MenuCandidato {
    CandidatoController candidatoController = new CandidatoController()
    CurtidaController curtidaController = new CurtidaController()
    CompetenciaController competenciaController = new CompetenciaController()
    VagaController vagaController = new VagaController()

    def executaCandidato() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        while (true) {
            println "1. Cadastrar Candidato"
            println "2. Atualizar Candidato"
            println "3. Excluir Candidato"
            println "4. Listar Candidatos"
            println "5. Buscar id Candidato"
            println "6. Buscar email Candidato"
            println "7. Buscar CPF Candidato"
            println "8. Cadastrar Competencia Candidato"
            println "9. Excluir Competencia Candidato"
            println "10. Listar Competencias Candidato"
            println "11. Buscar id Competencias Candidato "
            println "12. Buscar nome Competencias Candidato"
            println "13. Salvar Curtida Candidato"
            println "14. Listar Curtidas Candidato"
            println "15. Listar Matches Candidato"
            println "16. Listar Vagas"
            println "17. Buscar id Vagas "
            println "18. Buscar nome Vagas"
            println "19. Calcular Afinidade"
            println "20. Sair"
            print "Escolha: "
            int opcao = Util.leInteiroConsole(reader)
            switch (opcao) {
                case 1:
                    cadastrarCandidato(reader)
                    break
                case 2:
                    atualizarCandidato(reader)
                    break
                case 3:
                    excluirCandidato(reader)
                    break
                case 4:
                    listarCandidato()
                    break
                case 5:
                    buscarIdCandidato(reader)
                    break
                case 6:
                    buscarEmailCandidato(reader)
                    break
                case 7:
                    buscarCpfCandidato(reader)
                    break
                case 8:
                    cadastrarCompetenciaCandidato(reader)
                    break
                case 9:
                    excluirCompetenciaCandidato(reader)
                    break
                case 10:
                    listarCompetenciasCandidato(reader)
                    break
                case 11:
                    buscarIdCompetenciaCandidato(reader)
                    break
                case 12:
                    buscarNomeCompetenciaCandidato(reader)
                    break
                case 13:
                    salvarCurtidaCandidato(reader)
                    break
                case 14:
                    listarCurtidaCandidato()
                    break
                case 15:
                    listarMatchesCandidato(reader)
                    break
                case 16:
                    listarVagas(reader)
                    break
                case 17:
                    buscarIdVaga(reader)
                    break
                case 18:
                    buscarNomeVaga(reader)
                    break
                case 19:
                    calcularAfinidade(reader)
                    break
                case 20:
                    print "Saindo\n"
                    return
                default:
                    println "Opcao incorreta! Digite novamante\n"
            }
        }
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

        CandidatoDTO dto = new CandidatoDTO(
                cpf: cpf, idade: idade, dataNascimento: dataNascimento,
                nome: nome, email: email, senha: senha, pais: pais,
                estado: estado, cidade: cidade, cep: cep, descricao: descricao
        )

        String resposta = candidatoController.cadastrarCandidato(dto)
        println "\n${resposta}\n"
    }

    def atualizarCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        println "Atualizar cadastro de candidato"
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

        CandidatoDTO dto = new CandidatoDTO(
                cpf: cpf, idade: idade, dataNascimento: dataNascimento,
                nome: nome, email: email, senha: senha, pais: pais,
                estado: estado, cidade: cidade, cep: cep, descricao: descricao
        )

        String resposta = candidatoController.atualizarCandidato(candidato.id, dto)
        println "\n${resposta}\n"
    }

    def excluirCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        String resposta = candidatoController.excluirCandidato(candidatos[opcaoCandidato - 1].id)
        println "\n${resposta}\n"
    }

    def listarCandidato() {
        List<Candidato> candidatos = candidatoController.listarCandidatos()
        candidatos.forEach {
            println(
                    "Cpf: ${it.cpf.valor}\n" +
                            "Nome: ${it.nome}\n" +
                            "Email: ${it.email.valor}\n" +
                            "Pais: ${it.endereco.pais}\n" +
                            "Estado: ${it.endereco.estado}\n" +
                            "Cidade: ${it.endereco.cidade}\n" +
                            "Cep: ${it.endereco.cep}\n" +
                            "Descricao: ${it.descricao}\n" +
                            "Competencias:"

            )
            it.competencias?.forEach { comp ->
                print("${comp.nome}, ")
            }
            println "\n"
        }
    }

    def buscarIdCandidato(reader) {
        println "Busca id de candidato"
        print "Digite o id do candidato: "
        int id = Util.leInteiroConsole(reader)

        Candidato candidato = candidatoController.buscarPorId(id)

        if (candidato == null) {
            println "\nCandidato nao encontrado!\n"
            return
        }

        println(
                "Cpf: ${candidato.cpf.valor}\n" +
                        "Nome: ${candidato.nome}\n" +
                        "Email: ${candidato.email.valor}\n" +
                        "Pais: ${candidato.endereco.pais}\n" +
                        "Estado: ${candidato.endereco.estado}\n" +
                        "Cidade: ${candidato.endereco.cidade}\n" +
                        "Cep: ${candidato.endereco.cep}\n" +
                        "Descricao: ${candidato.descricao}\n" +
                        "Competencias:"

        )
        candidato.competencias?.forEach { comp ->
            print("${comp.nome}, ")
        }
        println "\n"
    }

    def buscarEmailCandidato(reader) {
        println "Busca email de candidato"
        print "Digite o email do candidato: "
        String email = Util.leStringConsole(reader)

        Candidato candidato = candidatoController.buscarPorEmail(email)

        if (candidato == null) {
            println "\nCandidato nao encontrado!\n"
            return
        }

        println(
                "Cpf: ${candidato.cpf.valor}\n" +
                        "Nome: ${candidato.nome}\n" +
                        "Email: ${candidato.email.valor}\n" +
                        "Pais: ${candidato.endereco.pais}\n" +
                        "Estado: ${candidato.endereco.estado}\n" +
                        "Cidade: ${candidato.endereco.cidade}\n" +
                        "Cep: ${candidato.endereco.cep}\n" +
                        "Descricao: ${candidato.descricao}\n" +
                        "Competencias:"

        )
        candidato.competencias?.forEach { comp ->
            print("${comp.nome}, ")
        }
        println "\n"
    }

    def buscarCpfCandidato(reader) {
        println "Busca cpf de candidato"
        print "Digite o cpf do candidato: "
        String cpf = Util.leStringConsole(reader)

        Candidato candidato = candidatoController.buscarPorCpf(cpf)

        if (candidato == null) {
            println "\nCandidato nao encontrado!\n"
            return
        }

        println(
                "Cpf: ${candidato.cpf.valor}\n" +
                        "Nome: ${candidato.nome}\n" +
                        "Email: ${candidato.email.valor}\n" +
                        "Pais: ${candidato.endereco.pais}\n" +
                        "Estado: ${candidato.endereco.estado}\n" +
                        "Cidade: ${candidato.endereco.cidade}\n" +
                        "Cep: ${candidato.endereco.cep}\n" +
                        "Descricao: ${candidato.descricao}\n" +
                        "Competencias:"

        )
        candidato.competencias?.forEach { comp ->
            print("${comp.nome}, ")
        }
        println "\n"
    }

    def cadastrarCompetenciaCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        List<Competencia> competencias = competenciaController.listarTodas()

        print "Competencias: "
        competencias.eachWithIndex { competencia, index ->
            println "${index + 1}. ${competencia.nome}"
        }

        print "Escolha a competencia: "
        int opcaoCompetencia = Util.leInteiroConsole(reader)
        if (opcaoCompetencia < 1 || opcaoCompetencia > competencias.size()) {
            println "Competencia invalida!\n"
            return
        }

        String resposta = competenciaController.adicionarCompetenciaCandidato(candidatos[opcaoCandidato - 1].id, competencias[opcaoCompetencia - 1].id)
        println "\n${resposta}\n"
    }

    def excluirCompetenciaCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        int idCandidatoReal = candidatos[opcaoCandidato - 1].id

        List<Competencia> competencias = competenciaController.listarCompetenciasCandidato(idCandidatoReal)

        print "Competencias: "
        competencias.eachWithIndex { competencia, index ->
            println "${index + 1}. ${competencia.nome}"
        }

        print "Escolha a competencia: "
        int opcaoCompetencia = Util.leInteiroConsole(reader)
        if (opcaoCompetencia < 1 || opcaoCompetencia > competencias.size()) {
            println "Competencia invalida!\n"
            return
        }
        Competencia comp = competencias[opcaoCompetencia - 1]

        String resposta = competenciaController.excluirCompetenciaCandidato(idCandidatoReal, comp.id)
        println "\n${resposta}\n"
    }

    def listarCompetenciasCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        List<Competencia> competencias = competenciaController.listarCompetenciasCandidato(candidatos[opcaoCandidato - 1].id)
        competencias.forEach {
            println(
                    "Nome: ${it.nome}"
            )
        }
    }

    def buscarIdCompetenciaCandidato(reader) {
        println "Busca id de competencia"
        print "Digite o id da competencia: "
        int id_competencia = Util.leInteiroConsole(reader)

        println "Busca id de candidato"
        print "Digite o id da candidato: "
        int id_candidato = Util.leInteiroConsole(reader)

        Competencia competencia = competenciaController.buscarIdCompetenciaCandidato(id_candidato, id_competencia)
        Candidato candidato = candidatoController.buscarPorId(id_candidato)

        if (candidato == null) {
            println "\nCandidato nao encontrado!\n"
            return
        }

        if (competencia == null) {
            println "\nCompetencia nao encontrada para este candidato!\n"
            return
        }

        println "Nome: ${candidato.nome}"
        println("Competencia: ${competencia.nome}\n")
    }

    def buscarNomeCompetenciaCandidato(reader) {
        println "Busca nome de competencia"
        print "Digite o nome da competencia: "
        String nome_competencia = Util.leStringConsole(reader)

        println "Busca id de candidato"
        print "Digite o id da candidato: "
        int id_candidato = Util.leInteiroConsole(reader)

        Competencia competencia = competenciaController.buscarNomeCompetenciaCandidato(id_candidato, nome_competencia)
        Candidato candidato = candidatoController.buscarPorId(id_candidato)

        if (candidato == null) {
            println "\nCandidato nao encontrado!\n"
            return
        }

        if (competencia == null) {
            println "\nCompetencia nao encontrada para este candidato!\n"
            return
        }

        println "Nome: ${candidato.nome}"
        println("Competencia: ${competencia.nome}")
    }

    def salvarCurtidaCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        List<Vaga> vagas = vagaController.listarTodas()

        if (vagas.isEmpty()) {
            println "\nNenhuma vaga encontrada\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println (
                    "${index + 1}." +
                            "Nome: ${vaga.nome}\n" +
                            "Empresa: ${vaga.empresa.nome}\n" +
                            "Descricao: ${vaga.descricao}\n" +
                            "Competencias: "
            )
            vaga.competencias?.forEach { comp ->
                println("${comp.nome}, ")
            }
            print "\n"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        String resposta = curtidaController.curtirVagaComoCandidato(candidatos[opcaoCandidato - 1].id, vagas[opcaoVaga - 1].id)
        println "\n${resposta}\n"
    }

    def listarCurtidaCandidato(){
        List<CurtidaCandidato> curtidaCandidatos = curtidaController.listarCurtidasCandidato()

        if (curtidaCandidatos.isEmpty()) {
            println "\nNenhuma curtida encontrado\n"
            return
        }

        println "\nCurtidas"
        curtidaCandidatos.each {
            println "Candidato ${it.candidato.nome} curtiu Vaga ${it.vaga.nome}"
        }
        print "\n"
    }

    def listarMatchesCandidato(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

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

        List<Match> matches = curtidaController.listarMatchesCandidato(candidatos[opcaoCandidato - 1].id)

        if (matches.isEmpty()) {
            println "\nNenhum match encontrado\n"
            return
        }

        println "\nMatches"
        matches.each {
            println "Candidato: ${it.candidato.nome}"
            println "Vaga: ${it.vaga.nome}"
            println "Empresa: ${it.empresa.nome}\n"
        }
    }

    def listarVagas(reader) {
        List<Vaga> vagas = vagaController.listarTodas()

        vagas.each {
            println (
                    "Nome: ${it.nome}\n" +
                            "Empresa: ${it.empresa.nome}\n" +
                            "Descricao: ${it.descricao}\n"
            )
        }
    }

    def buscarIdVaga(reader) {
        println "Busca id de vaga"
        print "Digite o id da vaga: "
        int id_vaga = Util.leInteiroConsole(reader)

        Vaga vaga = vagaController.buscarPorId(id_vaga)

        if (vaga == null) {
            println "\nVaga nao encontrada!\n"
            return
        }

        println(
                "Nome: ${vaga.nome}\n" +
                        "Empresa: ${vaga.empresa.nome}\n" +
                        "Descricao: ${vaga.descricao}\n" +
                        "Competencias: "
        )
        vaga.competencias?.each { comp ->
            println "${comp.nome} "
        }
        println "\n"
    }

    def buscarNomeVaga(reader) {
        println "Busca nome de vaga"
        print "Digite o nome da vaga: "
        String nome = Util.leStringConsole(reader)

        Vaga vaga = vagaController.buscarNomeVaga(nome)

        if (vaga == null) {
            println "\nVaga nao encontrada!\n"
            return
        }

        println(
                "Nome: ${vaga.nome}\n" +
                        "Empresa: ${vaga.empresa.nome}\n" +
                        "Descricao: ${vaga.descricao}\n" +
                        "Competencias: "
        )
        vaga.competencias?.each { comp ->
            println "${comp.nome} "
        }
        println "\n"
    }

    def calcularAfinidade(reader) {
        List<Candidato> candidatos = candidatoController.listarCandidatos()

        println "\nCandidatos"
        candidatos.eachWithIndex { candidato, index ->
            println "${index + 1}. ${candidato.nome}\nCompetencias:"
            candidato.competencias.each {
                println("${it.nome}")
            }
        }

        print "Escolha o candidato: "
        int opcaoCandidato = Util.leInteiroConsole(reader)
        if (opcaoCandidato < 1 || opcaoCandidato > candidatos.size()) {
            println "Candidato invalido!\n"
            return
        }

        Candidato cand = candidatoController.buscarPorId(candidatos[opcaoCandidato - 1].id)

        List<Vaga> vagas = vagaController.listarTodas()

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println (
                    "${index + 1}." +
                            "Nome: ${vaga.nome}\n" +
                            "Empresa: ${vaga.empresa.nome}\n" +
                            "Descricao: ${vaga.descricao}\n" +
                            "Competencias: "
            )
            vaga.competencias?.forEach { comp ->
                println("${comp.nome}, ")
            }
            print "\n"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vaga = vagaController.buscarPorId(vagas[opcaoVaga - 1].id)

        String resposta = curtidaController.calcularAfinidade(cand.id, vaga.id)
        println "\n${resposta}\n"
    }
}