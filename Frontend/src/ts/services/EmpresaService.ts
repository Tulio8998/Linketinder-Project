import type { Empresa } from "../models/Empresa"

export const EmpresaService = {
    listarEmpresas(): Empresa[] {
        const dados = localStorage.getItem('empresas_db')
        return dados ? JSON.parse(dados) : []
    },
    salvarEmpresa(empresa: Empresa): void {
        const empresas = this.listarEmpresas()
        const index = empresas.findIndex(e => e.email === empresa.email)
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
    salvarEmpresaAtual(empresa: Empresa): void {
        localStorage.setItem('empresa_atual', JSON.stringify(empresa))
        this.salvarEmpresa(empresa)
    },
    excluirEmpresa(): void {
        localStorage.removeItem('empresa_atual')
    }
}