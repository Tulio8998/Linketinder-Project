import type { Pessoa } from "./Pessoa"

export interface Empresa extends Pessoa {
    cnpj: string
}