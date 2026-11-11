import type { Empresa } from "./Empresa"

export interface Vaga {
    id?: number
    nome: string
    descricao: string
    area?: string
    pais: string
    estado: string
    cidade: string
    competencias: string[]
    empresa: Empresa
}