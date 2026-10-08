import { viewPanel } from '../pages/candidatoDash'

export function configSkills(container: HTMLElement | Document = document): void {
    const novaSkill = container.querySelector('#input-skill') as HTMLInputElement
    const skillsClass = container.querySelector('.info-professional .skills-class')

    novaSkill?.addEventListener('keydown', (event) => {
        if (event.key !== 'Enter') return
        event.preventDefault()
        const valorSkill = novaSkill.value.trim()
        if (!valorSkill) return

        const skillDiv = document.createElement('div')
        skillDiv.className = 'skill-div'
        skillDiv.innerHTML = `
            <p class="skill">${valorSkill}</p>
            <p class="remove-skill">x</p>
        `
        skillDiv.querySelector('.remove-skill')?.addEventListener('click', () => skillDiv.remove())
        skillsClass?.appendChild(skillDiv)
        novaSkill.value = ''
    })
}

export function renderizarSkillsExistentes(
    container: HTMLElement,
    skills: string[]
): void {
    const skillsClass = container.querySelector('.info-professional .skills-class')
    skills.forEach((skill) => {
        const skillDiv = document.createElement('div')
        skillDiv.className = 'skill-div'
        skillDiv.innerHTML = `
            <p class="skill">${skill}</p>
            <p class="remove-skill">x</p>
        `
        skillDiv.querySelector('.remove-skill')?.addEventListener('click', () => skillDiv.remove())
        skillsClass?.appendChild(skillDiv)
    })
}

export function coletarSkillsDoPainel(container: HTMLElement): string[] {
    return Array.from(container.querySelectorAll('.info-professional .skill'))
        .map((el) => el.textContent ?? '')
}

export function pointSkills(): void {
    document.querySelectorAll('.card').forEach((card) => {
        const pointers = card.querySelector('.pointers')
        const skills = card.querySelectorAll('.skill')
        const jaTemPointer = pointers?.querySelector('.pointer')
        if (skills.length > 6 && pointers && !jaTemPointer) {
            const pointer = document.createElement('p')
            pointer.className = 'pointer'
            pointer.textContent = '...'
            pointers.appendChild(pointer)
        }
    })
}

export function abrirPainelCard(): void {
    document.querySelectorAll('.card').forEach((card) => {
        card.addEventListener('click', (e) => {
            if ((e.target as HTMLElement).tagName === 'BUTTON') return

            const name = card.querySelector('.name')?.textContent?.trim() ?? ''
            const address = card.querySelector('.address')?.textContent?.trim() ?? ''
            const description = card.querySelector('.description')?.textContent?.trim() ?? ''
            const match = card.querySelector('.porcent-match')?.textContent?.trim() ?? ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(
                (el) => el.textContent?.trim() ?? ''
            )

            const panel = document.createElement('div')
            panel.className = 'card-panel'
            panel.innerHTML = viewPanel(name, address, description, match, skills)
            document.body.appendChild(panel)
            panel.querySelector('.close-card')?.addEventListener('click', (ev) => {
                ev.preventDefault()
                panel.remove()
            })
        })
    })
}