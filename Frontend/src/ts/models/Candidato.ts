import type { Pessoa } from "./Pessoa"

export interface Candidato extends Pessoa {
    id?: number
    cpf: string
    idade: number
    formacoes?: string[]
}