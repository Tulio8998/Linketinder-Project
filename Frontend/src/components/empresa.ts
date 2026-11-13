import ApexCharts from 'apexcharts'
import { addVaga, editEmpProfile, editVaga, deleteEmpresaProfile } from '../pages/empresaDash'
import { VagaService } from '../ts/services/VagaService'
import { EmpresaService } from '../ts/services/EmpresaService'
import { CurtidaService } from '../ts/services/CurtidaService'
import { RecrutadorService } from '../ts/services/RecrutadorService'
import { atualizaSidebarEmpresa } from '../main'
import type { Vaga } from '../ts/models/Vaga'
import type { NovoRecrutador } from '../ts/models/Recrutador'
import { validarCpf, validarEmail } from '../ts/util/validators'
import { escapeHtml } from '../ts/util/html'
import {
    abrirPainelCard,
    coletarSkillsDoPainel,
    configSkills,
    pointSkills,
    renderizarSkillsExistentes
} from './shared'

export { configSkills as configEmpSkills, pointSkills as empPointSkills }

const empresaService = new EmpresaService()
const vagaService = new VagaService()
const curtidaService = new CurtidaService()
const recrutadorService = new RecrutadorService()

function mostrarErro(error: unknown): void {
    alert(error instanceof Error ? error.message : 'Não foi possível concluir a operação.')
}

function campo(panel: HTMLElement, nome: string): HTMLInputElement {
    const input = panel.querySelector<HTMLInputElement>(`[data-field="${nome}"]`)
    if (!input) throw new Error(`Campo ${nome} não encontrado.`)
    return input
}

function payloadVaga(panel: HTMLElement, empresaId: number): {
    nome: string
    descricao: string
    area: string
    pais: string
    estado: string
    cidade: string
    empresa: { id: number }
} {
    const selects = panel.querySelectorAll<HTMLSelectElement>('.info-location select')
    return {
        nome: campo(panel, 'nome').value.trim(),
        area: campo(panel, 'area').value.trim(),
        descricao: panel.querySelector('textarea')?.value.trim() ?? '',
        pais: selects[0].value,
        estado: selects[1].value,
        cidade: campo(panel, 'cidade').value.trim(),
        empresa: { id: empresaId }
    }
}

export function editEmpPanel(): void {
    document.querySelector('.btn-edit')?.addEventListener('click', () => {
        void (async () => {
            const empresa = await empresaService.obterPerfil()
            const panel = document.createElement('div')
            panel.className = 'edit-panel'
            panel.innerHTML = editEmpProfile()
            document.body.appendChild(panel)
            void configSkills(panel).catch(mostrarErro)
            campo(panel, 'nome').value = empresa.nome
            campo(panel, 'cnpj').value = empresa.cnpj
            campo(panel, 'cidade').value = empresa.cidade
            campo(panel, 'cep').value = empresa.cep
            const selects = panel.querySelectorAll<HTMLSelectElement>('.info-location select')
            selects[0].value = empresa.pais
            selects[1].value = empresa.estado
            const textarea = panel.querySelector('textarea')
            if (textarea) textarea.value = empresa.descricao

            panel.querySelector('.btn-cancel')?.addEventListener('click', (event) => {
                event.preventDefault()
                panel.remove()
            })
            panel.querySelector('.btn-delete')?.addEventListener('click', (event) => {
                event.preventDefault()
                deleteEmpresa()
            })
            panel.querySelector('form')?.addEventListener('submit', (event) => {
                event.preventDefault()
                void empresaService.atualizar(empresa.id, {
                    nome: campo(panel, 'nome').value.trim(),
                    cnpj: campo(panel, 'cnpj').value.trim(),
                    email: empresa.email,
                    pais: selects[0].value,
                    estado: selects[1].value,
                    cidade: campo(panel, 'cidade').value.trim(),
                    cep: campo(panel, 'cep').value.trim(),
                    descricao: textarea?.value.trim() ?? ''
                }).then(() => atualizaSidebarEmpresa())
                    .then(() => panel.remove())
                    .catch(mostrarErro)
            })
        })().catch(mostrarErro)
    })
}

export function deleteEmpresa(): void {
    const panel = document.createElement('div')
    panel.className = 'delete-panel'
    panel.innerHTML = deleteEmpresaProfile()
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
        void empresaService.obterPerfil()
            .then((empresa) => empresaService.excluir(empresa.id))
            .then(() => window.location.reload())
            .catch(mostrarErro)
    })
}

export function addVagaPanel(): void {
    document.querySelector('.add-vaga')?.addEventListener('click', () => {
        const panel = document.createElement('div')
        panel.className = 'add-panel modal-overlay'
        panel.setAttribute('role', 'dialog')
        panel.setAttribute('aria-modal', 'true')
        panel.setAttribute('aria-label', 'Adicionar vaga')
        panel.innerHTML = addVaga()
        document.body.appendChild(panel)
        void configSkills(panel).catch(mostrarErro)
        panel.addEventListener('click', (event) => {
            if (event.target === panel) panel.remove()
        })
        panel.querySelector('.cancel')?.addEventListener('click', (event) => {
            event.preventDefault()
            panel.remove()
        })
        panel.querySelector('form')?.addEventListener('submit', (event) => {
            event.preventDefault()
            void (async () => {
                const empresa = await empresaService.obterPerfil()
                const payload = payloadVaga(panel, empresa.id)
                if (!payload.nome || !payload.area) {
                    alert('Informe o nome e a área da vaga.')
                    return
                }
                await vagaService.salvarVaga(payload, coletarSkillsDoPainel(panel))
                panel.remove()
                await findVagaConteudo()
            })().catch(mostrarErro)
        })
    })
}

export async function editVagaPanel(vagaAtual: Vaga): Promise<void> {
    const empresa = await empresaService.obterPerfil()
    const panel = document.createElement('div')
    panel.className = 'edit-panel'
    panel.innerHTML = editVaga()
    document.body.appendChild(panel)
    void configSkills(panel).catch(mostrarErro)
    campo(panel, 'nome').value = vagaAtual.nome
    campo(panel, 'area').value = vagaAtual.area
    campo(panel, 'cidade').value = vagaAtual.cidade ?? ''
    const selects = panel.querySelectorAll<HTMLSelectElement>('.info-location select')
    selects[0].value = vagaAtual.pais ?? ''
    selects[1].value = vagaAtual.estado ?? ''
    if (panel.querySelector('textarea')) {
        panel.querySelector<HTMLTextAreaElement>('textarea')!.value = vagaAtual.descricao
    }
    renderizarSkillsExistentes(panel, vagaAtual.competencias)

    panel.querySelector('.btn-cancel')?.addEventListener('click', (event) => {
        event.preventDefault()
        panel.remove()
    })
    panel.querySelector('.btn-delete')?.addEventListener('click', (event) => {
        event.preventDefault()
        void vagaService.excluirVaga(vagaAtual.id)
            .then(() => panel.remove())
            .then(() => findVagaConteudo())
            .catch(mostrarErro)
    })
    panel.querySelector('form')?.addEventListener('submit', (event) => {
        event.preventDefault()
        void (async () => {
            const payload = payloadVaga(panel, empresa.id)
            if (!payload.nome || !payload.area) {
                alert('Informe o nome e a área da vaga.')
                return
            }
            await vagaService.atualizarVaga(
                vagaAtual.id,
                payload,
                coletarSkillsDoPainel(panel)
            )
            panel.remove()
            await findVagaConteudo()
        })().catch(mostrarErro)
    })
}

export function graphCand(): void {
    void empresaService.obterPerfil()
        .then((empresa) => vagaService.listarDaEmpresa(empresa.id))
        .then((vagas) => {
            const counts = new Map<string, number>()
            vagas.forEach((vaga) => counts.set(vaga.area, (counts.get(vaga.area) ?? 0) + 1))
            const graph = document.querySelector<HTMLElement>('#graph')
            if (!graph) return
            graph.innerHTML = ''
            const chart = new ApexCharts(graph, {
                chart: { type: 'bar', height: 300, toolbar: { show: false } },
                colors: ['#22C55E'],
                series: [{ name: 'Vagas', data: [...counts.values()] }],
                xaxis: { categories: [...counts.keys()], title: { text: 'Áreas' } },
                yaxis: { min: 0, title: { text: 'Número de vagas' } }
            })
            return chart.render()
        })
        .catch(mostrarErro)
}

function formCadastroRecrutador(): string {
    return `
        <form class="input-form recruiter-form">
            <div class="info-personal">
                <p class="title-personal">CADASTRAR RECRUTADOR</p>
                <div><p class="input-name">Nome</p><input class="input-data" data-field="nome" required></div>
                <div><p class="input-name">Área</p><input class="input-data" data-field="area" required></div>
                <div><p class="input-name">Descrição</p><textarea data-field="descricao" required></textarea></div>
                <div><p class="input-name">E-mail</p><input class="input-data" data-field="email" type="email" required></div>
                <div><p class="input-name">CPF</p><input class="input-data" data-field="cpf" required></div>
                <div><p class="input-name">Data de nascimento</p><input class="input-data" data-field="dataNascimento" type="date" required></div>
                <div><p class="input-name">Senha</p><input class="input-data" data-field="senha" type="password" required></div>
                <button class="create" type="submit">Cadastrar recrutador</button>
            </div>
        </form>
    `
}

async function renderizarRecrutadores(dashboard: HTMLElement): Promise<void> {
    const recrutadores = await recrutadorService.listarDaEmpresa()
    const list = dashboard.querySelector<HTMLElement>('.recruiter-list')
    if (!list) return
    if (recrutadores.length === 0) {
        list.innerHTML = '<p class="none-vaga">Ainda não há recrutadores cadastrados.</p>'
        return
    }
    list.innerHTML = recrutadores.map((recrutador, index) => `
        <div class="card" data-index="${index}">
            <div class="info-job">
                <p class="name">${escapeHtml(recrutador.nome)}</p>
                <p class="address">${escapeHtml(recrutador.area)} — ${escapeHtml(recrutador.email)}</p>
                <p class="description">${escapeHtml(recrutador.descricao)}</p>
                <p>${recrutador.ativo ? 'Ativo' : 'Desativado'}</p>
                ${recrutador.ativo ? '<button class="pass deactivate-recruiter">Desativar</button>' : ''}
            </div>
        </div>
    `).join('')
    list.querySelectorAll<HTMLButtonElement>('.deactivate-recruiter').forEach((button) => {
        button.addEventListener('click', (event) => {
            event.stopPropagation()
            const card = button.closest<HTMLElement>('.card')
            const recrutador = recrutadores[Number(card?.dataset.index)]
            if (!recrutador) return
            if (!window.confirm('Tem certeza que deseja desativar a conta? Essa ação é irreversível.')) {
                return
            }
            void recrutadorService.desativar(recrutador.id)
                .then(() => renderizarRecrutadores(dashboard))
                .catch(mostrarErro)
        })
    })
}

export function findCand(): void {
    const button = document.querySelector('.find-cand')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    const graph = document.querySelector<HTMLElement>('.graph-class')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        if (graph) graph.style.display = 'none'
        dashboard.innerHTML = `
            ${formCadastroRecrutador()}
            <div class="recruiter-list"></div>
        `
        const form = dashboard.querySelector<HTMLFormElement>('.recruiter-form')
        form?.addEventListener('submit', (event) => {
            event.preventDefault()
            void (async () => {
                const payload: NovoRecrutador = {
                    nome: campo(form, 'nome').value.trim(),
                    area: campo(form, 'area').value.trim(),
                    descricao: form.querySelector<HTMLTextAreaElement>('[data-field="descricao"]')?.value.trim() ?? '',
                    email: campo(form, 'email').value.trim(),
                    cpf: campo(form, 'cpf').value.trim(),
                    dataNascimento: campo(form, 'dataNascimento').value,
                    senha: campo(form, 'senha').value
                }
                if (!validarEmail(payload.email)) throw new Error('E-mail inválido.')
                if (!validarCpf(payload.cpf)) throw new Error('CPF inválido.')
                await recrutadorService.cadastrar(payload)
                form.reset()
                await renderizarRecrutadores(dashboard)
            })().catch(mostrarErro)
        })
        void renderizarRecrutadores(dashboard).catch(mostrarErro)
    })
}

export function findMatch(): void {
    const button = document.querySelector('.match-cand')
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    const graph = document.querySelector<HTMLElement>('.graph-class')
    button?.addEventListener('click', () => {
        if (!dashboard) return
        if (graph) graph.style.display = 'none'
        void empresaService.obterPerfil()
            .then((empresa) => curtidaService.listarMatchesEmpresa(empresa.id))
            .then((received) => {
                const matches = Array.isArray(received) ? received : []
                const validMatches = matches.filter(
                    (match) => match?.candidato && match?.vaga
                )
                if (validMatches.length === 0) {
                    dashboard.innerHTML = '<p class="none-vaga">Nenhum match confirmado.</p>'
                    return
                }
                dashboard.innerHTML = validMatches.map((match) => `
                    <div class="card">
                        <div class="info-job">
                            <p class="name">${escapeHtml(match.candidato.nome)} — ${escapeHtml(match.vaga.nome)}</p>
                            <p class="address">${escapeHtml(match.candidato.email)}</p>
                            <p class="description">${escapeHtml(match.candidato.descricao)}</p>
                        </div>
                    </div>
                `).join('')
                abrirPainelCard()
                pointSkills()
            })
            .catch(mostrarErro)
    })
}

async function findVagaConteudo(): Promise<void> {
    const dashboard = document.querySelector<HTMLElement>('.match-cards')
    const graph = document.querySelector<HTMLElement>('.graph-class')
    if (!dashboard) return
    if (graph) graph.style.display = 'none'
    const vagas = await vagaService.listarDaEmpresa((await empresaService.obterPerfil()).id)
    if (!vagas.length) {
        dashboard.innerHTML = '<p class="none-vaga">Você ainda não cadastrou vagas.</p>'
        return
    }
    dashboard.innerHTML = vagas.map((vaga, index) => `
        <div class="card" data-index="${index}">
            <div class="info-job">
                <p class="name">${escapeHtml(vaga.nome)}</p>
                <p class="address">${escapeHtml(vaga.area)} — ${escapeHtml(vaga.cidade ?? '')}</p>
                <p class="description">${escapeHtml(vaga.descricao)}</p>
                <div class="skills-class">
                    ${vaga.competencias.map((skill) => `<p class="skill">${escapeHtml(skill)}</p>`).join('')}
                </div>
                <div class="choice">
                    <button class="like edit-vaga">Editar</button>
                </div>
            </div>
        </div>
    `).join('')
    dashboard.querySelectorAll<HTMLButtonElement>('.edit-vaga').forEach((button) => {
        button.addEventListener('click', (event) => {
            event.stopPropagation()
            const vaga = vagas[Number(button.closest<HTMLElement>('.card')?.dataset.index)]
            if (vaga) void editVagaPanel(vaga).catch(mostrarErro)
        })
    })
    pointSkills()
}

export function findVaga(): void {
    document.querySelector('.view-vaga')?.addEventListener('click', () => {
        void findVagaConteudo().catch(mostrarErro)
    })
}
