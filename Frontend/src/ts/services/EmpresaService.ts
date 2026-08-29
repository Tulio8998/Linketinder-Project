import type { Empresa } from "../models/Empresa"

export const EmpresaService = {
    listarEmpresas(): Empresa[] {
        const dados = localStorage.getItem('empresas_db')
        return dados ? JSON.parse(dados) : []
    },
    salvarEmpresa(empresa: Empresa): void {
        const empresas = this.listarEmpresas()
        const index = empresas.findIndex(e => e.cpnj === empresa.cpnj)
        if (index > -1) {
            empresas[index] = empresa
        } else {
            empresas.push(empresa)
        }
        localStorage.setItem('empresas_db', JSON.stringify(empresas))
    },
    empresaAtual(): Empresa | null {
        const dados = localStorage.getItem('empresa_atual')
        return dados ? JSON.parse(dados) : null
    },
    excluirEmpresa(cpnj: string): void {
        const empresas = this.listarEmpresas().filter(e => e.cpnj !== cpnj)
        localStorage.setItem('empresas_db', JSON.stringify(empresas))
    }
}