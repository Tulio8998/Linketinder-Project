import { deleteCandidatoProfile, editProfile, viewVagaCand } from '../pages/candidatoDash'
import { CandidatoService } from '../ts/services/CandidatoService'
import { VagaService } from '../ts/services/VagaService'
import { CurtidaService } from '../ts/services/CurtidaService'
import { CompetenciaService } from '../ts/services/CompetenciaService'
import { RecrutadorService } from '../ts/services/RecrutadorService'
import { atualizaSidebarCandidato } from '../main'
import type { Vaga } from '../ts/models/Vaga'
import { escapeHtml } from '../ts/util/html'
import {
    abrirPainelCard,
    coletarSkillsDoPainel,
    configSkills,
    pointSkills,
    renderizarSkillsExistentes
} from './shared'

export { configSkills as configCandSkills, pointSkills as candPointSkills }

const candidatoService = new CandidatoService()
const vagaService = new VagaService()
const curtidaService = new CurtidaService()
const competenciaService = new CompetenciaService()
const recrutadorService = new RecrutadorService()

function mostrarErro(error: unknown): void {
    alert(error instanceof Error ? error.message : 'Não foi possível concluir a operação.')
}

function valorDoCampo(panel: HTMLElement, campo: string): HTMLInputElement {
    const input = panel.querySelector<HTMLInputElement>(`[data-field="${campo}"]`)
    if (!input) throw new Error(`Campo ${campo} não encontrado.`)
    return input
}

function renderizarVaga(vaga: Vaga, afinidade: number, index: number): string {
    return `
        <div class="card" data-index="${index}">
            <div class="info-job">
                <p class="porcent-match">AFINIDADE: ${afinidade.toFixed(0)}%</p>
                <p class="name">${escapeHtml(vaga.nome)}</p>
                <p class="address">${escapeHtml(vaga.area)}</p>
                <p class="description">${escapeHtml(vaga.descricao)}</p>
                <div class="skills-class">
                    ${vaga.competencias.map((skill) => `<p class="skill">${escapeHtml(skill)}</p>`).join('')}
                </div>
            </div>
            <div class="pointers"></div>
            <div class="choice">
                <button class="pass btn-passar">Passar</button>
                <button class="like btn-curtir">Curtir</button>
            </div>
        </div>`
}

function abrirDetalheVaga(
    card: HTMLElement,
    vaga: Vaga,
    avaliar: (curtiu: boolean, source: HTMLElement) => Promise<void>
): void {
    const panel = document.createElement('div')
    panel.className = 'card-panel'
    panel.innerHTML = viewVagaCand(
        card.querySelector('.name')?.textContent ?? '',
        card.querySelector('.address')?.textContent ?? '',
        card.querySelector('.description')?.textContent ?? '',
        card.querySelector('.porcent-match')?.textContent ?? '',
        Array.from(card.querySelectorAll('.skill')).map((skill) => skill.textContent ?? '')
    )
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
    panel.dataset.vagaId = String(vaga.id)
}

export function editCandPanel(): void {
    document.querySelector('.btn-edit')?.addEventListener('click', () => {
        void (async () => {
            const cand = await candidatoService.obterPerfil()
            const skills = await competenciaService.listarDoCandidato(cand.id)
            const panel = document.createElement('div')
            panel.className = 'edit-panel'
            panel.innerHTML = editProfile()
            document.body.appendChild(panel)
            void configSkills(panel).catch(mostrarErro)

            valorDoCampo(panel, 'nome').value = cand.nome
            valorDoCampo(panel, 'cpf').value = cand.cpf
            valorDoCampo(panel, 'dataNascimento').value = cand.dataNascimento
            valorDoCampo(panel, 'cidade').value = cand.cidade
            valorDoCampo(panel, 'cep').value = cand.cep
            const selects = panel.querySelectorAll<HTMLSelectElement>('.info-location select')
            selects[0].value = cand.pais
            selects[1].value = cand.estado
            const textarea = panel.querySelector('textarea')
            if (textarea) textarea.value = cand.descricao
            renderizarSkillsExistentes(panel, skills.map((skill) => skill.nome))

            panel.querySelector('.btn-cancel')?.addEventListener('click', (event) => {
                event.preventDefault()
                panel.remove()
            })
            panel.querySelector('.btn-delete')?.addEventListener('click', (event) => {
                event.preventDefault()
                deleteCandidato()
            })
            panel.querySelector('form')?.addEventListener('submit', (event) => {
                event.preventDefault()
                void (async () => {
                    await candidatoService.atualizar(cand.id, {
                        nome: valorDoCampo(panel, 'nome').value.trim(),
                        email: cand.email,
                        cpf: valorDoCampo(panel, 'cpf').value.trim(),
                        dataNascimento: valorDoCampo(panel, 'dataNascimento').value,
                        pais: selects[0].value,
                        estado: selects[1].value,
                        cidade: valorDoCampo(panel, 'cidade').value.trim(),
                        cep: valorDoCampo(panel, 'cep').value.trim(),
                        descricao: textarea?.value.trim() ?? ''
                    })
                    await competenciaService.substituirDoCandidato(
                        cand.id,
                        coletarSkillsDoPainel(panel)
                    )
                    await atualizaSidebarCandidato()
                    panel.remove()
                })().catch(mostrarErro)
            })
        })().catch(mostrarErro)
    })
}

export function deleteCandidato(): void {
    const panel = document.createElement('div')
    panel.className = 'delete-panel'
    panel.innerHTML = deleteCandidatoProfile()
    document.body.appendChild(panel)

    panel.querySelector('.cancel-delete')?.addEventListener('click', (event) => {
        event.preventDefault()
        panel.remove()
    })
    panel.querySelector('.confirm-delete')?.addEventListener('click', (event) => {
        event.preventDefault()
        if (!window.confirm('Tem certeza que deseja desativar a conta? Essa ação é irreversível.')) {
            return
        }
        void candidatoService.obterPerfil()
            .then((candidato) => candidatoService.excluir(candidato.id))
            .then(() => window.location.reload())
            .catch(mostrarErro)
    })
}

export function findVaga(): void {
    const button = document.querySelector('.find-vaga')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        dashboard.style.display = 'grid'
        void (async () => {
            const candidato = await candidatoService.obterPerfil()
            const todasAsVagas = await vagaService.listarVagas()
            const avaliadas = await curtidaService.listarVagasAvaliadasCandidato(candidato.id)
            const vagas = todasAsVagas.filter((vaga) => !avaliadas.has(vaga.id))
            if (!vagas.length) {
                dashboard.innerHTML = todasAsVagas.length
                    ? '<p class="none-vaga">Você já avaliou todas as vagas disponíveis.</p>'
                    : '<p class="none-vaga">Não há vagas disponíveis.</p>'
                return
            }
            const afinidades = await Promise.all(vagas.map((vaga) =>
                curtidaService.calcularAfinidade(candidato.id, vaga.id)
            ))
            dashboard.innerHTML = vagas
                .map((vaga, index) => renderizarVaga(vaga, afinidades[index], index))
                .join('')
            dashboard.querySelectorAll<HTMLElement>('.card').forEach((card, index) => {
                card.dataset.vagaIndex = String(index)
                const avaliar = async (curtiu: boolean, source: HTMLElement): Promise<void> => {
                    await curtidaService.salvarCurtidaCandidato(
                        candidato.id,
                        vagas[index].id,
                        curtiu
                    )
                    source.remove()
                }
                card.querySelector('.btn-curtir')?.addEventListener('click', (event) => {
                    event.stopPropagation()
                    void avaliar(true, card).catch(mostrarErro)
                })
                card.querySelector('.btn-passar')?.addEventListener('click', (event) => {
                    event.stopPropagation()
                    void avaliar(false, card).catch(mostrarErro)
                })
                card.addEventListener('click', (event) => {
                    if ((event.target as HTMLElement).closest('button')) return
                    abrirDetalheVaga(card, vagas[index], avaliar)
                })
            })
            pointSkills()
        })().catch(mostrarErro)
    })
}

export function findMatch(): void {
    const button = document.querySelector('.match-cand')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        dashboard.style.display = 'grid'
        void recrutadorService.listarMatchesCandidato().then((received) => {
            const matches = Array.isArray(received) ? received : []
            if (!matches.length) {
                dashboard.innerHTML =
                    '<p class="none-vaga">Nenhum match confirmado.</p>'
                return
            }
            dashboard.innerHTML = matches.filter((match) => match?.nomeVaga).map((match) => `
                <div class="card">
                    <div class="info-job">
                        <p class="name">${escapeHtml(match.nomeVaga)}</p>
                        <p class="address">${escapeHtml(match.nomeEmpresa ?? '')}</p>
                        <p class="description">${escapeHtml(match.descricaoEmpresa ?? '')}</p>
                        <p>Contato do recrutador: ${
                            match.emailRecrutador
                                ? `${escapeHtml(match.nomeRecrutador ?? '')} — ${escapeHtml(match.emailRecrutador)}`
                                : 'Não disponível para este match.'
                        }</p>
                        ${match.emailEmpresa
                            ? `<p>Contato da empresa: ${escapeHtml(match.emailEmpresa)}</p>`
                            : ''}
                    </div>
                </div>
            `).join('')
            abrirPainelCard()
        }).catch(mostrarErro)
    })
}
