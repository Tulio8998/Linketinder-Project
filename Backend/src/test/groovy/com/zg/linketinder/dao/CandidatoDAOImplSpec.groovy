package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import java.time.LocalDate

class CandidatoDAOImplSpec extends DaoIntegrationSpec {
    CandidatoDAO candidatoDAO = new CandidatoDAOImpl()

    def setup() {
        limparBanco()
    }

    def "deve listar todos os candidatos"() {
        given: "dois candidatos salvos"
        candidatoDAO.salvar(
                criarCandidato('111.111.111-11', 'Tulio', 't@email.com')
        )
        candidatoDAO.salvar(criarCandidato(
                '222.222.222-22', 'Ana', 'a@email.com')
        )

        when: "listo todos"
        List<Candidato> lista = candidatoDAO.listarTodos()

        then: "recebo os dois"
        lista.size() == 2
        lista*.nome.containsAll(['Tulio', 'Ana'])
    }

    def "deve buscar o id de determinado candidato"() {
        given: "um candidato salvo"
        def candidato = criarCandidato('111.111.111-11', 'Tulio', 't@email.com')
        candidatoDAO.salvar(candidato)

        when: "busco pelo id"
        def encontrado = candidatoDAO.buscarPorId(candidato.id)

        then: "encontro o candidato"
        encontrado != null
        encontrado.nome == 'Tulio'
        encontrado.cpf == '111.111.111-11'
    }

    def "deve salvar o candidato no banco"() {
    }

    def "deve atualizar o candidato"() {
        given: "um candidato salvo"
        def candidato = criarCandidato('111.111.111-11', 'Tulio', 't@email.com')
        candidatoDAO.salvar(candidato)

        when: "atualizo o nome"
        candidato.nome = 'Tulio Vilela'
        candidatoDAO.atualizar(candidato.id, candidato)

        then: "o banco reflete"
        candidatoDAO.buscarPorId(candidato.id).nome == 'Tulio Vilela'
    }

    def "deve listar os candidatos para a empresa"() {
    }

    def "deve buscar email de candidato"() {
        given: "um candidato salvo"
        def candidato = criarCandidato('111.111.111-11', 'Tulio', 't@email.com')
        candidatoDAO.salvar(candidato)

        when: "busco pelo email"
        def encontrado = candidatoDAO.buscarEmailCandidato('t@email.com')

        then: "encontro o candidato"
        encontrado != null
        encontrado.nome == 'Tulio'
    }

    def "deve buscar cpf de candidato"() {
        given: "um candidato salvo"
        def candidato = criarCandidato('111.111.111-11', 'Tulio', 't@email.com')
        candidatoDAO.salvar(candidato)

        when: "busco pelo cpf"
        def encontrado = candidatoDAO.buscarCpfCandidato('111.111.111-11')

        then: "encontro o candidato"
        encontrado != null
        encontrado.nome == 'Tulio'
    }

    private Candidato criarCandidato(String cpf, String nome, String email) {
        return new Candidato(
                cpf: cpf,
                nome: nome,
                email: email,
                senha: '12345678',
                pais: 'Brasil',
                estado: 'MG',
                cidade: 'Belo Horizonte',
                cep: '00000-000',
                dataNascimento: LocalDate.of(2004, 10, 1),
                descricao: 'Descricao',
                competencias: []
        )
    }
}
