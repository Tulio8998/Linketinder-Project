import type { Candidato } from "../models/Candidato"

export const CandidatoService = {
    listarCandidatos(): Candidato[] {
        const dados = localStorage.getItem('candidatos_db')
        return dados ? JSON.parse(dados) : []
    },
    salvarCandidato(candidato: Candidato): void {
        const candidatos = this.listarCandidatos()
        candidatos.push(candidato)
        localStorage.setItem('candidatos_db', JSON.stringify(candidatos))
    },
    candidatoAtual(): Candidato | null {
        const dados = localStorage.getItem('candidato_atual')
        return dados ? JSON.parse(dados) : null
    }
}