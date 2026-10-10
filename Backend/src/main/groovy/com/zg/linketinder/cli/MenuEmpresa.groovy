package com.zg.linketinder.cli

import com.zg.linketinder.controller.CandidatoController
import com.zg.linketinder.controller.CompetenciaController
import com.zg.linketinder.controller.CurtidaController
import com.zg.linketinder.controller.EmpresaController
import com.zg.linketinder.controller.VagaController
import com.zg.linketinder.dto.EmpresaDTO
import com.zg.linketinder.dto.VagaDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.util.Util

class MenuEmpresa {
    EmpresaController empresaController = new EmpresaController()
    CandidatoController candidatoController = new CandidatoController()
    CurtidaController curtidaController = new CurtidaController()
    CompetenciaController competenciaController = new CompetenciaController()
    VagaController vagaController = new VagaController()

    def executaEmpresa() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        while (true) {
            println "1. Cadastrar Empresa"
            println "2. Atualizar Empresa"
            println "3. Excluir Empresa"
            println "4. Listar Candidatos Cutiram Vaga Empresa"
            println "5. Buscar id Empresa"
            println "6. Buscar email Empresa"
            println "7. Buscar CNPJ Empresa"
            println "8. Cadastrar Competencia Vaga Empresa"
            println "9. Excluir Competencia Vaga Empresa"
            println "10. Listar Competencias Vaga Empresa"
            println "11. Buscar id Competencias Vaga Empresa"
            println "12. Buscar nome Competencias Vaga Empresa"
            println "13. Salvar Curtida Empresa"
            println "14. Listar Curtidas Empresa"
            println "15. Listar Matches Empresa"
            println "16. Listar Vagas Empresa"
            println "17. Buscar id Vagas Empresa"
            println "18. Buscar nome Vagas Empresa"
            println "19. Cadastrar Vaga"
            println "20. Atualizar Vaga"
            println "21. Excluir Vaga"
            println "22. Sair"
            print "Escolha: "
            int opcao = Util.leInteiroConsole(reader)
            switch (opcao) {
                case 1:
                    cadastrarEmpresa(reader)
                    break
                case 2:
                    atualizarEmpresa(reader)
                    break
                case 3:
                    excluirEmpresa(reader)
                    break
                case 4:
                    listarCandidatosAnonimos(reader)
                    break
                case 5:
                    buscarIdEmpresa(reader)
                    break
                case 6:
                    buscarEmailEmpresa(reader)
                    break
                case 7:
                    buscarCnpjEmpresa(reader)
                    break
                case 8:
                    cadastrarCompetenciaVagaEmpresa(reader)
                    break
                case 9:
                    excluirCompetenciaVagaEmpresa(reader)
                    break
                case 10:
                    listarCompetenciasVagaEmpresa(reader)
                    break
                case 11:
                    buscarIdCompetenciaVagaEmpresa(reader)
                    break
                case 12:
                    buscarNomeCompetenciaVagaEmpresa(reader)
                    break
                case 13:
                    salvarCurtidaEmpresa(reader)
                    break
                case 14:
                    listarCurtidasEmpresa()
                    break
                case 15:
                    listarMatchesEmpresa(reader)
                    break
                case 16:
                    listarVagasEmpresa(reader)
                    break
                case 17:
                    buscarIdVagasEmpresa(reader)
                    break
                case 18:
                    buscarNomeVagasEmpresa(reader)
                    break
                case 19:
                    cadastrarVagaEmpresa(reader)
                    break
                case 20:
                    atualizarVagaEmpresa(reader)
                    break
                case 21:
                    excluirVagaEmpresa(reader)
                    break
                case 22:
                    print "Saindo\n"
                    return
                default:
                    println "Opcao incorreta! Digite novamante\n"
            }
        }
    }

    def cadastrarEmpresa(reader) {
        println "Cadastro de empresa"
        print "Cnpj: "
        String cnpj = Util.leStringConsole(reader)
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

        EmpresaDTO dto = new EmpresaDTO(
                cnpj: cnpj, nome: nome, email: email, senha: senha,
                pais: pais, estado: estado, cidade: cidade, cep: cep, descricao: descricao
        )

        String resposta = empresaController.cadastrarEmpresa(dto)
        println "\n${resposta}\n"
    }

    def atualizarEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome} - ${empresa.cnpj.valor}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        println "Atualizar cadastro de empresa"
        print "Cnpj: "
        String cnpj = Util.leStringConsole(reader)
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

        EmpresaDTO dto = new EmpresaDTO(
                cnpj: cnpj, nome: nome, email: email, senha: senha,
                pais: pais, estado: estado, cidade: cidade, cep: cep, descricao: descricao
        )

        String resposta = empresaController.atualizarEmpresa(empresaSelecionada.id, dto)
        println "\n${resposta}\n"
    }

    def excluirEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome} - ${empresa.cnpj.valor}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        String resposta = empresaController.excluirEmpresa(empresaSelecionada.id)
        println "\n${resposta}\n"
    }

    def buscarIdEmpresa(reader) {
        println "Busca id de empresa"
        print "Digite o id da empresa: "
        int id = Util.leInteiroConsole(reader)

        Empresa empresa = empresaController.buscarPorId(id)

        if (empresa == null) {
            println "\nEmpresa nao encontrada!\n"
            return
        }

        println(
                "Cnpj: ${empresa.cnpj.valor}\n" +
                        "Nome: ${empresa.nome}\n" +
                        "Email: ${empresa.email.valor}\n" +
                        "Pais: ${empresa.endereco.pais}\n" +
                        "Estado: ${empresa.endereco.estado}\n" +
                        "Cidade: ${empresa.endereco.cidade}\n" +
                        "Cep: ${empresa.endereco.cep}\n" +
                        "Descricao: ${empresa.descricao}\n"
        )
    }

    def buscarEmailEmpresa(reader) {
        println "Busca email de empresa"
        print "Digite o email da empresa: "
        String email = Util.leStringConsole(reader)

        Empresa empresa = empresaController.buscarPorEmail(email)

        if (empresa == null) {
            println "\nEmpresa nao encontrada!\n"
            return
        }

        println(
                "Cnpj: ${empresa.cnpj.valor}\n" +
                        "Nome: ${empresa.nome}\n" +
                        "Email: ${empresa.email.valor}\n" +
                        "Pais: ${empresa.endereco.pais}\n" +
                        "Estado: ${empresa.endereco.estado}\n" +
                        "Cidade: ${empresa.endereco.cidade}\n" +
                        "Cep: ${empresa.endereco.cep}\n" +
                        "Descricao: ${empresa.descricao}\n"
        )
    }

    def buscarCnpjEmpresa(reader) {
        println "Busca CNPJ de empresa"
        print "Digite o CNPJ da empresa: "
        String cnpj = Util.leStringConsole(reader)

        Empresa empresa = empresaController.buscarPorCnpj(cnpj)

        if (empresa == null) {
            println "\nEmpresa nao encontrada!\n"
            return
        }

        println(
                "Cnpj: ${empresa.cnpj.valor}\n" +
                        "Nome: ${empresa.nome}\n" +
                        "Email: ${empresa.email.valor}\n" +
                        "Pais: ${empresa.endereco.pais}\n" +
                        "Estado: ${empresa.endereco.estado}\n" +
                        "Cidade: ${empresa.endereco.cidade}\n" +
                        "Cep: ${empresa.endereco.cep}\n" +
                        "Descricao: ${empresa.descricao}\n"
        )
    }

    def cadastrarVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        println "Cadastro de Vaga"
        print "Nome da Vaga: "
        String nome = Util.leStringConsole(reader)
        print "Descricao: "
        String descricao = Util.leStringConsole(reader)
        print "Pais: "
        String pais = Util.leStringConsole(reader)
        print "Estado: "
        String estado = Util.leStringConsole(reader)
        print "Cidade: "
        String cidade = Util.leStringConsole(reader)

        VagaDTO dto = new VagaDTO(
                nome: nome, descricao: descricao,
                pais: pais, estado: estado, cidade: cidade,
                empresa: empresaSelecionada
        )

        String resposta = vagaController.cadastrarVaga(dto)
        println "\n${resposta}\n"
    }

    def atualizarVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresaSelecionada.id)
        if (vagas.isEmpty()) {
            println "Esta empresa nao possui vagas cadastradas.\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Escolha a vaga para atualizar: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vagaSelecionada = vagas[opcaoVaga - 1]

        println "Atualizar Vaga"
        print "Novo Nome da Vaga: "
        String nome = Util.leStringConsole(reader)
        print "Nova Descricao: "
        String descricao = Util.leStringConsole(reader)
        print "Novo Pais: "
        String pais = Util.leStringConsole(reader)
        print "Novo Estado: "
        String estado = Util.leStringConsole(reader)
        print "Nova Cidade: "
        String cidade = Util.leStringConsole(reader)

        VagaDTO dto = new VagaDTO(
                id: vagaSelecionada.id, nome: nome, descricao: descricao,
                pais: pais, estado: estado, cidade: cidade,
                empresa: empresaSelecionada
        )

        String resposta = vagaController.atualizarVaga(vagaSelecionada.id, dto)
        println "\n${resposta}\n"
    }

    def excluirVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresaSelecionada.id)
        if (vagas.isEmpty()) {
            println "Esta empresa nao possui vagas cadastradas.\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Escolha a vaga para excluir: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vagaSelecionada = vagas[opcaoVaga - 1]

        String resposta = vagaController.excluirVaga(empresaSelecionada.id, vagaSelecionada.id)
        println "\n${resposta}\n"
    }

    def listarVagasEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresas[opcaoEmpresa - 1].id)

        if (vagas.isEmpty()) {
            println "Nenhuma vaga encontrada para esta empresa.\n"
            return
        }

        vagas.each {
            println(
                    "Nome: ${it.nome}\n" +
                            "Empresa: ${it.empresa.nome}\n" +
                            "Descricao: ${it.descricao}\n"
            )
        }
    }

    def buscarIdVagasEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        print "Digite o id exato da vaga: "
        int id_vaga = Util.leInteiroConsole(reader)

        Vaga vaga = vagaController.buscarIdVagaEmpresa(empresas[opcaoEmpresa - 1].id, id_vaga)

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

    def buscarNomeVagasEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        print "Digite o nome exato da vaga: "
        String nome = Util.leStringConsole(reader)

        Vaga vaga = vagaController.buscarNomeVagaEmpresa(empresas[opcaoEmpresa - 1].id, nome)

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

    def cadastrarCompetenciaVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresa = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresa.id)
        if (vagas.isEmpty()) {
            println "\nEsta empresa nao possui vagas cadastradas.\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vaga = vagas[opcaoVaga - 1]

        List<Competencia> competencias = competenciaController.listarTodas()

        print "Competencias: "
        competencias.eachWithIndex { competencia, index ->
            println "${index + 1}. ${competencia.nome}"
        }

        print "Escolha o competencia: "
        int opcaoCompetencia = Util.leInteiroConsole(reader)
        if (opcaoCompetencia < 1 || opcaoCompetencia > competencias.size()) {
            println "Competencia invalida!\n"
            return
        }

        Competencia competencia = competencias[opcaoCompetencia - 1]

        String resposta = competenciaController.adicionarCompetenciaVaga(vaga.id, competencia.id)
        println "\n${resposta}\n"
    }

    def excluirCompetenciaVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresaSelecionada.id)
        if (vagas.isEmpty()) {
            println "Esta empresa nao possui vagas cadastradas.\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vagaSelecionada = vagas[opcaoVaga - 1]

        List<Competencia> competencias = competenciaController.listarCompetenciasVaga(vagaSelecionada.id)
        if (competencias.isEmpty()) {
            println "Esta vaga nao possui competencias cadastradas!\n"
            return
        }

        print "Competencias: "
        competencias.eachWithIndex { competencia, index ->
            println "${index + 1}. ${competencia.nome}"
        }

        print "Escolha o competencia: "
        int opcaoCompetencia = Util.leInteiroConsole(reader)
        if (opcaoCompetencia < 1 || opcaoCompetencia > competencias.size()) {
            println "Competencia invalida!\n"
            return
        }

        Competencia comp = competencias[opcaoCompetencia - 1]

        String resposta = competenciaController.excluirCompetenciaVaga(vagaSelecionada.id, comp.id)
        println "\n${resposta}\n"
    }

    def listarCompetenciasVagaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresaSelecionada.id)
        if (vagas.isEmpty()) {
            println "Esta empresa nao possui vagas cadastradas.\n"
            return
        }

        println "\nVagas"
        vagas.eachWithIndex { vaga, index ->
            println "${index + 1}. ${vaga.nome}"
        }

        print "Escolha a vaga: "
        int opcaoVaga = Util.leInteiroConsole(reader)
        if (opcaoVaga < 1 || opcaoVaga > vagas.size()) {
            println "Vaga invalido!\n"
            return
        }

        Vaga vagaSelecionada = vagas[opcaoVaga - 1]

        List<Competencia> competencias = competenciaController.listarCompetenciasVaga(vagaSelecionada.id)
        if (competencias.isEmpty()) {
            println "Nenhuma competencia encontrada para esta vaga.\n"
            return
        }

        competencias.forEach {
            println(
                    "Nome: ${it.nome}"
            )
        }
    }

    def buscarIdCompetenciaVagaEmpresa(reader) {
        println "Busca id de competencia"
        print "Digite o id da competencia: "
        int id_competencia = Util.leInteiroConsole(reader)

        println "Busca id de vaga"
        print "Digite o id da vaga: "
        int id_vaga = Util.leInteiroConsole(reader)

        Competencia competencia = competenciaController.buscarIdCompetenciaVaga(id_vaga, id_competencia)
        Vaga vaga = vagaController.buscarPorId(id_vaga)

        if (vaga == null) {
            println "\nVaga nao encontrada!\n"
            return
        }

        if (competencia == null) {
            println "\nCompetencia nao encontrada para esta vaga!\n"
            return
        }

        println "Nome: ${vaga.nome}"
        println("Competencia: ${competencia.nome}\n")
    }

    def buscarNomeCompetenciaVagaEmpresa(reader) {
        println "Busca nome de competencia"
        print "Digite o nome da competencia: "
        String nome_competencia = Util.leStringConsole(reader)

        println "Busca id de vaga"
        print "Digite o id da vaga: "
        int id_vaga = Util.leInteiroConsole(reader)

        Competencia competencia = competenciaController.buscarNomeCompetenciaVaga(id_vaga, nome_competencia)
        Vaga vaga = vagaController.buscarPorId(id_vaga)

        if (vaga == null) {
            println "\nVaga nao encontrada!\n"
            return
        }

        if (competencia == null) {
            println "\nCompetencia nao encontrada para esta vaga!\n"
            return
        }

        println "Nome: ${vaga.nome}"
        println("Competencia: ${competencia.nome}")
    }

    def listarCandidatosAnonimos(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Candidato> candidatos = candidatoController.listarCandidatoEmpresa(empresaSelecionada.id)
        if (candidatos.isEmpty()) {
            println "\nNenhum candidato anonimo curtiu as vagas dessa empresa!\n"
            return
        }

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

    def salvarCurtidaEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresa = empresas[opcaoEmpresa - 1]

        List<Vaga> vagas = vagaController.listarVagasEmpresa(empresa.id)

        if (vagas.isEmpty()) {
            println "\nEsta empresa nao possui vagas cadastradas.\n"
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

        Vaga vaga = vagas[opcaoVaga - 1]

        List<Candidato> candidatos = candidatoController.listarCandidatos()

        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado!\n"
            return
        }

        println "\nCandidatos"
        candidatos.eachWithIndex { candidato, index ->
            println "${index + 1}. ${candidato.nome}"
        }

        print "Escolha o candidato para curtir: "
        int opcaoCandidato = Util.leInteiroConsole(reader)
        if (opcaoCandidato < 1 || opcaoCandidato > candidatos.size()) {
            println "Candidato invalido!\n"
            return
        }

        Candidato candidato = candidatos[opcaoCandidato - 1]

        String resposta = curtidaController.curtirCandidatoComoEmpresa(empresa.id, candidato.id, vaga.id)
        println "\n${resposta}\n"
    }

    def listarCurtidasEmpresa() {
        List<CurtidaEmpresa> curtidas = curtidaController.listarCurtidasEmpresa()

        if (curtidas.isEmpty()) {
            println "\nNenhuma curtida encontrado\n"
            return
        }

        println "\nCurtidas"
        curtidas.each {
            println "Empresa ${it.empresa.nome} curtiu Candidato ${it.candidato.nome} para Vaga ${it.vaga.nome}"
        }
        print "\n"
    }

    def listarMatchesEmpresa(reader) {
        List<Empresa> empresas = empresaController.listarEmpresas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada!\n"
            return
        }

        println "\nEmpresas"
        empresas.eachWithIndex { empresa, index ->
            println "${index + 1}. ${empresa.nome}"
        }

        print "Escolha a empresa: "
        int opcaoEmpresa = Util.leInteiroConsole(reader)
        if (opcaoEmpresa < 1 || opcaoEmpresa > empresas.size()) {
            println "Empresa invalida!\n"
            return
        }

        Empresa empresaSelecionada = empresas[opcaoEmpresa - 1]

        List<Match> matches = curtidaController.listarMatchesEmpresa(empresaSelecionada.id)

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
}