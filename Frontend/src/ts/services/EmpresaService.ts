import { apiFetch, getApiCredentials } from '../../api/api'
import type { Empresa } from '../models/Empresa'

export interface EmpresaPayload {
    nome: string
    email: string
    senha: string
    cnpj: string
    pais: string
    estado: string
    cidade: string
    cep: string
    descricao: string
}

interface PerfilEmpresaApi {
    id: number
    nome: string
    email: string
    cnpj: string
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

function mapearPerfil(perfil: PerfilEmpresaApi): Empresa {
    return {
        id: perfil.id,
        nome: perfil.nome,
        email: perfil.email,
        cnpj: perfil.cnpj,
        pais: perfil.endereco.pais,
        estado: perfil.endereco.estado,
        cidade: perfil.endereco.cidade,
        cep: perfil.endereco.cep,
        descricao: perfil.descricao,
        competencias: []
    }
}

export class EmpresaService {
    async cadastrar(payload: EmpresaPayload): Promise<number> {
        const resposta = await apiFetch<IdResponse>('/empresas', {
            method: 'POST',
            authenticated: false,
            body: payload
        })
        return resposta.id
    }

    async obterPerfil(): Promise<Empresa> {
        const perfil = await apiFetch<PerfilEmpresaApi>('/empresas')
        return mapearPerfil(perfil)
    }

    async empresaAtual(): Promise<Empresa> {
        return this.obterPerfil()
    }

    async listarEmpresas(): Promise<Empresa[]> {
        return [await this.obterPerfil()]
    }

    async buscarPorId(id: number): Promise<Empresa> {
        const perfil = await apiFetch<PerfilEmpresaApi>(`/empresas/${id}`)
        return mapearPerfil(perfil)
    }

    async buscarPorCnpj(cnpj: string): Promise<Empresa> {
        const perfil = await apiFetch<PerfilEmpresaApi>(
            `/empresas?cnpj=${encodeURIComponent(cnpj)}`
        )
        return mapearPerfil(perfil)
    }

    async buscarPorEmail(email: string): Promise<Empresa> {
        const perfil = await apiFetch<PerfilEmpresaApi>(
            `/empresas?email=${encodeURIComponent(email)}`
        )
        return mapearPerfil(perfil)
    }

    async atualizar(id: number, payload: Omit<EmpresaPayload, 'senha'>): Promise<void> {
        const currentCredentials = getApiCredentials()
        if (!currentCredentials) throw new Error('Faça login novamente para editar o perfil.')

        await apiFetch(`/empresas/${id}`, {
            method: 'PUT',
            body: { ...payload, senha: currentCredentials.password }
        })
    }

    async excluir(id: number): Promise<void> {
        await apiFetch(`/empresas/${id}`, { method: 'DELETE' })
    }
}
