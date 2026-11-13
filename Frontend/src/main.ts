import './styles/global.css'
import './styles/signIn.css'
import './styles/signUpCandidato.css'
import './styles/signUpEmpresa.css'
import './styles/candidatoDash.css'
import './styles/empresaDash.css'

import { signIn } from './pages/signIn'
import { signUpCandidato } from './pages/signUpCandidato'
import { signUpEmpresa } from './pages/signUpEmpresa'
import { candidatoDash } from './pages/candidatoDash'
import { empresaDash } from './pages/empresaDash'
import { recrutadorDash } from './pages/recrutadorDash'

import { setApiCredentials, clearApiCredentials, ApiError } from './api/api'
import { CandidatoService, type CandidatoPayload } from './ts/services/CandidatoService'
import { EmpresaService, type EmpresaPayload } from './ts/services/EmpresaService'
import { CompetenciaService } from './ts/services/CompetenciaService'
import { VagaService } from './ts/services/VagaService'

import type { Candidato } from './ts/models/Candidato'
import type { Empresa } from './ts/models/Empresa'
import { validarCep, validarCnpj, validarCpf, validarEmail } from './ts/util/validators'

import {
    configCandSkills,
    editCandPanel,
    findMatch as candFindMatch,
    findVaga as candFindVaga
} from './components/candidato'

import {
    configEmpSkills,
    addVagaPanel,
    editEmpPanel,
    findCand,
    findMatch as empFindMatch,
    findVaga as empFindVaga,
    graphCand
} from './components/empresa'

import {
    atualizaSidebarRecrutador,
    recrutadorFindVaga,
    recrutadorHistorico
} from './components/recrutador'

const candidatoService = new CandidatoService()
const empresaService = new EmpresaService()
const competenciaService = new CompetenciaService()
const vagaService = new VagaService()
const app = document.querySelector<HTMLDivElement>('#app')!

function mostrarErro(error: unknown): void {
    alert(error instanceof Error ? error.message : 'Não foi possível concluir a operação.')
}

function campo(form: ParentNode, nome: string): HTMLInputElement {
    const input = form.querySelector<HTMLInputElement>(`[data-field="${nome}"]`)
    if (!input) throw new Error(`Campo ${nome} não encontrado.`)
    return input
}

function skillsDoFormulario(form: ParentNode): string[] {
    return Array.from(form.querySelectorAll('.info-professional .skill'))
        .map((skill) => skill.textContent?.trim() ?? '')
        .filter(Boolean)
}

async function carregarCandidato(): Promise<Candidato> {
    const perfil = await candidatoService.obterPerfil()
    const competencias = await competenciaService.listarDoCandidato(perfil.id)
    perfil.competencias = competencias.map((competencia) => competencia.nome)
    return perfil
}

async function carregarEmpresa(): Promise<Empresa> {
    return empresaService.obterPerfil()
}

function signInPage(): void {
    clearApiCredentials()
    app.innerHTML = signIn()
    document.querySelector('.link-cand')?.addEventListener('click', (event) => {
        event.preventDefault()
        signUpPageCandidato()
    })
    document.querySelector('.link-emp')?.addEventListener('click', (event) => {
        event.preventDefault()
        signUpPageEmpresa()
    })

    const form = document.querySelector<HTMLFormElement>('.signIn-content form')
    form?.addEventListener('submit', (event) => {
        event.preventDefault()
        const email = form.querySelector<HTMLInputElement>('input[type="email"]')?.value.trim() ?? ''
        const password = form.querySelector<HTMLInputElement>('input[type="password"]')?.value ?? ''
        if (!email || !password) {
            alert('Informe e-mail e senha.')
            return
        }

        void (async () => {
            clearApiCredentials()
            setApiCredentials(email, password)

            try {
                await carregarCandidato()
                candidatoDashPage()
                return
            } catch (error) {
                if (!(error instanceof ApiError) || error.status !== 403) throw error
            }

            try {
                await carregarEmpresa()
                empresaDashPage()
                return
            } catch (error) {
                if (!(error instanceof ApiError) || error.status !== 403) throw error
            }

            try {
                await atualizaSidebarRecrutador()
                recrutadorDashPage()
                return
            } catch (error) {
                if (!(error instanceof ApiError) || error.status !== 403) throw error
            }

            throw new ApiError('Usuário não encontrado ou tipo de conta não reconhecido.', 403)
        })().catch((error: unknown) => {
            clearApiCredentials()
            mostrarErro(error)
        })
    })
}

function signUpPageCandidato(): void {
    app.innerHTML = signUpCandidato()
    void configCandSkills(document).catch(mostrarErro)
    const form = document.querySelector<HTMLFormElement>('.signUpCandidato-content form')
    document.querySelector('.cancel')?.addEventListener('click', (event) => {
        event.preventDefault()
        signInPage()
    })

    form?.addEventListener('submit', (event) => {
        event.preventDefault()
        void (async () => {
            const selects = form.querySelectorAll<HTMLSelectElement>('.info-location select')
            const payload: CandidatoPayload = {
                nome: campo(form, 'nome').value.trim(),
                cpf: campo(form, 'cpf').value.trim(),
                dataNascimento: campo(form, 'dataNascimento').value,
                email: campo(form, 'email').value.trim(),
                senha: campo(form, 'senha').value,
                pais: selects[0].value,
                estado: selects[1].value,
                cidade: campo(form, 'cidade').value.trim(),
                cep: campo(form, 'cep').value.trim(),
                descricao: form.querySelector<HTMLTextAreaElement>('textarea')?.value.trim() ?? ''
            }
            if (!payload.nome || !payload.dataNascimento || !payload.senha || !payload.cidade) {
                throw new Error('Preencha todos os campos obrigatórios.')
            }
            if (!validarEmail(payload.email)) throw new Error('E-mail inválido.')
            if (!validarCpf(payload.cpf)) throw new Error('CPF inválido.')
            if (!validarCep(payload.cep)) throw new Error('CEP inválido. Use o formato 00000-000.')

            const skills = skillsDoFormulario(form)
            const catalogo = await competenciaService.listarTodas()
            const skillsDisponiveis = new Set(
                catalogo.map((competencia) => competencia.nome.trim().toLowerCase())
            )
            const skillInvalida = skills.find(
                (skill) => !skillsDisponiveis.has(skill.trim().toLowerCase())
            )
            if (skillInvalida) {
                throw new Error(`A competência "${skillInvalida}" não existe no catálogo da API.`)
            }

            const id = await candidatoService.cadastrar(payload)
            setApiCredentials(payload.email, payload.senha)
            await competenciaService.substituirDoCandidato(id, skills)
            await carregarCandidato()
            candidatoDashPage()
        })().catch(mostrarErro)
    })
}

function signUpPageEmpresa(): void {
    app.innerHTML = signUpEmpresa()
    void configEmpSkills(document).catch(mostrarErro)
    const form = document.querySelector<HTMLFormElement>('.signUpEmpresa-content form')
    document.querySelector('.cancel')?.addEventListener('click', (event) => {
        event.preventDefault()
        signInPage()
    })

    form?.addEventListener('submit', (event) => {
        event.preventDefault()
        void (async () => {
            const selects = form.querySelectorAll<HTMLSelectElement>('.info-location select')
            const payload: EmpresaPayload = {
                nome: campo(form, 'nome').value.trim(),
                cnpj: campo(form, 'cnpj').value.trim(),
                email: campo(form, 'email').value.trim(),
                senha: campo(form, 'senha').value,
                pais: selects[0].value,
                estado: selects[1].value,
                cidade: campo(form, 'cidade').value.trim(),
                cep: campo(form, 'cep').value.trim(),
                descricao: form.querySelector<HTMLTextAreaElement>('textarea')?.value.trim() ?? ''
            }
            if (!payload.nome || !payload.senha || !payload.cidade) {
                throw new Error('Preencha todos os campos obrigatórios.')
            }
            if (!validarEmail(payload.email)) throw new Error('E-mail inválido.')
            if (!validarCnpj(payload.cnpj)) throw new Error('CNPJ inválido.')
            if (!validarCep(payload.cep)) throw new Error('CEP inválido. Use o formato 00000-000.')

            await empresaService.cadastrar(payload)
            setApiCredentials(payload.email, payload.senha)
            await carregarEmpresa()
            empresaDashPage()
        })().catch(mostrarErro)
    })
}

export async function atualizaSidebarCandidato(): Promise<void> {
    const candidato = await carregarCandidato()
    const root = document.querySelector<HTMLElement>('.candidato-profile')
    const name = root?.querySelector<HTMLElement>('.profile-content .name')
    const location = root?.querySelector<HTMLElement>('.profile-content div')
    const about = root?.querySelector<HTMLElement>('.about p:last-child')
    const skills = root?.querySelector<HTMLElement>('.skills div')
    if (name) name.textContent = candidato.nome
    if (location) {
        const state = document.createElement('p')
        state.textContent = candidato.estado
        const separator = document.createElement('p')
        separator.textContent = '-'
        const country = document.createElement('p')
        country.textContent = candidato.pais
        location.replaceChildren(state, separator, country)
    }
    if (about) about.textContent = candidato.descricao
    if (skills) {
        skills.replaceChildren(...candidato.competencias.map((name) => {
            const skill = document.createElement('p')
            skill.className = 'skill'
            skill.textContent = name
            return skill
        }))
    }
}

export async function atualizaSidebarEmpresa(): Promise<void> {
    const empresa = await carregarEmpresa()
    const vagas = await vagaService.listarDaEmpresa(empresa.id)
    const root = document.querySelector<HTMLElement>('.empresa-profile')
    const name = root?.querySelector<HTMLElement>('.profile-content .name')
    const location = root?.querySelector<HTMLElement>('.profile-content div')
    const about = root?.querySelector<HTMLElement>('.about p:last-child')
    const skills = root?.querySelector<HTMLElement>('.skills div')
    if (name) name.textContent = empresa.nome
    if (location) {
        const state = document.createElement('p')
        state.textContent = empresa.estado
        const separator = document.createElement('p')
        separator.textContent = '-'
        const country = document.createElement('p')
        country.textContent = empresa.pais
        location.replaceChildren(state, separator, country)
    }
    if (about) about.textContent = empresa.descricao
    if (skills) {
        const competencias = [...new Set(vagas.flatMap((vaga) => vaga.competencias))]
        skills.replaceChildren(...competencias.map((name) => {
            const skill = document.createElement('p')
            skill.className = 'skill'
            skill.textContent = name
            return skill
        }))
    }
}

function sair(): void {
    clearApiCredentials()
    signInPage()
}

function candidatoDashPage(): void {
    app.innerHTML = candidatoDash()
    editCandPanel()
    candFindMatch()
    candFindVaga()
    void atualizaSidebarCandidato().catch(mostrarErro)
    document.querySelector<HTMLElement>('.find-vaga')?.click()
    document.querySelector('.btn-out')?.addEventListener('click', sair)
}

function empresaDashPage(): void {
    app.innerHTML = empresaDash()
    editEmpPanel()
    addVagaPanel()
    graphCand()
    findCand()
    empFindMatch()
    empFindVaga()
    void atualizaSidebarEmpresa().catch(mostrarErro)
    document.querySelector<HTMLElement>('.find-cand')?.click()
    document.querySelector('.btn-out')?.addEventListener('click', sair)
}

function recrutadorDashPage(): void {
    app.innerHTML = recrutadorDash()
    recrutadorFindVaga()
    recrutadorHistorico()
    void atualizaSidebarRecrutador().catch(mostrarErro)
    document.querySelector<HTMLElement>('.find-vaga')?.click()
    document.querySelector('.btn-out')?.addEventListener('click', sair)
}

signInPage()
