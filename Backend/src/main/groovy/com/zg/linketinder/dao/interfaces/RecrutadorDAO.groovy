package com.zg.linketinder.dao.interfaces

import com.zg.linketinder.dto.AvaliacaoRecrutadorDTO
import com.zg.linketinder.dto.CandidatoAnonimoDTO
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.dto.VagaResumoDTO
import com.zg.linketinder.dto.MatchRecrutadorDTO

interface RecrutadorDAO {
    void salvar(Recrutador recrutador)
    Recrutador buscarPorId(Integer id)
    Recrutador buscarPorEmail(String email)
    boolean existeCpf(String cpf)
    List<Recrutador> listarPorEmpresa(Integer empresaId)
    boolean desativar(Integer recrutadorId, Integer empresaId)
    List<VagaResumoDTO> listarVagasDaArea(Integer recrutadorId)
    List<CandidatoAnonimoDTO> listarCandidatosAnonimos(Integer recrutadorId, Integer vagaId)
    boolean registrarAvaliacao(Integer recrutadorId, Integer candidatoId, Integer vagaId, Boolean curtiu)
    List<AvaliacaoRecrutadorDTO> listarHistorico(Integer recrutadorId)
    List<MatchRecrutadorDTO> listarMatchesCandidato(Integer candidatoId)
}
