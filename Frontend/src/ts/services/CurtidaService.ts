import type { Candidato } from "../models/Candidato"
import type { Curtida, CurtidaCandidato, CurtidaEmpresa } from "../models/Curtida"
import type { Empresa } from "../models/Empresa"
import type { Vaga } from "../models/Vaga"

export class CurtidaService {
    private storage: Storage

    constructor(storage: Storage) {
        this.storage = storage
    }

    listarMatchs(): CurtidaEmpresa[] {
        const dados = this.storage.getItem('matchs_db')
        return dados ? JSON.parse(dados) : []
    }

    listarCurtidas(): CurtidaCandidato[] {
        const dados = this.storage.getItem('curtidas_db')
        return dados ? JSON.parse(dados) : []
    }

    listarCurtidasEmpresa(vaga: Vaga) {
        const dados = this.listarCurtidas()
        return dados.filter(d => 
            d.like === true &&
            d.vaga.empresa.cpnj === vaga.empresa.cpnj)
    }

    salvarCurtidaEmpresa(empresa: Empresa, curtida: boolean, curtidaCandidato: CurtidaCandidato): void {
        const matchs = this.listarMatchs()
        
        const match: CurtidaEmpresa = {
            empresa: empresa,
            like: curtida,
            candidato: curtidaCandidato.candidato,
            vaga: curtidaCandidato.vaga
        }

        matchs.push(match)
        this.storage.setItem('matchs_db', JSON.stringify(matchs))
    }

    salvarCurtidaCandidato(candidato: Candidato, curtida: Curtida): void {
        const curtidas = this.listarCurtidas()

        const match: CurtidaCandidato = {
            candidato: candidato,
            like: curtida.like,
            vaga: curtida.vaga
        }

        curtidas.push(match)
        this.storage.setItem('curtidas_db', JSON.stringify(curtidas))
    }
    
    calcularAfinidade(candidato: Candidato, vaga: Vaga): number {
    if (vaga.competencias.length === 0) {
        return 0
    }

    const compIguais = vaga.competencias.filter(c => candidato.competencias.includes(c))
    const qntCompVaga = vaga.competencias.length
    const qntCompTotal = compIguais.length

    return (qntCompTotal * 100) / qntCompVaga
}
}