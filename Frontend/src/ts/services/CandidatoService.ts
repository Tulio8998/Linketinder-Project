import type { Candidato } from "../models/Candidato"

export class CandidatoService {
    private storage: Storage

    constructor (storage: Storage) {
        this.storage = storage
    }

    listarCandidatos(): Candidato[] {
        const dados = this.storage.getItem('candidatos_db')
        const candidatos: Candidato[] = dados ? JSON.parse(dados) : []
        let proximoId = candidatos.reduce((maior, candidato) => Math.max(maior, candidato.id ?? 0), 0)
        const normalizados = candidatos.map((candidato) => {
            if (candidato.id !== undefined) return candidato
            proximoId += 1
            return { ...candidato, id: proximoId }
        })
        if (normalizados.some((candidato, index) => candidato.id !== candidatos[index].id)) {
            this.storage.setItem('candidatos_db', JSON.stringify(normalizados))
        }
        return normalizados
    }

    salvarCandidato(candidato: Candidato): Candidato {
        const candidatos = this.listarCandidatos()
        const index = candidatos.findIndex(c => c.cpf === candidato.cpf)
        const salvo = { ...candidato, id: candidato.id ?? this.proximoId(candidatos) }
        if (index > -1) {
            candidatos[index] = salvo
        } else {
            candidatos.push(salvo)
        }
        this.storage.setItem('candidatos_db', JSON.stringify(candidatos))
        return salvo
    }

    candidatoAtual(): Candidato | null {
        const dados = this.storage.getItem('candidato_atual')
        return dados ? JSON.parse(dados) : null
    }

    excluirCandidato(cpf: string): void {
        const candidatos = this.listarCandidatos().filter(c => c.cpf !== cpf)
        this.storage.setItem('candidatos_db', JSON.stringify(candidatos))
    }

    private proximoId(candidatos: Candidato[]): number {
        return candidatos.reduce((maior, candidato) => Math.max(maior, candidato.id ?? 0), 0) + 1
    }

}