import type { Candidato } from "../models/Candidato"

export class CandidatoService {
    private storage: Storage

    constructor (storage: Storage) {
        this.storage = storage
    }

    listarCandidatos(): Candidato[] {
        const dados = this.storage.getItem('candidatos_db')
        return dados ? JSON.parse(dados) : []
    }

    salvarCandidato(candidato: Candidato): void {
        const candidatos = this.listarCandidatos()
        const index = candidatos.findIndex(c => c.cpf === candidato.cpf)
        if (index > -1) {
            candidatos[index] = candidato
        } else {
            candidatos.push(candidato)
        }
        this.storage.setItem('candidatos_db', JSON.stringify(candidatos))
    }

    candidatoAtual(): Candidato | null {
        const dados = this.storage.getItem('candidato_atual')
        return dados ? JSON.parse(dados) : null
    }

    excluirCandidato(cpf: string): void {
        const candidatos = this.listarCandidatos().filter(c => c.cpf !== cpf)
        this.storage.setItem('candidatos_db', JSON.stringify(candidatos))
    }

}