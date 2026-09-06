import ApexCharts from 'apexcharts'
import { addVaga, editEmpProfile, editVaga, viewPanel, deleteEmpresaProfile } from "../pages/empresaDash"
import { VagaService } from '../ts/services/VagaService'
import { EmpresaService } from '../ts/services/EmpresaService'
import { CandidatoService } from '../ts/services/CandidatoService'
import { CurtidaService } from '../ts/services/CurtidaService'
import type { Vaga } from '../ts/models/Vaga'
import { atualizaSidebarEmpresa } from '../main'

const empresaService = new EmpresaService(localStorage)
const vagaService = new VagaService(localStorage)
const candidatoService = new CandidatoService(localStorage)
const curtidaService = new CurtidaService(localStorage)

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

        const empresa = empresaService.empresaAtual()
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
                
                empresaService.salvarEmpresa(empresa)
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
        const empLogada = empresaService.empresaAtual()
        if (empLogada) {
            empresaService.excluirEmpresa(empLogada.cpnj)
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
            
            const empresaLogada = empresaService.empresaAtual() || {
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

            vagaService.salvarVaga(novaVaga)
            panel.remove()
            
            document.querySelector<HTMLElement>('.view-vaga')?.click()
        })
    })
}

export function editVagaPanel(index: number): void {
    const vagaAtual = vagaService.listarVagas()[index]
    if (!vagaAtual) return

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
        vagaService.excluirVaga(index)
        panel.remove()
        document.querySelector<HTMLElement>('.view-vaga')?.click()
    })

    const form = panel.querySelector('form')
    form?.addEventListener('submit', (e) => {
        e.preventDefault()
        
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

        vagaService.atualizarVaga(index, vagaAtualizada)
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
    const empresaLogada = empresaService.empresaAtual()
    const todosCandidatos = candidatoService.listarCandidatos()
    const avaliacoesFeitas = curtidaService.listarMatchs().filter(m => m.empresa.cpnj === empresaLogada?.cpnj)
    const candidatosNaoAvaliados = todosCandidatos.filter(cand => 
        !avaliacoesFeitas.some(a => a.candidato.cpf === cand.cpf)
    )
    const competencias = new Map<string, number>()
    
    candidatosNaoAvaliados.forEach((cand) => {
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
    const empresaLogada = empresaService.empresaAtual()

    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    findCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'flex'
        
        if (!empresaLogada) return;

        const candidaturas = curtidaService.listarCurtidas().filter(c => 
            c.vaga.empresa.cpnj === empresaLogada.cpnj && 
            c.like === true
        )
        const avaliacoesFeitas = curtidaService.listarMatchs().filter(m => m.empresa.cpnj === empresaLogada.cpnj)

        const candidaturasDisponiveis = candidaturas.filter(c => 
            !avaliacoesFeitas.some(a => 
                a.candidato.cpf === c.candidato.cpf && 
                a.vaga.nome === c.vaga.nome
            )
        )

        if(candidaturasDisponiveis.length > 0) {
            dash.innerHTML = candidaturasDisponiveis.map((curtida, index) => {
                const afinidade = curtidaService.calcularAfinidade(curtida.candidato, curtida.vaga)
                return `
                <div class="card" data-index="${index}">
                    <div class="info-job">
                        <p class="porcent-match">MATCH: ${afinidade.toFixed(0)}%</p>
                        <p class="name">Vaga: ${curtida.vaga.nome}</p>
                        <p class="address">${curtida.candidato.estado}, ${curtida.candidato.pais}</p>
                        <p class="description">${curtida.candidato.descricao}</p>
                        <div class="skills-class">
                            ${curtida.candidato.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass btn-passar">Passar</button>
                        <button class="like btn-curtir">Dar Match</button>
                    </div>
                </div>`
            }).join('')

            dash.querySelectorAll('.btn-curtir').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const card = (e.target as HTMLElement).closest('.card')
                    const curtidaRef = candidaturasDisponiveis[Number(card?.getAttribute('data-index'))]
                    
                    curtidaService.salvarCurtidaEmpresa(empresaLogada, true, curtidaRef)
                    card?.remove()
                })
            })
            
            dash.querySelectorAll('.btn-passar').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const card = (e.target as HTMLElement).closest('.card')
                    const curtidaRef = candidaturasDisponiveis[Number(card?.getAttribute('data-index'))]
                    
                    curtidaService.salvarCurtidaEmpresa(empresaLogada, false, curtidaRef)
                    card?.remove() 
                })
            })
        } else {
            dash.innerHTML = '<p style="padding: 2rem; color: #64748B;">Nenhum novo candidato para avaliar</p>'
        }
        cadidatoPanel()
        pointSkills()
    })
}

export function findMatch(): void {
    const matchCandBtn = document.querySelector('.match-cand') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    const empresaLogada = empresaService.empresaAtual()

    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    matchCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        
        const matchsConfirmados = curtidaService.listarMatchs().filter(m => 
            m.empresa.cpnj === empresaLogada?.cpnj && 
            m.like === true
        )

        if (matchsConfirmados.length > 0) {
            dash.innerHTML = matchsConfirmados.map(match => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${match.candidato.nome} - Vaga: ${match.vaga.nome}</p>
                        <p class="address">${match.candidato.estado}, ${match.candidato.pais}</p>
                        <p class="description">${match.candidato.descricao}</p>
                        <div class="skills-class">
                            ${match.candidato.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass">Deletar</button>
                        <button class="like">Conversar</button>
                    </div>
                </div>
            `).join('')
        } else {
             dash.innerHTML = '<p style="padding: 2rem; color: #64748B;">Nenhum match confirmado</p>'
        }
        cadidatoPanel()
        pointSkills()
    })
}

export function findVaga(): void {
    const viewVagaBtn = document.querySelector('.view-vaga') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const graphDash = document.querySelector('.graph-class') as HTMLElement
    const empresaLogada = empresaService.empresaAtual()

    if (!dashOriginalHTML && dash) dashOriginalHTML = dash.innerHTML

    viewVagaBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        graphDash.style.display = 'none'
        
        const vagas = vagaService.listarVagas().filter(v => v.empresa.cpnj === empresaLogada?.cpnj)

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
                    <div class="choice" style="visibility: hidden;">
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
                    editVagaPanel(index)
                })
            })
        } else {
             dash.innerHTML = '<p style="padding: 2rem; color: #64748B;">Sua empresa nao possui vagas cadastradas</p>'
        }
        pointSkills()
    })
}