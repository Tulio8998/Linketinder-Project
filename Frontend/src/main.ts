import './styles/global.css'
import './styles/signIn.css'
import './styles/signUpCandidato.css'
import './styles/signUpEmpresa.css'
import { configSkills } from './components/cadastro'
import { signIn } from './pages/signIn'
import { signUpCandidato } from './pages/signUpCandidato'
import { signUpEmpresa } from './pages/signUpEmpresa'


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

// signUpPageEmpresa()
// configSkills()

signUpPageCandidato()
configSkills()