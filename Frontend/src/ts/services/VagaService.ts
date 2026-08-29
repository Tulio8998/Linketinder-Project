import type { Vaga } from "../models/Vaga"

export const VagaService = {
    listarVagas(): Vaga[] {
        const vagasJSON = localStorage.getItem('vagas')
        return vagasJSON ? JSON.parse(vagasJSON) : []
    },
    excluirVaga(index: number): void {
        const vagas = this.listarVagas()
        vagas.splice(index, 1)
        localStorage.setItem('vagas', JSON.stringify(vagas))
    },
    salvarVaga(vaga: Vaga): void {
        const vagas = this.listarVagas()
        vagas.push(vaga)
        localStorage.setItem('vagas', JSON.stringify(vagas))
    },
    atualizarVaga(index: number, vagaAtualizada: Vaga): void {
        const vagas = this.listarVagas()
        vagas[index] = vagaAtualizada
        localStorage.setItem('vagas', JSON.stringify(vagas))
    }
}