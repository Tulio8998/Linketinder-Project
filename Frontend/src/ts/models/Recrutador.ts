export interface Recrutador {
    id: number
    nome: string
    area: string
    descricao: string
    email: string
    ativo: boolean
    empresa?: { cnpj: string }
}

export interface NovoRecrutador {
    nome: string
    area: string
    descricao: string
    email: string
    cpf: string
    dataNascimento: string
    senha: string
}

export interface CandidatoAnonimo {
    id: number
    descricao: string
    competencias: string[]
    formacoes: string[]
}

export interface VagaRecrutador {
    id: number
    nome: string
    descricao: string
    area: string
    competencias: string[]
    empresa: { cnpj: string }
}

export interface AvaliacaoRecrutador {
    autor: string | null
    vaga: string
    area: string
    descricaoCandidato: string
    competencias: string[]
    curtiu: boolean
}

export interface MatchRecrutador {
    nomeRecrutador: string | null
    emailRecrutador: string | null
    nomeEmpresa: string | null
    emailEmpresa: string | null
    cnpjEmpresa: string | null
    descricaoEmpresa: string | null
    paisEmpresa: string | null
    estadoEmpresa: string | null
    cidadeEmpresa: string | null
    cepEmpresa: string | null
    nomeVaga: string
}
