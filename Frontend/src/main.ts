import './styles/global.css'
import './styles/signIn.css'
import './styles/signUpCandidato.css'
import './styles/signUpEmpresa.css'
import './styles/candidatoDash.css'
import { configSkills, editPanel, vagaPanel } from './components/candidato'
import { signIn } from './pages/signIn'
import { signUpCandidato } from './pages/signUpCandidato'
import { signUpEmpresa } from './pages/signUpEmpresa'
import { candidatoDash } from './pages/candidatoDash'

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

function candidatoDashPage() {
    app.innerHTML = candidatoDash()
}

// signUpPageEmpresa()
// configSkills()

// signUpPageCandidato()
// configSkills()

candidatoDashPage()
editPanel()
vagaPanel()