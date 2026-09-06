import { deleteCandidatoProfile, editProfile, viewPanel, viewVagaCand } from "../pages/candidatoDash"
import { CandidatoService } from '../ts/services/CandidatoService'
import { VagaService } from '../ts/services/VagaService'
import { CurtidaService } from '../ts/services/CurtidaService'
import { atualizaSidebarCandidato } from "../main"

const candidatoService = new CandidatoService(localStorage)
const vagaService = new VagaService(localStorage)
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

export function cadidatoPanel(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        card.addEventListener('click', (e) => {
            if ((e.target as HTMLElement).tagName === 'BUTTON') {
                return
            }
            
            const name = card.querySelector('.name')?.textContent ?? ''
            const address = card.querySelector('.address')?.textContent ?? ''
            const description = card.querySelector('.description')?.textContent ?? ''
            const match = card.querySelector('.porcent-match')?.textContent ?? ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(el => el.textContent ?? '')
            
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

export function editCandPanel(): void {
    const editButton = document.querySelector('.btn-edit')
    editButton?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'edit-panel'
        panel.innerHTML = editProfile()
        document.body.appendChild(panel)
        configSkills(panel)

        const cand = candidatoService.candidatoAtual()
        const inputs = panel.querySelectorAll<HTMLInputElement>('.input-data')
        const selects = panel.querySelectorAll<HTMLSelectElement>('select')
        const textarea = panel.querySelector('textarea')
        const skillsClass = panel.querySelector('.info-professional .skills-class')

        if (cand) {
            inputs[0].value = cand.nome
            inputs[1].value = cand.cpf
            inputs[2].value = cand.idade.toString()
            selects[0].value = cand.pais
            selects[1].value = cand.estado
            inputs[3].value = cand.cep
            if (textarea) textarea.value = cand.descricao

            cand.competencias.forEach(comp => {
                const skillDiv = document.createElement('div')
                skillDiv.className = 'skill-div'
                skillDiv.innerHTML = `<p class="skill">${comp}</p><p class="remove-skill">x</p>`
                skillDiv.querySelector('.remove-skill')?.addEventListener('click', () => skillDiv.remove())
                skillsClass?.appendChild(skillDiv)
            })
        }

        panel.querySelector('.btn-cancel')?.addEventListener('click', (e) => {
            e.preventDefault(); 
            panel.remove()
        })

        panel.querySelector('.btn-delete')?.addEventListener('click', (e) => {
            e.preventDefault(); 
            deleteCandidato()
        })

        panel.querySelector('form')?.addEventListener('submit', (e) => {
            e.preventDefault()
            if (cand) {
                const skillsElements = panel.querySelectorAll('.info-professional .skill')
                cand.nome = inputs[0].value
                cand.cpf = inputs[1].value
                cand.idade = Number(inputs[2].value)
                cand.pais = selects[0].value
                cand.estado = selects[1].value
                cand.cep = inputs[3].value
                cand.descricao = textarea?.value || ''
                cand.competencias = Array.from(skillsElements).map(s => s.textContent || '')
                
                const lista = candidatoService.listarCandidatos()
                const index = lista.findIndex(c => c.cpf === cand.cpf)
                if(index > -1) lista[index] = cand
                localStorage.setItem('candidatos_db', JSON.stringify(lista))
                localStorage.setItem('candidato_atual', JSON.stringify(cand))
                
                atualizaSidebarCandidato()
            }
            panel.remove()
        })
    })
}

export function deleteCandidato(): void {
    const panel = document.createElement('div')
    panel.className = 'delete-panel'
    panel.innerHTML = deleteCandidatoProfile()
    document.body.appendChild(panel)

    panel.querySelector('.cancel-delete')?.addEventListener('click', (e) => {
        e.preventDefault(); panel.remove()
    })

    panel.querySelector('.confirm-delete')?.addEventListener('click', (e) => {
        e.preventDefault()
        const candLogado = candidatoService.candidatoAtual()
        if(candLogado) {
            candidatoService.excluirCandidato(candLogado.cpf)
        }
        localStorage.removeItem('candidato_atual')
        window.location.reload()
    })
}

export function vagaPanel(): void {
    const cards = document.querySelectorAll('.card')
    cards.forEach((card) => {
        card.addEventListener('click', (e) => {
            if ((e.target as HTMLElement).tagName === 'BUTTON'){
                return
            }
            const name = card.querySelector('.name')?.textContent ?? ''
            const address = card.querySelector('.address')?.textContent ?? ''
            const description = card.querySelector('.description')?.textContent ?? ''
            const match = card.querySelector('.porcent-match')?.textContent ?? ''
            const skills = Array.from(card.querySelectorAll('.skill')).map(el => el.textContent ?? '')
            
            const panel = document.createElement('div')
            panel.className = 'card-panel'
            panel.innerHTML = viewVagaCand(name, address, description, match, skills)
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

let candidatosCards: string = ''

export function findVaga(): void {
    const findVagaBtn = document.querySelector('.find-vaga')
    const dash = document.querySelector('.match-cards') as HTMLElement
    const candLogado = candidatoService.candidatoAtual()

    if (!candidatosCards && dash) {
        candidatosCards = dash.innerHTML
    }

    findVagaBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'           
        
        if (!candLogado) {
            return;
        }
        
        const todasVagas = vagaService.listarVagas()
        const interacoesCand = curtidaService.listarCurtidas().filter(c => c.candidato.cpf === candLogado.cpf)

        const vagasNaoAvaliadas = todasVagas.filter(vaga => 
            !interacoesCand.some(interacao => 
                interacao.vaga.nome === vaga.nome && 
                interacao.vaga.empresa.cpnj === vaga.empresa.cpnj
            )
        )

        if (vagasNaoAvaliadas.length > 0) {
            dash.innerHTML = vagasNaoAvaliadas.map((vaga, index) => {
                const afinidade = curtidaService.calcularAfinidade(candLogado, vaga)
                return `
                <div class="card" data-index="${index}">
                    <div class="info-job">
                        <p class="porcent-match">MATCH: ${afinidade.toFixed(0)}%</p>
                        <p class="name">${vaga.nome}</p>
                        <p class="address">${vaga.cidade}, ${vaga.estado} - ${vaga.pais}</p>
                        <p class="description">${vaga.descricao}</p>
                        <div class="skills-class">
                            ${vaga.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
                        </div>
                    </div>
                    <div class="pointers"></div>
                    <div class="choice">
                        <button class="pass btn-passar">Passar</button>
                        <button class="like btn-curtir">Curtir</button>
                    </div>
                </div>`
            }).join('')

            dash.querySelectorAll('.btn-curtir').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const card = (e.target as HTMLElement).closest('.card')
                    const vagaIndex = Number(card?.getAttribute('data-index'))
                    const vaga = vagasNaoAvaliadas[vagaIndex]
                    
                    curtidaService.salvarCurtidaCandidato(candLogado, { like: true, vaga })
                    card?.remove() 
                })
            })
            
            dash.querySelectorAll('.btn-passar').forEach(btn => {
                btn.addEventListener('click', (e) => {
                    const card = (e.target as HTMLElement).closest('.card')
                    const vagaIndex = Number(card?.getAttribute('data-index'))
                    const vaga = vagasNaoAvaliadas[vagaIndex]

                    curtidaService.salvarCurtidaCandidato(candLogado, { like: false, vaga })
                    card?.remove() 
                })
            })
        } else {
            dash.innerHTML = '<p style="padding: 2rem; color: #64748B;">Você já avaliou todas as vagas disponiveis</p>'
        }
        vagaPanel()
        pointSkills()
    })
}

export function findMatch(): void {
    const matchCandBtn = document.querySelector('.match-cand') 
    const dash = document.querySelector('.match-cards') as HTMLElement
    const candLogado = candidatoService.candidatoAtual()

    if (!candidatosCards && dash) candidatosCards = dash.innerHTML

    matchCandBtn?.addEventListener('click', () => {
        dash.style.display = 'grid'
        
        const matchsConfirmados = curtidaService.listarMatchs().filter(m => 
            m.candidato.cpf === candLogado?.cpf && 
            m.like === true
        )

        if (matchsConfirmados.length > 0) {
            dash.innerHTML = matchsConfirmados.map(match => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${match.vaga.nome} na ${match.empresa.nome}</p>
                        <p class="address">${match.vaga.cidade}, ${match.vaga.estado} - ${match.vaga.pais}</p>
                        <p class="description">${match.vaga.descricao}</p>
                        <div class="skills-class">
                            ${match.vaga.competencias.map(skill => `<p class="skill">${skill}</p>`).join('')}
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