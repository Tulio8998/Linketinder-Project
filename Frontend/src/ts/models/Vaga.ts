import type { Empresa } from "./Empresa"

export interface Vaga {
    nome: string
    descricao: string
    pais: string
    estado: string
    cidade: string
    competencias: string[]
    empresa: Empresa
}