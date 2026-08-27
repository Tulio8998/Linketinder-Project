import './styles/global.css'
import './styles/signIn.css'
import './styles/signUp.css'

import { signIn } from './pages/signIn'
import { signUp } from './pages/signUp'

const app = document.querySelector<HTMLDivElement>('#app')!

function signInPage(): void {
    app.innerHTML = signIn()
    document.querySelector('.signUp-link')?.addEventListener('click', (e) => {
        e.preventDefault()
        renderSignUpPage()
    })
}

function renderSignUpPage(): void {
    app.innerHTML = signUp()
    document.querySelector('.signIn-link')?.addEventListener('click', (e) => {
        e.preventDefault()
        signInPage()
    })
}

signInPage()