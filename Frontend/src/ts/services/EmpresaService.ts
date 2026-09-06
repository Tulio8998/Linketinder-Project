import type { Empresa } from "../models/Empresa"

export class EmpresaService {
    private storage: Storage

    constructor(storage: Storage) {
        this.storage = storage
    }
    
    listarEmpresas(): Empresa[] {
        const dados = this.storage.getItem('empresas_db')
        return dados ? JSON.parse(dados) : []
    }
    salvarEmpresa(empresa: Empresa): void {
        const empresas = this.listarEmpresas()
        const index = empresas.findIndex(e => e.cpnj === empresa.cpnj)
        if (index > -1) {
            empresas[index] = empresa
        } else {
            empresas.push(empresa)
        }
        this.storage.setItem('empresas_db', JSON.stringify(empresas))
    }
    empresaAtual(): Empresa | null {
        const dados = this.storage.getItem('empresa_atual')
        return dados ? JSON.parse(dados) : null
    }

    excluirEmpresa(cpnj: string): void {
        const empresas = this.listarEmpresas().filter(e => e.cpnj !== cpnj)
        this.storage.setItem('empresas_db', JSON.stringify(empresas))
    }
}