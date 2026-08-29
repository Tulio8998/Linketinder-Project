import type { Vaga } from "../models/Vaga"

export const VagaService = { 
    listarVagas(): Vaga[] {
        const vagasJSON = localStorage.getItem('vagas')
        return vagasJSON ? JSON.parse(vagasJSON) : []
    },
    excluirVaga(vaga: Vaga): void {
        const vagas = this.listarVagas()
        const novasVagas = vagas.filter(v => v.nome !== vaga.nome || v.descricao !== vaga.descricao)
        localStorage.setItem('vagas', JSON.stringify(novasVagas))
    },
    salvarVaga(vaga: Vaga): void {
        const vagas = this.listarVagas()
        vagas.push(vaga)
        localStorage.setItem('vagas', JSON.stringify(vagas))
    }
}