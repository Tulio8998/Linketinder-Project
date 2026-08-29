import ApexCharts from 'apexcharts'
import { addVaga, editEmpProfile, editVaga, viewPanel, deleteEmpresaProfile } from "../pages/empresaDash"
import { VagaService } from '../ts/services/VagaService'
import { EmpresaService } from '../ts/services/EmpresaService'
import { CandidatoService } from '../ts/services/CandidatoService'
import type { Vaga } from '../ts/models/Vaga'
import { atualizaSidebarEmpresa } from '../main'

export function configSkills(container: HTMLElement | Document = document): void {
    const novaSkill = container.querySelector('#input-skill') as HTMLInputElement
    const skillsClass = container.querySelector('.info-professional .skills-class')

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
            removeSkill.addEventListener('click', () => skillDiv.remove())
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
        configSkills(panel)

        const empresa = EmpresaService.empresaAtual()
        const inputs = panel.querySelectorAll<HTMLInputElement>('.input-data')
        const selects = panel.querySelectorAll<HTMLSelectElement>('select')
        const textarea = panel.querySelector('textarea')
        const skillsClass = panel.querySelector('.info-professional .skills-class')

        if (empresa) {
            inputs[0].value = empresa.nome
            inputs[1].value = empresa.cpnj
            selects[0].value = empresa.pais
            selects[1].value = empresa.estado
            inputs[2].value = empresa.cep
            if (textarea) textarea.value = empresa.descricao
            
            empresa.competencias.forEach(comp => {
                const skillDiv = document.createElement('div')
                skillDiv.className = 'skill-div'
                skillDiv.innerHTML = `<p class="skill">${comp}</p><p class="remove-skill">x</p>`
                skillDiv.querySelector('.remove-skill')?.addEventListener('click', () => skillDiv.remove())
                skillsClass?.appendChild(skillDiv)
            })
        }

        panel.querySelector('.btn-cancel')?.addEventListener('click', (e) => {
            e.preventDefault(); panel.remove()
        })
        panel.querySelector('.btn-delete')?.addEventListener('click', (e) => {
            e.preventDefault(); deleteEmpresa()
        })

        panel.querySelector('form')?.addEventListener('submit', (e) => {
            e.preventDefault()
            if (empresa) {
                const skillsElements = panel.querySelectorAll('.info-professional .skill')
                empresa.nome = inputs[0].value
                empresa.cpnj = inputs[1].value
                empresa.pais = selects[0].value
                empresa.estado = selects[1].value
                empresa.cep = inputs[2].value
                empresa.descricao = textarea?.value || ''
                empresa.competencias = Array.from(skillsElements).map(s => s.textContent || '')
                EmpresaService.salvarEmpresa(empresa)
                localStorage.setItem('empresa_atual', JSON.stringify(empresa))
                
                atualizaSidebarEmpresa()
            }
            panel.remove()
        })
    })
}

export function deleteEmpresa(): void {
    const panel = document.createElement('div')
    panel.className = 'delete-panel'
    panel.innerHTML = deleteEmpresaProfile()
    document.body.appendChild(panel)

    panel.querySelector('.cancel-delete')?.addEventListener('click', (e) => {
        e.preventDefault(); panel.remove()
    })
    panel.querySelector('.confirm-delete')?.addEventListener('click', (e) => {
        e.preventDefault()
        const empLogada = EmpresaService.empresaAtual()
        if (empLogada) {
            EmpresaService.excluirEmpresa(empLogada.cpnj)
        }
        localStorage.removeItem('empresa_atual')
        window.location.reload()
    })
}

export function addVagaPanel(): void {
    const addNav = document.querySelector('.add-vaga')
    addNav?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'add-panel'
        panel.innerHTML = addVaga()
        document.body.appendChild(panel)
        configSkills(panel)

        panel.querySelector('.cancel')?.addEventListener('click', (e) => {
            e.preventDefault(); panel.remove()
        })

        const form = panel.querySelector('form')
        form?.addEventListener('submit', (e) => {
            e.preventDefault()
            
            const inputs = form.querySelectorAll<HTMLInputElement>('.input-data')
            const selects = form.querySelectorAll<HTMLSelectElement>('select')
            const textarea = form.querySelector('textarea')
            const skillsElements = form.querySelectorAll('.info-professional .skill')
            
            const empresaLogada = EmpresaService.empresaAtual() || {
                nome: '', email: '', pais: '', estado: '', cep: '', descricao: '', competencias: [], cpnj: ''
            }

            const novaVaga: Vaga = {
                nome: inputs[0].value,
                pais: selects[0].value, 
                estado: selects[1].value,
                cidade: selects[2].value,
                descricao: textarea?.value || '',
                competencias: Array.from(skillsElements).map(s => s.textContent || ''),
                empresa: empresaLogada
            }

            VagaService.salvarVaga(novaVaga)
            panel.remove()
            
            document.querySelector<HTMLElement>('.view-vaga')?.click()
        })
    })
}

export function editVagaPanel(vagaAtual: Vaga): void {
    const panel = document.createElement('div')
    panel.className = 'edit-panel'
    panel.innerHTML = editVaga()
    document.body.appendChild(panel)
    configSkills(panel)

    const inputs = panel.querySelectorAll<HTMLInputElement>('.input-data')
    const selects = panel.querySelectorAll<HTMLSelectElement>('select')
    const textarea = panel.querySelector('textarea')
    const skillsClass = panel.querySelector('.info-professional .skills-class')

    inputs[0].value = vagaAtual.nome
    selects[0].value = vagaAtual.pais
    selects[1].value = vagaAtual.estado
    selects[2].value = vagaAtual.cidade
    if (textarea) textarea.value = vagaAtual.descricao

    vagaAtual.competencias.forEach(comp => {
        const skillDiv = document.createElement('div')
        skillDiv.className = 'skill-div'
        skillDiv.innerHTML = `<p class="skill">${comp}</p><p class="remove-skill">x</p>`
        skillDiv.querySelector('.remove-skill')?.addEventListener('click', () => skillDiv.remove())
        skillsClass?.appendChild(skillDiv)
    })

    panel.querySelector('.btn-cancel')?.addEventListener('click', (e) => {
        e.preventDefault(); panel.remove()
    })

    panel.querySelector('.btn-delete')?.addEventListener('click', (e) => {
        e.preventDefault()
        VagaService.excluirVaga(vagaAtual)
        panel.remove()
        document.querySelector<HTMLElement>('.view-vaga')?.click()
    })

    const form = panel.querySelector('form')
    form?.addEventListener('submit', (e) => {
        e.preventDefault()
        
        VagaService.excluirVaga(vagaAtual)
        
        const skillsElements = panel.querySelectorAll('.info-professional .skill')
        const vagaAtualizada: Vaga = {
            ...vagaAtual,
            nome: inputs[0].value,
            pais: selects[0].value,
            estado: selects[1].value,
            cidade: selects[2].value,
            descricao: textarea?.value || '',
            competencias: Array.from(skillsElements).map(s => s.textContent || '')
        }

        VagaService.salvarVaga(vagaAtualizada)
        panel.remove()
        document.querySelector<HTMLElement>('.view-vaga')?.click()
    })
}

export function cadidatoPanel(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        card.addEventListener('click', (e) => {
            if ((e.target as HTMLElement).tagName === 'BUTTON'){
                return
            }

            const name = card.querySelector('.name')?.textContent?.trim() || ''
            const address = card.querySelector('.address')?.textContent?.trim() || ''
            const description = card.querySelector('.description')?.textContent?.trim() || ''
            const match = card.querySelector('.porcent-match')?.textContent?.trim() || ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(e => e.textContent?.trim() || '')
            
            const panel = document.createElement('div')
            panel.className = 'card-panel'
            panel.innerHTML = viewPanel(name, address, description, match, skills)

            document.body.appendChild(panel)
            panel.querySelector('.close-card')?.addEventListener('click', (ev) => {
                ev.preventDefault(); panel.remove()
            })
        })
    })
}

export function pointSkills(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        const pointers = card.querySelector('.pointers')
        const skills = card.querySelectorAll('.skill')
        const pointerExis = pointers?.querySelector('.pointer')

        if (skills.length > 6 && pointers && !pointerExis) {
            const pointer = document.createElement('p')
            pointer.className = 'pointer'
            pointer.textContent = '...'
            pointers.appendChild(pointer)
        }
    })
}

export function graphCand(): void {
    const candidatos = CandidatoService.listarCandidatos()
    const competencias = new Map<string, number>()
    
    candidatos.forEach((cand) => {
        cand.competencias.forEach((comp) => {
            competencias.set(comp, (competencias.get(comp) ?? 0) + 1)
        })
    })

    const categorias = Array.from(competencias.keys())
    const valores = Array.from(competencias.values())
    const options: ApexCharts.ApexOptions = {
        chart: { type: 'bar', height: 400, toolbar: { show: false } },
        colors: ['#22C55E'],
        grid: { padding: { bottom: 20 } },
        series: [{ name: 'Quantidade de candidatos', data: valores }],
        xaxis: { categories: categorias, title: { text: 'Competências' } },
        yaxis: { min: 0, tickAmount: 5, title: { text: 'Número de candidatos' } },
        tooltip: { y: { formatter: (value) => `${value} candidatos` } }
    }
    const graph = document.querySelector('#graph') as HTMLElement
    if (graph) {
        graph.innerHTML = ''
        const chart = new ApexCharts(graph, options)
        chart.render()
    }
}

let dashOriginalHTML: string = ''

export function findCand(): void {
    const findCandBtn = document.querySelector('.find-cand')
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    findCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'flex'    
        
        const candidatos = CandidatoService.listarCandidatos()
        if(candidatos.length > 0) {
            dash.innerHTML = candidatos.map(cand => `
                <div class="card">
                    <div class="info-job">
                        <p class="porcent-match">MATCH: 100%</p>
                        <p class="name">Nome vaga aplicada</p>
                        <p class="address">${cand.estado}, ${cand.pais}</p>
                        <p class="description">${cand.descricao}</p>
                        <div class="skills-class">
                            ${cand.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass">Passar</button>
                        <button class="like">Curtir</button>
                    </div>
                </div>
            `).join('')
        } else {
            dash.innerHTML = dashOriginalHTML
        }

        cadidatoPanel()
        pointSkills()
    })
}

export function findMatch(): void {
    const matchCandBtn = document.querySelector('.match-cand') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    matchCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        
        const candidatos = CandidatoService.listarCandidatos()
        if (candidatos.length > 0) {
            dash.innerHTML = candidatos.map(cand => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${cand.nome}</p>
                        <p class="address">${cand.estado}, ${cand.pais}</p>
                        <p class="description">${cand.descricao}</p>
                        <div class="skills-class">
                            ${cand.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass">Deletar</button>
                        <button class="like">Conversar</button>
                    </div>
                </div>
            `).join('')
        }

        cadidatoPanel()
        pointSkills()
    })
}

export function findVaga(): void {
    const viewVagaBtn = document.querySelector('.view-vaga') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    viewVagaBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        
        const vagas = VagaService.listarVagas()
        if (vagas.length > 0) {
            dash.innerHTML = vagas.map(vaga => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${vaga.nome}</p>
                        <p class="address">${vaga.cidade}, ${vaga.estado} - ${vaga.pais}</p>
                        <p class="description">${vaga.descricao}</p>
                        <div class="skills-class">
                            ${vaga.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass">Passar</button>
                        <button class="like">Curtir</button>
                    </div>
                </div>
            `).join('')
            
            const cards = dash.querySelectorAll('.card')
            cards.forEach((card, index) => {
                card.addEventListener('click', (e) => {
                    if ((e.target as HTMLElement).tagName === 'BUTTON'){
                        return
                    }
                    editVagaPanel(vagas[index])
                })
            })
        }

        pointSkills()
    })
}