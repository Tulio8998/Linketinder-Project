import type { Vaga } from "../models/Vaga"

export class VagaService {
    private storage: Storage

    constructor (storage: Storage) {
        this.storage = storage
    }


    listarVagas(): Vaga[] {
        const vagasJSON = this.storage.getItem('vagas')
        return vagasJSON ? JSON.parse(vagasJSON) : []
    }

    excluirVaga(index: number): void {
        const vagas = this.listarVagas()
        vagas.splice(index, 1)
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }
    
    salvarVaga(vaga: Vaga): void {
        const vagas = this.listarVagas()
        vagas.push(vaga)
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }

    atualizarVaga(index: number, vagaAtualizada: Vaga): void {
        const vagas = this.listarVagas()
        vagas[index] = vagaAtualizada
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }
}