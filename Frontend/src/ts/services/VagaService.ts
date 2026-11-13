import { apiFetch } from '../../api/api'
import type { Competencia } from '../models/Competencia'
import type { Vaga } from '../models/Vaga'
import { CompetenciaService } from './CompetenciaService'

export interface VagaPayload {
    nome: string
    descricao: string
    area: string
    pais: string
    estado: string
    cidade: string
    empresa: { id: number }
}

interface VagaApi {
    id: number
    nome: string
    descricao: string
    area: string
    pais?: string
    estado?: string
    cidade?: string
    competencias?: Array<string | Competencia>
}

function mapearVaga(vaga: VagaApi): Vaga {
    return {
        id: vaga.id,
        nome: vaga.nome,
        descricao: vaga.descricao,
        area: vaga.area,
        pais: vaga.pais,
        estado: vaga.estado,
        cidade: vaga.cidade,
        competencias: (Array.isArray(vaga.competencias) ? vaga.competencias : []).map((competencia) =>
            typeof competencia === 'string' ? competencia : competencia.nome
        )
    }
}

export class VagaService {
    private readonly competencias = new CompetenciaService()

    async listarVagas(): Promise<Vaga[]> {
        const response = await apiFetch<unknown>('/vagas', { authenticated: false })
        if (!Array.isArray(response)) return []
        return response.map((vaga) => mapearVaga(vaga as VagaApi))
    }

    async buscarVagaPorId(id: number): Promise<Vaga> {
        return mapearVaga(await apiFetch<VagaApi>(`/vagas/${id}`, { authenticated: false }))
    }

    async buscarVagaPorNome(nome: string): Promise<Vaga> {
        return mapearVaga(await apiFetch<VagaApi>(`/vagas?nome=${encodeURIComponent(nome)}`, {
            authenticated: false
        }))
    }

    async listarDaEmpresa(empresaId: number): Promise<Vaga[]> {
        const response = await apiFetch<unknown>(`/vagas?empresaId=${empresaId}`)
        if (!Array.isArray(response)) return []
        const vagas = response as VagaApi[]
        return Promise.all(vagas.map((vaga) => this.anexarCompetencias(vaga)))
    }

    async buscarDaEmpresa(empresaId: number, vagaId: number): Promise<Vaga> {
        const vaga = await apiFetch<VagaApi>(
            `/vagas?empresaId=${empresaId}&vagaId=${vagaId}`
        )
        return this.anexarCompetencias(vaga)
    }

    async buscarNomeDaEmpresa(empresaId: number, nome: string): Promise<Vaga> {
        const vaga = await apiFetch<VagaApi>(
            `/vagas?empresaId=${empresaId}&nome=${encodeURIComponent(nome)}`
        )
        return this.anexarCompetencias(vaga)
    }

    async salvarVaga(payload: VagaPayload, competencias: string[]): Promise<Vaga> {
        const catalogo = await this.competencias.listarTodas()
        const ids = this.idsDasCompetencias(competencias, catalogo)
        const vaga = await apiFetch<VagaApi>('/vagas', { method: 'POST', body: payload })
        await this.atualizarCompetencias(vaga.id, [], ids)
        return this.anexarCompetencias(vaga)
    }

    async atualizarVaga(
        id: number,
        payload: VagaPayload,
        competencias: string[]
    ): Promise<Vaga> {
        const catalogo = await this.competencias.listarTodas()
        const idsNovos = this.idsDasCompetencias(competencias, catalogo)
        const existentes = await this.competencias.listarDaVaga(id)
        const vaga = await apiFetch<VagaApi>(`/vagas/${id}`, {
            method: 'PUT',
            body: payload
        })
        await this.atualizarCompetencias(
            id,
            existentes.map((competencia) => competencia.id),
            idsNovos
        )
        return this.anexarCompetencias(vaga)
    }

    async excluirVaga(id: number): Promise<void> {
        await apiFetch(`/vagas/${id}`, { method: 'DELETE' })
    }

    private async anexarCompetencias(vaga: VagaApi): Promise<Vaga> {
        const fullVaga = mapearVaga(vaga)
        if (!vaga.competencias) {
            const competencias = await this.competencias.listarDaVaga(vaga.id)
            fullVaga.competencias = competencias.map((competencia) => competencia.nome)
        }
        return fullVaga
    }

    private idsDasCompetencias(nomes: string[], catalogo: Competencia[]): number[] {
        const porNome = new Map(catalogo.map((competencia) => [
            competencia.nome.trim().toLowerCase(),
            competencia.id
        ]))
        return [...new Set(nomes.map((nome) => {
            const id = porNome.get(nome.trim().toLowerCase())
            if (id === undefined) {
                throw new Error(`A competência "${nome}" não existe no catálogo da API.`)
            }
            return id
        }))]
    }

    private async atualizarCompetencias(
        vagaId: number,
        atuais: number[],
        desejadas: number[]
    ): Promise<void> {
        const removidas = atuais.filter((id) => !desejadas.includes(id))
        const adicionadas = desejadas.filter((id) => !atuais.includes(id))
        await Promise.all([
            ...removidas.map((id) => this.competencias.excluirDaVaga(vagaId, id)),
            ...adicionadas.map((id) => this.competencias.adicionarNaVaga(vagaId, id))
        ])
    }
}
