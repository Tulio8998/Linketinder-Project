import type { Candidato } from "../models/Candidato"

export const CandidatoService = {
    listarCandidatos(): Candidato[] {
        const dados = localStorage.getItem('candidatos_db')
        return dados ? JSON.parse(dados) : []
    },
    salvarCandidato(candidato: Candidato): void {
        const candidatos = this.listarCandidatos()
        const index = candidatos.findIndex(c => c.cpf === candidato.cpf)
        if (index > -1) {
            candidatos[index] = candidato
        } else {
            candidatos.push(candidato)
        }
        localStorage.setItem('candidatos_db', JSON.stringify(candidatos))
    },
    candidatoAtual(): Candidato | null {
        const dados = localStorage.getItem('candidato_atual')
        return dados ? JSON.parse(dados) : null
    },
    excluirCandidato(cpf: string): void {
        const candidatos = this.listarCandidatos().filter(c => c.cpf !== cpf)
        localStorage.setItem('candidatos_db', JSON.stringify(candidatos))
    },
}