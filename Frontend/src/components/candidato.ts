import { editProfile, viewMatch, viewPanel, viewVagaCand } from "../pages/candidatoDash"

export function configSkills(): void {
    const novaSkill = document.getElementById('input-skill') as HTMLInputElement
    const skillsClass = document.querySelector('.skills-class')

    novaSkill?.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault()

            const valorSkill = novaSkill.value.trim()

            if (valorSkill === '') {
                return
            }

            const skillDiv = document.createElement('div')
            skillDiv.className = 'skill-div'

            const skillName = document.createElement('p')
            skillName.className = 'skill'
            skillName.textContent = valorSkill

            const removeSkill = document.createElement('p')
            removeSkill.className = 'remove-skill'
            removeSkill.textContent = 'x'

            removeSkill.addEventListener('click', () => {
                skillDiv.remove()
            })

            skillDiv.appendChild(skillName)
            skillDiv.appendChild(removeSkill)

            skillsClass?.appendChild(skillDiv)

            novaSkill.value = ''
        }
    })
}

export function cadidatoPanel(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        card.addEventListener('click', () => {
            const name = card.querySelector('.name')?.textContent ?? ''
            const address = card.querySelector('.address')?.textContent ?? ''
            const description = card.querySelector('.description')?.textContent ?? ''
            const match = card.querySelector('.porcent-match')?.textContent ?? ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(e => e.textContent ?? '')
            const panel = document.createElement('div')
            panel.className = 'card-panel'
            panel.innerHTML = viewPanel(
                name,
                address,
                description,
                match,
                skills,
            )

            document.body.appendChild(panel)
            const closeButton = panel.querySelector('.close-card')
            closeButton?.addEventListener('click', (e) => {
                e.preventDefault()
                panel.remove()
            })
        })
    })
}

export function editCandPanel(): void {
    const editButton = document.querySelector('.btn-edit')
    editButton?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'edit-panel'
        panel.innerHTML = editProfile()
        document.body.appendChild(panel)
        configSkills()

        const closeButton = panel.querySelector('.cancel')
        closeButton?.addEventListener('click', (e) => {
            e.preventDefault()
            panel.remove()
        })

        const form = panel.querySelector('form')
        form?.addEventListener('submit', (e) => {
            e.preventDefault()
            panel.remove()
        })
    })
}


export function vagaPanel(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        card.addEventListener('click', () => {
            const name = card.querySelector('.name')?.textContent ?? ''
            const address = card.querySelector('.address')?.textContent ?? ''
            const description = card.querySelector('.description')?.textContent ?? ''
            const match = card.querySelector('.porcent-match')?.textContent ?? ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(e => e.textContent ?? '')
            const panel = document.createElement('div')
            panel.className = 'card-panel'
            panel.innerHTML = viewVagaCand(
                name,
                address,
                description,
                match,
                skills,
            )

            document.body.appendChild(panel)
            const closeButton = panel.querySelector('.close-card')
            closeButton?.addEventListener('click', (e) => {
                e.preventDefault()
                panel.remove()
            })
        })
    })
}

export function pointSkills(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        const pointers = card.querySelector('.pointers')
        const skills = card.querySelectorAll('.skill')

        if (skills.length > 6 && pointers) {
            const pointer = document.createElement('p')
            pointer.className = 'pointer'
            pointer.textContent = '...'

            pointers.appendChild(pointer)
        }
    })
}

let candidatosCards: string = ''

export function findMatch(): void {
    const matchCandBtn = document.querySelector('.match-cand') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    candidatosCards = dash.innerHTML

    matchCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        dash.innerHTML = viewMatch() 
        cadidatoPanel()
        pointSkills()
    })
}