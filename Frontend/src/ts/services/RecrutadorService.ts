import { apiFetch } from '../../api/api'
import type {
    AvaliacaoRecrutador,
    CandidatoAnonimo,
    MatchRecrutador,
    NovoRecrutador,
    Recrutador,
    VagaRecrutador
} from '../models/Recrutador'

const avaliacoesDaSessao = new Map<number, Set<number>>()

export class RecrutadorService {
    async obterPerfil(): Promise<Recrutador> {
        return apiFetch<Recrutador>('/recrutadores/me')
    }

    async cadastrar(payload: NovoRecrutador): Promise<Recrutador> {
        return apiFetch<Recrutador>('/recrutadores/empresa/recrutadores', {
            method: 'POST',
            body: payload
        })
    }

    async listarDaEmpresa(): Promise<Recrutador[]> {
        const response = await apiFetch<unknown>('/recrutadores/empresa/recrutadores')
        return Array.isArray(response) ? response as Recrutador[] : []
    }

    async desativar(id: number): Promise<void> {
        await apiFetch(`/recrutadores/empresa/recrutadores/${id}`, { method: 'DELETE' })
    }

    async listarVagas(): Promise<VagaRecrutador[]> {
        const [perfil, response] = await Promise.all([
            this.obterPerfil(),
            apiFetch<unknown>('/recrutadores/me/vagas')
        ])
        const cnpjEmpresa = perfil.empresa?.cnpj
        if (!cnpjEmpresa) {
            throw new Error('A API não informou o CNPJ da empresa vinculada ao recrutador.')
        }
        if (!Array.isArray(response)) return []
        return (response as VagaRecrutador[]).map((vaga) => ({
            ...vaga,
            competencias: Array.isArray(vaga.competencias) ? vaga.competencias : [],
            empresa: vaga.empresa
        })).filter((vaga) => vaga.empresa?.cnpj === cnpjEmpresa)
    }

    async listarCandidatos(vagaId: number): Promise<CandidatoAnonimo[]> {
        await this.validarVagaDaEmpresa(vagaId)
        const response = await apiFetch<unknown>(`/recrutadores/me/vagas/${vagaId}/candidatos`)
        if (!Array.isArray(response)) return []
        const jaAvaliados = avaliacoesDaSessao.get(vagaId) ?? new Set<number>()
        return (response as CandidatoAnonimo[]).filter(Boolean).map((candidato) => ({
            ...candidato,
            competencias: Array.isArray(candidato.competencias) ? candidato.competencias : [],
            formacoes: Array.isArray(candidato.formacoes) ? candidato.formacoes : []
        })).filter(
            (candidato) => !jaAvaliados.has(candidato.id)
        )
    }

    async avaliar(candidatoId: number, vagaId: number, curtiu: boolean): Promise<void> {
        await this.validarVagaDaEmpresa(vagaId)
        await apiFetch('/recrutadores/me/avaliacoes', {
            method: 'POST',
            body: { candidatoId, vagaId, curtiu }
        })
        const candidatos = avaliacoesDaSessao.get(vagaId) ?? new Set<number>()
        candidatos.add(candidatoId)
        avaliacoesDaSessao.set(vagaId, candidatos)
    }

    private async validarVagaDaEmpresa(vagaId: number): Promise<void> {
        const vagas = await this.listarVagas()
        if (!vagas.some((vaga) => vaga.id === vagaId)) {
            throw new Error('A vaga não pertence à empresa vinculada ao recrutador.')
        }
    }

    async listarHistorico(): Promise<AvaliacaoRecrutador[]> {
        const response = await apiFetch<unknown>('/recrutadores/me/historico')
        return Array.isArray(response) ? response as AvaliacaoRecrutador[] : []
    }

    async listarMatchesCandidato(): Promise<MatchRecrutador[]> {
        const response = await apiFetch<unknown>('/recrutadores/matches/candidato')
        return Array.isArray(response) ? response as MatchRecrutador[] : []
    }
}
