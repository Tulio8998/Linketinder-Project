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

import { CandidatoService } from './ts/services/CandidatoService'
import { EmpresaService } from './ts/services/EmpresaService'
import type { Candidato } from './ts/models/Candidato'
import type { Empresa } from './ts/models/Empresa'

import { configSkills as configCandSkills, cadidatoPanel as candCardPanel, editCandPanel, vagaPanel, pointSkills as candPointSkills, findMatch as candFindMatch, findVaga as candFindVaga } from './components/candidato'
import { configSkills as configEmpSkills, addVagaPanel, cadidatoPanel as empCardPanel, editEmpPanel, findCand, findMatch as empFindMatch, findVaga as empFindVaga, graphCand, pointSkills as empPointSkills } from './components/empresa'

const app = document.querySelector<HTMLDivElement>('#app')!

function signInPage(): void {
    app.innerHTML = signIn()
    document.querySelector('.link-cand')?.addEventListener('click', (e) => {
        e.preventDefault(); signUpPageCandidato()
    })
    document.querySelector('.link-emp')?.addEventListener('click', (e) => {
        e.preventDefault(); signUpPageEmpresa()
    })

    const form = document.querySelector('form')
    form?.addEventListener('submit', (e) => {
        e.preventDefault()
        const emailInput = form.querySelector<HTMLInputElement>('.input-in[type="email"]')
        const email = emailInput?.value.trim()

        if (!email) {
            alert('Informe um email.')
            return
        }

        const candidato = CandidatoService.listarCandidatos().find(c => c.email === email)
        if (candidato) {
            localStorage.setItem('candidato_atual', JSON.stringify(candidato))
            candidatoDashPage()
            return
        }

        const empresa = EmpresaService.listarEmpresas().find(emp => emp.email === email)
        if (empresa) {
            localStorage.setItem('empresa_atual', JSON.stringify(empresa))
            empresaDashPage()
            return
        }
        alert('Usuário nao encontrado! Verifique o e-mail ou cadastre-se.')
    })
}

function signUpPageCandidato(): void {
    app.innerHTML = signUpCandidato()
    configCandSkills(document)

    const form = document.querySelector('form')
    document.querySelector('.cancel')?.addEventListener('click', (e) => {
        e.preventDefault(); signInPage()
    })

    form?.addEventListener('submit', (e) => {
        e.preventDefault()
        const inputs = form.querySelectorAll<HTMLInputElement>('.input-data')
        const selects = form.querySelectorAll<HTMLSelectElement>('select')
        const textarea = form.querySelector('textarea')
        const skillsElements = form.querySelectorAll('.info-professional .skill')

        const nome = inputs[0].value.trim()
        const cpf = inputs[1].value.trim()
        const idade = Number(inputs[2].value)
        const email = inputs[3].value.trim()
        const cep = inputs[5].value.trim()

        if (!nome || !cpf || !email) {
            alert("Preencha os campos obrigatórios!")
            return
        }

        const emailExisteCand = CandidatoService.listarCandidatos().some(c => c.email === email)
        const emailExisteEmp = EmpresaService.listarEmpresas().some(e => e.email === email)
        if (emailExisteCand || emailExisteEmp) {
            alert("Este E-mail já está em uso!")
            return
        }

        const cpfExiste = CandidatoService.listarCandidatos().some(c => c.cpf === cpf)
        if (cpfExiste) {
            alert("Este CPF já está cadastrado!")
            return
        }

        const novoCandidato: Candidato = {
            nome,
            cpf,
            idade,
            email, 
            cep,
            pais: selects[0].value,
            estado: selects[1].value,
            descricao: textarea?.value || '',
            competencias: Array.from(skillsElements).map(s => s.textContent || '')
        }

        CandidatoService.salvarCandidato(novoCandidato)
        localStorage.setItem('candidato_atual', JSON.stringify(novoCandidato))
        candidatoDashPage()
    })
}

function signUpPageEmpresa(): void {
    app.innerHTML = signUpEmpresa()
    configEmpSkills(document) 

    const form = document.querySelector('form')
    document.querySelector('.cancel')?.addEventListener('click', (e) => {
        e.preventDefault(); signInPage()
    })

    form?.addEventListener('submit', (e) => {
        e.preventDefault()
        const inputs = form.querySelectorAll<HTMLInputElement>('.input-data')
        const selects = form.querySelectorAll<HTMLSelectElement>('select')
        const textarea = form.querySelector('textarea')
        const skillsElements = form.querySelectorAll('.info-professional .skill')

        const nome = inputs[0].value.trim()
        const cpnj = inputs[1].value.trim()
        const email = inputs[2].value.trim()
        const cep = inputs[4].value.trim()

        if (!nome || !cpnj || !email) {
            alert("Preencha os campos obrigatórios!")
            return
        }

        const emailExisteCand = CandidatoService.listarCandidatos().some(c => c.email === email)
        const emailExisteEmp = EmpresaService.listarEmpresas().some(e => e.email === email)
        if (emailExisteCand || emailExisteEmp) {
            alert("Este E-mail já está em uso!")
            return
        }

        const cnpjExiste = EmpresaService.listarEmpresas().some(e => e.cpnj === cpnj)
        if (cnpjExiste) {
            alert("Este CNPJ já está cadastrado!")
            return
        }

        const novaEmpresa: Empresa = {
            nome,
            cpnj, 
            email, 
            cep, 
            pais: selects[0].value,
            estado: selects[1].value,
            descricao: textarea?.value || '',
            competencias: Array.from(skillsElements).map(s => s.textContent || '')
        }

        EmpresaService.salvarEmpresa(novaEmpresa)
        localStorage.setItem('empresa_atual', JSON.stringify(novaEmpresa))
        empresaDashPage()
    })
}

export function atualizaSidebarCandidato(): void {
    const candLogado = CandidatoService.candidatoAtual()
    if(candLogado) {
        const nameEl = document.querySelector('.candidato-profile .name')
        const locEl = document.querySelector('.candidato-profile .profile-content div')
        const aboutEl = document.querySelector('.candidato-profile .about p:last-child')
        const skillsEl = document.querySelector('.candidato-profile .skills div')

        if(nameEl) nameEl.textContent = candLogado.nome
        if(locEl) locEl.innerHTML = `<p>${candLogado.estado}</p><p>-</p><p>${candLogado.pais}</p>`
        if(aboutEl) aboutEl.textContent = candLogado.descricao
        if(skillsEl) skillsEl.innerHTML = candLogado.competencias.map(s => `<p class="skill">${s}</p>`).join('')
    }
}

export function atualizaSidebarEmpresa(): void {
    const empresaLogada = EmpresaService.empresaAtual()
    if(empresaLogada) {
        const nameEl = document.querySelector('.empresa-profile .name')
        const locEl = document.querySelector('.empresa-profile .profile-content div')
        const aboutEl = document.querySelector('.empresa-profile .about p:last-child')
        const skillsEl = document.querySelector('.empresa-profile .skills div')

        if(nameEl) nameEl.textContent = empresaLogada.nome
        if(locEl) locEl.innerHTML = `<p>${empresaLogada.estado}</p><p>-</p><p>${empresaLogada.pais}</p>`
        if(aboutEl) aboutEl.textContent = empresaLogada.descricao
        if(skillsEl) skillsEl.innerHTML = empresaLogada.competencias.map(s => `<p class="skill">${s}</p>`).join('')
    }
}

function candidatoDashPage(): void {
    app.innerHTML = candidatoDash()
    editCandPanel()
    vagaPanel()
    candPointSkills()
    candFindMatch()
    candFindVaga()

    atualizaSidebarCandidato()
    document.querySelector<HTMLElement>('.find-vaga')?.click()
    
    document.querySelector('.btn-out')?.addEventListener('click', () => {
        localStorage.removeItem('candidato_atual')
        signInPage()
    })
}

function empresaDashPage(): void {
    app.innerHTML = empresaDash()
    editEmpPanel()
    addVagaPanel()
    empCardPanel()
    empPointSkills()
    graphCand()
    findCand()
    empFindMatch()
    empFindVaga()

    atualizaSidebarEmpresa()
    document.querySelector<HTMLElement>('.find-cand')?.click()

    document.querySelector('.btn-out')?.addEventListener('click', () => {
        localStorage.removeItem('empresa_atual')
        signInPage()
    })
}

signInPage()