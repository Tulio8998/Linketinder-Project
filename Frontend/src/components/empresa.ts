import { addVaga, editEmpProfile, viewCandidato } from "../pages/empresaDash"

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


export function editCandPanel(): void {
    const editButton = document.querySelector('.btn-edit')
    editButton?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'edit-panel'
        panel.innerHTML = editEmpProfile()
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


export function addVagaPanel(): void {
    const addNav = document.querySelector('.add-vaga')
    addNav?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'add-panel'
        panel.innerHTML = addVaga()
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
            panel.innerHTML = viewCandidato(
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