import type { Pessoa } from "./Pessoa"

export interface Candidato extends Pessoa {
    cpf: string
    dataNascimento: string
}