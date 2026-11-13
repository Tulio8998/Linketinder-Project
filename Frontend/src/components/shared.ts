import { viewPanel } from '../pages/candidatoDash'
import { CompetenciaService } from '../ts/services/CompetenciaService'

const competenciaService = new CompetenciaService()
let nextDataListId = 0

export async function configSkills(container: HTMLElement | Document = document): Promise<void> {
    const novaSkill = container.querySelector('#input-skill') as HTMLInputElement
    const skillsClass = container.querySelector('.info-professional .skills-class')
    if (!novaSkill || !skillsClass) return

    novaSkill.disabled = true
    const formButtons = Array.from(container.querySelectorAll<HTMLButtonElement>(
        'form button[type="submit"]'
    ))
    formButtons.forEach((button) => { button.disabled = true })
    let catalogo
    try {
        catalogo = await competenciaService.listarTodas()
    } catch (error) {
        formButtons.forEach((button) => { button.disabled = false })
        throw error
    }

    const datalist = document.createElement('datalist')
    datalist.id = `competencias-catalogo-${nextDataListId++}`
    catalogo.forEach((competencia) => {
        const option = document.createElement('option')
        option.value = competencia.nome
        datalist.appendChild(option)
    })
    novaSkill.setAttribute('list', datalist.id)
    novaSkill.insertAdjacentElement('afterend', datalist)
    novaSkill.disabled = false
    formButtons.forEach((button) => { button.disabled = false })

    novaSkill?.addEventListener('keydown', (event) => {
        if (event.key !== 'Enter') return
        event.preventDefault()
        const enteredSkill = novaSkill.value.trim()
        if (!enteredSkill) return
        const competencia = catalogo.find(
            (item) => item.nome.trim().toLowerCase() === enteredSkill.toLowerCase()
        )
        if (!competencia) {
            alert('Selecione uma competência existente no catálogo.')
            novaSkill.value = ''
            return
        }
        const alreadySelected = Array.from(skillsClass.querySelectorAll('.skill'))
            .some((skill) => skill.textContent?.trim().toLowerCase() === competencia.nome.toLowerCase())
        if (alreadySelected) {
            novaSkill.value = ''
            return
        }

        const skillDiv = document.createElement('div')
        skillDiv.className = 'skill-div'
        const skill = document.createElement('p')
        skill.className = 'skill'
        skill.textContent = competencia.nome
        const remove = document.createElement('p')
        remove.className = 'remove-skill'
        remove.textContent = 'x'
        skillDiv.append(skill, remove)
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
        const skillText = document.createElement('p')
        skillText.className = 'skill'
        skillText.textContent = skill
        const remove = document.createElement('p')
        remove.className = 'remove-skill'
        remove.textContent = 'x'
        skillDiv.append(skillText, remove)
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