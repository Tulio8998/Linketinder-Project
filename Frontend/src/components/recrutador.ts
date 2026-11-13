import { viewPanel } from '../pages/candidatoDash'
import { RecrutadorService } from '../ts/services/RecrutadorService'
import type {
    CandidatoAnonimo,
    Recrutador,
    VagaRecrutador
} from '../ts/models/Recrutador'
import { escapeHtml } from '../ts/util/html'

const recrutadorService = new RecrutadorService()
let vagasCarregadas: VagaRecrutador[] = []

function mostrarErro(error: unknown): void {
    alert(error instanceof Error ? error.message : 'Não foi possível concluir a operação.')
}

function renderizarVaga(vaga: VagaRecrutador, index: number): string {
    const skills = Array.isArray(vaga.competencias) ? vaga.competencias : []
    return `
        <div class="card" data-index="${index}">
            <div class="info-job">
                <p class="name">${escapeHtml(vaga.nome)}</p>
                <p class="address">${escapeHtml(vaga.area)}</p>
                <p class="description">${escapeHtml(vaga.descricao)}</p>
                <div class="skills-class">
                    ${skills.map((skill) => `<p class="skill">${escapeHtml(skill)}</p>`).join('')}
                </div>
                <div class="choice"><button class="like view-candidates">Ver candidatos</button></div>
            </div>
        </div>`
}

function renderizarCandidato(
    candidato: CandidatoAnonimo,
    index: number
): string {
    const formacoes = Array.isArray(candidato.formacoes) ? candidato.formacoes : []
    const competencias = Array.isArray(candidato.competencias) ? candidato.competencias : []
    return `
        <div class="card" data-index="${index}">
            <div class="info-job">
                <p class="description">${escapeHtml(candidato.descricao)}</p>
                ${formacoes.length
                    ? `<p>Formação: ${formacoes.map(escapeHtml).join(', ')}</p>`
                    : ''}
                <div class="skills-class">
                    ${competencias.map((skill) => `<p class="skill">${escapeHtml(skill)}</p>`).join('')}
                </div>
                <div class="pointers"></div>
                <div class="choice">
                    <button class="pass reject-candidate">Passar</button>
                    <button class="like like-candidate">Curtir</button>
                </div>
            </div>
        </div>`
}

function abrirPainel(
    card: HTMLElement,
    candidato: CandidatoAnonimo,
    vaga: VagaRecrutador,
    avaliar: (curtiu: boolean, source: HTMLElement) => Promise<void>
): void {
    const formacoes = Array.isArray(candidato.formacoes) ? candidato.formacoes : []
    const panel = document.createElement('div')
    panel.className = 'card-panel'
    panel.innerHTML = viewPanel(
        '',
        '',
        candidato.descricao,
        '',
        Array.isArray(candidato.competencias) ? candidato.competencias : []
    )
    const info = panel.querySelector('.info-job')
    if (info && formacoes.length) {
        const education = document.createElement('p')
        education.textContent = `Formação: ${formacoes.join(', ')}`
        info.insertBefore(education, info.querySelector('.description'))
    }
    document.body.appendChild(panel)
    panel.querySelector('.close-card')?.addEventListener('click', (event) => {
        event.preventDefault()
        panel.remove()
    })
    panel.querySelector('.like')?.addEventListener('click', () => {
        void avaliar(true, card).then(() => panel.remove()).catch(mostrarErro)
    })
    panel.querySelector('.pass')?.addEventListener('click', () => {
        void avaliar(false, card).then(() => panel.remove()).catch(mostrarErro)
    })
    panel.dataset.vagaName = vaga.nome
}

async function carregarCandidatos(
    vaga: VagaRecrutador,
    dashboard: HTMLElement
): Promise<void> {
    const candidatos = await recrutadorService.listarCandidatos(vaga.id)
    if (!candidatos.length) {
        dashboard.innerHTML = '<p class="none-vaga">Não há novos perfis anônimos para esta vaga.</p>'
        return
    }

    dashboard.innerHTML = candidatos.map(renderizarCandidato).join('')
    dashboard.querySelectorAll<HTMLElement>('.card').forEach((card, index) => {
        const candidato = candidatos[index]
        const avaliar = async (curtiu: boolean, source: HTMLElement): Promise<void> => {
            await recrutadorService.avaliar(candidato.id, vaga.id, curtiu)
            source.remove()
        }
        card.querySelector('.like-candidate')?.addEventListener('click', (event) => {
            event.stopPropagation()
            void avaliar(true, card).catch(mostrarErro)
        })
        card.querySelector('.reject-candidate')?.addEventListener('click', (event) => {
            event.stopPropagation()
            void avaliar(false, card).catch(mostrarErro)
        })
        card.addEventListener('click', (event) => {
            if ((event.target as HTMLElement).closest('button')) return
            abrirPainel(card, candidato, vaga, avaliar)
        })
    })
}

export async function atualizaSidebarRecrutador(): Promise<Recrutador> {
    const perfil = await recrutadorService.obterPerfil()
    const root = document.querySelector<HTMLElement>('.candidatoDash-class')
    const name = root?.querySelector<HTMLElement>('.profile-content .name')
    const area = root?.querySelector<HTMLElement>('.profile-content .area')
    const description = root?.querySelector<HTMLElement>('.about .description')
    const areaSkill = root?.querySelector<HTMLElement>('.recruiter-area')
    if (name) name.textContent = perfil.nome
    if (area) area.textContent = perfil.area
    if (description) description.textContent = perfil.descricao
    if (areaSkill) {
        const skill = document.createElement('p')
        skill.className = 'skill'
        skill.textContent = perfil.area
        areaSkill.replaceChildren(skill)
    }
    return perfil
}

export function recrutadorFindVaga(): void {
    const button = document.querySelector('.find-vaga')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        void (async () => {
            vagasCarregadas = await recrutadorService.listarVagas()
            if (!vagasCarregadas.length) {
                dashboard.innerHTML = '<p class="none-vaga">Não há vagas da sua área disponíveis para triagem.</p>'
                return
            }
            dashboard.innerHTML = vagasCarregadas.map(renderizarVaga).join('')
            dashboard.querySelectorAll<HTMLButtonElement>('.view-candidates').forEach((button) => {
                button.addEventListener('click', (event) => {
                    event.stopPropagation()
                    const index = Number(button.closest<HTMLElement>('.card')?.dataset.index)
                    const vaga = vagasCarregadas[index]
                    if (vaga) void carregarCandidatos(vaga, dashboard).catch(mostrarErro)
                })
            })
        })().catch(mostrarErro)
    })
}

export function recrutadorHistorico(): void {
    const button = document.querySelector('.match-cand')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        void recrutadorService.listarHistorico().then((received) => {
            const historico = Array.isArray(received) ? received : []
            if (!historico.length) {
                dashboard.innerHTML = '<p class="none-vaga">Nenhuma avaliação registrada.</p>'
                return
            }
            dashboard.innerHTML = historico.filter(Boolean).map((avaliacao) => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${escapeHtml(avaliacao.vaga)} — ${escapeHtml(avaliacao.area)}</p>
                        ${avaliacao.autor ? `<p class="address">Avaliado por ${escapeHtml(avaliacao.autor)}</p>` : ''}
                        <p class="description">${escapeHtml(avaliacao.descricaoCandidato)}</p>
                        <div class="skills-class">
                            ${(Array.isArray(avaliacao.competencias) ? avaliacao.competencias : [])
                                .map((skill) => `<p class="skill">${escapeHtml(skill)}</p>`).join('')}
                        </div>
                        <p>${avaliacao.curtiu ? 'Curtir' : 'Passar'}</p>
                    </div>
                </div>
            `).join('')
        }).catch(mostrarErro)
    })
}
