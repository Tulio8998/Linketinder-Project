package com.zg.linketinder.service

import com.zg.linketinder.dao.RecrutadorDAOImpl
import com.zg.linketinder.dao.interfaces.RecrutadorDAO
import com.zg.linketinder.dto.AvaliacaoRecrutadorDTO
import com.zg.linketinder.dto.CandidatoAnonimoDTO
import com.zg.linketinder.dto.MatchRecrutadorDTO
import com.zg.linketinder.dto.VagaResumoDTO
import com.zg.linketinder.dto.VagaTriagemDTO
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.util.AreaAtuacao
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO

class RecrutadorService {
    private final RecrutadorDAO recrutadorDAO

    RecrutadorService() {
        this(new RecrutadorDAOImpl())
    }

    RecrutadorService(RecrutadorDAO recrutadorDAO) {
        this.recrutadorDAO = recrutadorDAO
    }

    void cadastrar(Recrutador recrutador) {
        validarCadastro(recrutador)
        if (recrutadorDAO.buscarPorEmail(recrutador.email.valor) != null) {
            throw new IllegalArgumentException('Email corporativo ja cadastrado')
        }
        if (recrutadorDAO.existeCpf(recrutador.cpf.valor)) {
            throw new IllegalArgumentException('CPF ja cadastrado')
        }
        recrutador.area = AreaAtuacao.normalizar(recrutador.area)
        recrutador.email = new EmailVO(recrutador.email.valor.trim().toLowerCase(Locale.ROOT))
        recrutador.ativo = true
        recrutadorDAO.salvar(recrutador)
    }

    Recrutador buscarPorEmail(String email) {
        if (email == null || email.isBlank()) return null
        return recrutadorDAO.buscarPorEmail(email.trim())
    }

    List<Recrutador> listarPorEmpresa(Integer empresaId) {
        validarId(empresaId, 'Empresa')
        return recrutadorDAO.listarPorEmpresa(empresaId)
    }

    void desativar(Integer recrutadorId, Integer empresaId) {
        validarId(recrutadorId, 'Recrutador')
        validarId(empresaId, 'Empresa')
        if (!recrutadorDAO.desativar(recrutadorId, empresaId)) {
            throw new IllegalArgumentException('Recrutador nao encontrado ou ja inativo')
        }
    }

    List<VagaTriagemDTO> listarVagasDaArea(Integer recrutadorId) {
        validarId(recrutadorId, 'Recrutador')
        return recrutadorDAO.listarVagasDaArea(recrutadorId)
    }

    List<CandidatoAnonimoDTO> listarCandidatosAnonimos(Integer recrutadorId, Integer vagaId) {
        validarId(recrutadorId, 'Recrutador')
        validarId(vagaId, 'Vaga')
        return recrutadorDAO.listarCandidatosAnonimos(recrutadorId, vagaId)
    }

    boolean registrarAvaliacao(Integer recrutadorId, Integer candidatoId, Integer vagaId, Boolean curtiu) {
        validarId(recrutadorId, 'Recrutador')
        validarId(candidatoId, 'Candidato')
        validarId(vagaId, 'Vaga')
        if (curtiu == null) throw new IllegalArgumentException('A avaliacao deve ser positiva ou negativa')
        return recrutadorDAO.registrarAvaliacao(recrutadorId, candidatoId, vagaId, curtiu)
    }

    List<AvaliacaoRecrutadorDTO> listarHistorico(Integer recrutadorId) {
        validarId(recrutadorId, 'Recrutador')
        return recrutadorDAO.listarHistorico(recrutadorId)
    }

    List<MatchRecrutadorDTO> listarMatchesCandidato(Integer candidatoId) {
        validarId(candidatoId, 'Candidato')
        return recrutadorDAO.listarMatchesCandidato(candidatoId)
    }

    private static void validarCadastro(Recrutador recrutador) {
        if (recrutador == null) throw new IllegalArgumentException('Recrutador obrigatorio')
        validarId(recrutador.empresa?.id, 'Empresa')
        if (recrutador.nome == null || recrutador.nome.isBlank()) {
            throw new IllegalArgumentException('Nome obrigatorio')
        }
        if (recrutador.descricao == null || recrutador.descricao.isBlank()) {
            throw new IllegalArgumentException('Descricao obrigatoria')
        }
        if (recrutador.email == null) throw new IllegalArgumentException('Email obrigatorio')
        if (recrutador.cpf == null) throw new IllegalArgumentException('CPF obrigatorio')
        if (recrutador.dataNascimento == null) {
            throw new IllegalArgumentException('Data de nascimento obrigatoria')
        }
        if (recrutador.senha == null || recrutador.senha.isBlank()) {
            throw new IllegalArgumentException('Senha obrigatoria')
        }
        recrutador.area = AreaAtuacao.normalizar(recrutador.area)
    }

    private static void validarId(Integer id, String entidade) {
        if (id == null || id < 1) throw new IllegalArgumentException("${entidade} invalido")
    }
}
