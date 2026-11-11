export interface Recrutador {
    id: number
    empresaCnpj: string
    nome: string
    area: string
    descricao: string
    email: string
    cpf: string
    dataNascimento: string
    senha: string
    ativo: boolean
}

export interface CandidatoAnonimo {
    id: number
    descricao: string
    competencias: string[]
    formacoes: string[]
}

export interface AvaliacaoRecrutador {
    id: number
    candidatoId: number
    vagaId: number
    empresaCnpj: string
    recrutadorId: number
    curtiu: boolean
    createdAt: string
}

export interface MatchRecrutador {
    candidatoId: number
    vagaId: number
    nomeVaga: string
    descricaoVaga: string
    competenciasVaga: string[]
    nomeEmpresa: string
    descricaoEmpresa: string
    recrutadorNome: string
    recrutadorEmail: string
}
