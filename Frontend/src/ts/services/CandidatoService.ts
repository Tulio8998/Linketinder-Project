import { apiFetch, getApiCredentials } from '../../api/api'
import type { Candidato } from '../models/Candidato'

export interface CandidatoPayload {
    nome: string
    email: string
    senha: string
    cpf: string
    dataNascimento: string
    pais: string
    estado: string
    cidade: string
    cep: string
    descricao: string
}

interface PerfilCandidatoApi {
    id: number
    nome: string
    email: string
    cpf: string
    dataNascimento: string
    descricao: string
    endereco: {
        pais: string
        estado: string
        cidade: string
        cep: string
    }
}

interface IdResponse {
    id: number
}

function mapearPerfil(perfil: PerfilCandidatoApi): Candidato {
    return {
        id: perfil.id,
        nome: perfil.nome,
        email: perfil.email,
        cpf: perfil.cpf,
        dataNascimento: perfil.dataNascimento,
        pais: perfil.endereco.pais,
        estado: perfil.endereco.estado,
        cidade: perfil.endereco.cidade,
        cep: perfil.endereco.cep,
        descricao: perfil.descricao,
        competencias: []
    }
}

export class CandidatoService {
    async cadastrar(payload: CandidatoPayload): Promise<number> {
        const resposta = await apiFetch<IdResponse>('/candidatos', {
            method: 'POST',
            authenticated: false,
            body: payload
        })
        return resposta.id
    }

    async obterPerfil(): Promise<Candidato> {
        const perfil = await apiFetch<PerfilCandidatoApi>('/candidatos')
        return mapearPerfil(perfil)
    }

    async candidatoAtual(): Promise<Candidato> {
        return this.obterPerfil()
    }

    async listarCandidatos(): Promise<Candidato[]> {
        return [await this.obterPerfil()]
    }

    async buscarPorId(id: number): Promise<Candidato> {
        const perfil = await apiFetch<PerfilCandidatoApi>(`/candidatos/${id}`)
        return mapearPerfil(perfil)
    }

    async buscarPorEmail(email: string): Promise<Candidato> {
        const perfil = await apiFetch<PerfilCandidatoApi>(
            `/candidatos?email=${encodeURIComponent(email)}`
        )
        return mapearPerfil(perfil)
    }

    async buscarPorCpf(cpf: string): Promise<Candidato> {
        const perfil = await apiFetch<PerfilCandidatoApi>(
            `/candidatos?cpf=${encodeURIComponent(cpf)}`
        )
        return mapearPerfil(perfil)
    }

    async atualizar(id: number, payload: Omit<CandidatoPayload, 'senha'>): Promise<void> {
        const currentCredentials = getApiCredentials()
        if (!currentCredentials) throw new Error('Faça login novamente para editar o perfil.')

        await apiFetch(`/candidatos/${id}`, {
            method: 'PUT',
            body: { ...payload, senha: currentCredentials.password }
        })
    }

    async excluir(id: number): Promise<void> {
        await apiFetch(`/candidatos/${id}`, { method: 'DELETE' })
    }
}
