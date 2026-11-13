import { apiFetch } from '../../api/api'
export interface MatchCandidato {
    candidato: {
        id: number
        nome: string
        email: string
        descricao: string
    }
    empresa: { id: number; nome: string; email: string; cnpj: string }
    vaga: {
        id: number
        nome: string
        descricao: string
    }
}

export interface MatchEmpresa {
    candidato: {
        id: number
        nome: string
        email: string | { valor: string }
        descricao?: string
    }
    vaga: { id: number; nome: string; descricao?: string }
}

interface AfinidadeApi {
    vagaNome: string
    area: string
    afinidade: number
}

const avaliacoesCandidato = new Map<number, Set<number>>()

export class CurtidaService {
    async salvarCurtidaCandidato(
        candidatoId: number,
        vagaId: number,
        curtiu: boolean
    ): Promise<void> {
        await apiFetch('/curtidas/candidato', {
            method: 'POST',
            body: { candidatoId, vagaId, curtiu }
        })
        const vagas = avaliacoesCandidato.get(candidatoId) ?? new Set<number>()
        vagas.add(vagaId)
        avaliacoesCandidato.set(candidatoId, vagas)
    }

    async listarMatchesCandidato(candidatoId: number): Promise<MatchCandidato[]> {
        const response = await apiFetch<unknown>(`/curtidas/matches/candidato/${candidatoId}`)
        return Array.isArray(response) ? response as MatchCandidato[] : []
    }

    async listarMatchesEmpresa(empresaId: number): Promise<MatchEmpresa[]> {
        const response = await apiFetch<unknown>(`/curtidas/matches/empresa/${empresaId}`)
        return Array.isArray(response) ? response as MatchEmpresa[] : []
    }

    async listarVagasAvaliadasCandidato(candidatoId: number): Promise<Set<number>> {
        const ids = new Set(avaliacoesCandidato.get(candidatoId) ?? [])
        const matches = await this.listarMatchesCandidato(candidatoId)
        matches.forEach((match) => {
            const vagaId = Number(match.vaga?.id)
            if (Number.isInteger(vagaId) && vagaId > 0) ids.add(vagaId)
        })
        return ids
    }

    async calcularAfinidade(candidatoId: number, vagaId: number): Promise<number> {
        const resultado = await apiFetch<AfinidadeApi>(
            `/curtidas/afinidade?candidatoId=${candidatoId}&vagaId=${vagaId}`
        )
        return resultado.afinidade
    }
}
