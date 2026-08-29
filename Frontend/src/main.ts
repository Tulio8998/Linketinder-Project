import './styles/global.css'
import './styles/signIn.css'
import './styles/signUpCandidato.css'
import './styles/signUpEmpresa.css'
import './styles/candidatoDash.css'
// import './styles/empresaDash.css'
import { configSkills, editCandPanel, findMatch, pointSkills, vagaPanel } from './components/candidato'
import { signIn } from './pages/signIn'
import { signUpCandidato } from './pages/signUpCandidato'
import { signUpEmpresa } from './pages/signUpEmpresa'
import { candidatoDash, viewVagaCand } from './pages/candidatoDash'
import { editEmpProfile, empresaDash } from './pages/empresaDash'
// import { addVagaPanel, cadidatoPanel, editEmpPanel, editVagaPanel, findCand, findMatch, findVaga, graphCand, pointSkills } from './components/empresa'

const app = document.querySelector<HTMLDivElement>('#app')!

function signInPage(): void {
    app.innerHTML = signIn()
    document.querySelector('.signUp-link')?.addEventListener('click', (e) => {
        e.preventDefault()
        signUpPageCandidato()
    })
}

function signUpPageCandidato(): void {
    app.innerHTML = signUpCandidato()
    // document.querySelector('.signIn-link')?.addEventListener('click', (e) => {
    //     e.preventDefault()
    //     signInPage()
    // })
}

function signUpPageEmpresa(): void {
    app.innerHTML = signUpEmpresa()
}

function candidatoDashPage(): void {
    app.innerHTML = candidatoDash()
}

function empresaDashPage(): void {
    app.innerHTML = empresaDash()
}

// signUpPageEmpresa()
// configSkills()

// signUpPageCandidato()
// configSkills()

candidatoDashPage()
editCandPanel()
vagaPanel()
pointSkills()
findMatch()

// empresaDashPage()
// editEmpPanel()
// addVagaPanel()
// cadidatoPanel()
// pointSkills()
// graphCand()
// findCand()
// findMatch()
// findVaga()