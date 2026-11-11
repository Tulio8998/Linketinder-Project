package com.zg.linketinder.dao.interfaces

import com.zg.linketinder.model.AvaliacaoRecrutador
import com.zg.linketinder.model.PerfilCandidatoAnonimo
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.dto.MatchRecrutadorDTO

interface RecrutadorDAO {
    void salvar(Recrutador recrutador)
    Recrutador buscarPorId(Integer id)
    Recrutador buscarPorEmail(String email)
    boolean existeCpf(String cpf)
    List<Recrutador> listarPorEmpresa(Integer empresaId)
    boolean desativar(Integer recrutadorId, Integer empresaId)
    List<Vaga> listarVagasDaArea(Integer recrutadorId)
    List<PerfilCandidatoAnonimo> listarCandidatosAnonimos(Integer recrutadorId, Integer vagaId)
    boolean registrarAvaliacao(Integer recrutadorId, Integer candidatoId, Integer vagaId, Boolean curtiu)
    List<AvaliacaoRecrutador> listarHistorico(Integer recrutadorId)
    List<MatchRecrutadorDTO> listarMatchesCandidato(Integer candidatoId)
}
