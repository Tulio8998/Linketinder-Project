import { apiFetch } from '../../api/api'
import type { Competencia } from '../models/Competencia'

export class CompetenciaService {
    async listarTodas(): Promise<Competencia[]> {
        return apiFetch<Competencia[]>('/competencias', { authenticated: false })
    }

    async listarDoCandidato(candidatoId: number): Promise<Competencia[]> {
        return apiFetch<Competencia[]>(`/competencias/candidato/${candidatoId}`)
    }

    async buscarNomeDoCandidato(candidatoId: number, nome: string): Promise<Competencia> {
        return apiFetch<Competencia>(
            `/competencias/candidato/${candidatoId}?nome=${encodeURIComponent(nome)}`
        )
    }

    async buscarDoCandidato(candidatoId: number, competenciaId: number): Promise<Competencia> {
        return apiFetch<Competencia>(`/competencias/candidato/${candidatoId}/${competenciaId}`)
    }

    async adicionarAoCandidato(candidatoId: number, competenciaId: number): Promise<void> {
        await apiFetch('/competencias/candidato', {
            method: 'POST',
            body: { candidatoId, competenciaId }
        })
    }

    async excluirDoCandidato(candidatoId: number, competenciaId: number): Promise<void> {
        await apiFetch(`/competencias/candidato/${candidatoId}/${competenciaId}`, {
            method: 'DELETE'
        })
    }

    async substituirDoCandidato(candidatoId: number, nomes: string[]): Promise<void> {
        const [catalogo, atuais] = await Promise.all([
            this.listarTodas(),
            this.listarDoCandidato(candidatoId)
        ])
        const porNome = new Map(catalogo.map((competencia) => [
            competencia.nome.trim().toLowerCase(),
            competencia.id
        ]))
        const desejadas = [...new Set(nomes.map((nome) => {
            const id = porNome.get(nome.trim().toLowerCase())
            if (id === undefined) {
                throw new Error(`A competência "${nome}" não existe no catálogo da API.`)
            }
            return id
        }))]
        const atuaisIds = atuais.map((competencia) => competencia.id)
        await Promise.all([
            ...atuaisIds
                .filter((id) => !desejadas.includes(id))
                .map((id) => this.excluirDoCandidato(candidatoId, id)),
            ...desejadas
                .filter((id) => !atuaisIds.includes(id))
                .map((id) => this.adicionarAoCandidato(candidatoId, id))
        ])
    }

    async listarDaVaga(vagaId: number): Promise<Competencia[]> {
        return apiFetch<Competencia[]>(`/competencias/vaga/${vagaId}`)
    }

    async buscarNomeDaVaga(vagaId: number, nome: string): Promise<Competencia> {
        return apiFetch<Competencia>(
            `/competencias/vaga/${vagaId}?nome=${encodeURIComponent(nome)}`
        )
    }

    async buscarDaVaga(vagaId: number, competenciaId: number): Promise<Competencia> {
        return apiFetch<Competencia>(`/competencias/vaga/${vagaId}/${competenciaId}`)
    }

    async adicionarNaVaga(vagaId: number, competenciaId: number): Promise<void> {
        await apiFetch('/competencias/vaga', {
            method: 'POST',
            body: { vagaId, competenciaId }
        })
    }

    async excluirDaVaga(vagaId: number, competenciaId: number): Promise<void> {
        await apiFetch(`/competencias/vaga/${vagaId}/${competenciaId}`, {
            method: 'DELETE'
        })
    }
}
