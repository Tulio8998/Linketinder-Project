import type { Vaga } from "../models/Vaga"

export class VagaService {
    private storage: Storage

    constructor (storage: Storage) {
        this.storage = storage
    }


    listarVagas(): Vaga[] {
        const vagasJSON = this.storage.getItem('vagas')
        const vagas: Vaga[] = vagasJSON ? JSON.parse(vagasJSON) : []
        let proximoId = vagas.reduce((maior, vaga) => Math.max(maior, vaga.id ?? 0), 0)
        const normalizadas = vagas.map((vaga) => {
            if (vaga.id !== undefined) return vaga
            proximoId += 1
            return { ...vaga, id: proximoId }
        })
        if (normalizadas.some((vaga, index) => vaga.id !== vagas[index].id)) {
            this.storage.setItem('vagas', JSON.stringify(normalizadas))
        }
        return normalizadas
    }

    excluirVaga(index: number): void {
        const vagas = this.listarVagas()
        vagas.splice(index, 1)
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }
    
    salvarVaga(vaga: Vaga): void {
        const vagas = this.listarVagas()
        const id = vaga.id ?? vagas.reduce((maior, atual) => Math.max(maior, atual.id ?? 0), 0) + 1
        vagas.push({ ...vaga, id })
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }

    atualizarVaga(index: number, vagaAtualizada: Vaga): void {
        const vagas = this.listarVagas()
        vagas[index] = vagaAtualizada
        this.storage.setItem('vagas', JSON.stringify(vagas))
    }
}