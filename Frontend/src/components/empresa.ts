import ApexCharts from 'apexcharts'
import { addVaga, editEmpProfile, editVaga, viewCandidatoPanel, viewMatch, viewVagaEmp } from "../pages/empresaDash"

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


export function editEmpPanel(): void {
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

export function editVagaPanel(): void {
    const cardVaga = document.querySelectorAll('.card')
    cardVaga.forEach((card) => {
        card.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'edit-panel'
        panel.innerHTML = editVaga()
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
            panel.innerHTML = viewCandidatoPanel(
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

const pessoaMock = {
    competencia: []
}

const p1 = {
    ...pessoaMock,
    competencia: ['Java', 'Python', 'SQL', 'Groovy', 'JavaScripy']
}

const p2 = {
    ...pessoaMock,
    competencia: ['Java', 'Python', 'SQL', 'TypeScripy', 'JavaScripy']
}

const p3 = {
    ...pessoaMock,
    competencia: ['Java', 'Python', 'C#', 'Groovy', 'JavaScripy']
}

const p4 = {
    ...pessoaMock,
    competencia: ['C#', 'C++', 'SQL', 'Java', 'C++']
}

const p5 = {
    ...pessoaMock,
    competencia: ['Java', 'SQL', 'Groovy', 'Java']
}

const p6 = {
    ...pessoaMock,
    competencia: ['Java', 'SQL', 'Groovy', 'Java']
}

const p7 = {
    ...pessoaMock,
    competencia: ['Java', 'SQL', 'Groovy', 'Java']
}
const pessoas = [p1, p2, p3, p4, p5, p6, p7]


export function graphCand(): void {
    const competencias = new Map<string, number>()
    pessoas.forEach((pessoa) => {
        const competenciasUn = new Set(pessoa.competencia)
        competenciasUn.forEach((competencia) => {
            competencias.set(competencia,(competencias.get(competencia) ?? 0) + 1
            )
        })
    })

    const categorias = Array.from(competencias.keys())
    const valores = Array.from(competencias.values())
    const options: ApexCharts.ApexOptions = {
        chart: {
            type: 'bar',
            height: 400,
            toolbar: {
                show: false
            }
        },
        colors: ['#22C55E'],
        grid: {
            padding: {
                bottom: 20
            }
        },
        series: [
            {
                name: 'Quantidade de candidatos',
                data: valores
            }
        ],
        xaxis: {
            categories: categorias,
            title: {
                text: 'Competências',
            }
        },
        yaxis: {
            min: 0,
            tickAmount: 5,
            title: {
                text: 'Número de candidatos'
            }
        },
        tooltip: {
            y: {
                formatter: (value) => `${value} candidatos`
            }
        }
    }
    const graph = document.querySelector('#graph') as HTMLElement
    if (graph) {
        const chart = new ApexCharts(graph, options)
        chart.render()
    }
}

let candidatosCards: string = ''

export function findCand(): void {
    const findCandBtn = document.querySelector('.find-cand')
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    candidatosCards = dash.innerHTML

    findCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'flex'    
        dash.innerHTML = candidatosCards
        cadidatoPanel()
        pointSkills()
    })
}

export function findMatch(): void {
    const matchCandBtn = document.querySelector('.match-cand') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    candidatosCards = dash.innerHTML

    matchCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        dash.innerHTML = viewMatch() 
        cadidatoPanel()
        pointSkills()
    })
}

export function findVaga(): void {
    const viewVagaBtn = document.querySelector('.view-vaga') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    candidatosCards = dash.innerHTML

    viewVagaBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        dash.innerHTML = viewVagaEmp()
        editVagaPanel()
        pointSkills()
    })
}