import { validarCpf, validarEmail } from '../ts/util/validators'
import type { Candidato } from '../ts/models/Candidato'
import type { Recrutador } from '../ts/models/Recrutador'
import type { Vaga } from '../ts/models/Vaga'
import { CandidatoService } from '../ts/services/CandidatoService'
import { EmpresaService } from '../ts/services/EmpresaService'
import { RecrutadorService } from '../ts/services/RecrutadorService'
import { VagaService } from '../ts/services/VagaService'

const recrutadorService = new RecrutadorService(localStorage)
const empresaService = new EmpresaService(localStorage)
const candidatoService = new CandidatoService(localStorage)
const vagaService = new VagaService(localStorage)

function escapar(valor: string): string {
    return valor.replace(/[&<>"']/g, (caractere) => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[caractere]!)
}

export function gerenciarRecrutadores(): void {
    const botao = document.querySelector('.manage-recrutadores')
    botao?.addEventListener('click', () => abrirGestaoRecrutadores())
}

function abrirGestaoRecrutadores(): void {
    const empresa = empresaService.empresaAtual()
    if (!empresa) return

    const painel = document.createElement('div')
    painel.className = 'recrutador-overlay'
    painel.innerHTML = `
        <section class="recrutador-modal" aria-labelledby="recrutador-management-title">
            <button type="button" class="recrutador-close" aria-label="Fechar">×</button>
            <h2 id="recrutador-management-title">Equipe de recrutamento</h2>
            <form class="recrutador-form">
                <label>Nome<input name="nome" required></label>
                <label>Área<input name="area" placeholder="TI, RH, Suporte..." required></label>
                <label>Descrição<textarea name="descricao" required></textarea></label>
                <label>E-mail corporativo<input name="email" type="email" required></label>
                <label>CPF<input name="cpf" placeholder="000.000.000-00" required></label>
                <label>Data de nascimento<input name="dataNascimento" type="date" required></label>
                <label>Senha<input name="senha" type="password" required></label>
                <button type="submit">Cadastrar recrutador</button>
                <p class="recrutador-form-error" role="alert"></p>
            </form>
            <h3>Recrutadores cadastrados</h3>
            <ul class="recrutador-lista-gestao"></ul>
        </section>
    `
    document.body.appendChild(painel)
    const atualizarLista = () => {
        const lista = painel.querySelector<HTMLUListElement>('.recrutador-lista-gestao')!
        lista.innerHTML = recrutadorService.listarPorEmpresa(empresa.cpnj)
            .map((item) => `
                <li>
                    <span>${escapar(item.nome)} · ${escapar(item.area)} · ${escapar(item.email)}
                        ${item.ativo ? '' : ' · Inativo'}</span>
                    ${item.ativo ? `<button type="button" data-id="${item.id}">Desativar</button>` : ''}
                </li>
            `).join('') || '<li>Nenhum recrutador cadastrado.</li>'
        lista.querySelectorAll<HTMLButtonElement>('button[data-id]').forEach((acao) => {
            acao.addEventListener('click', () => {
                recrutadorService.desativar(Number(acao.dataset.id), empresa.cpnj)
                atualizarLista()
            })
        })
    }

    atualizarLista()
    painel.querySelector('.recrutador-close')?.addEventListener('click', () => painel.remove())
    painel.addEventListener('click', (evento) => {
        if (evento.target === painel) painel.remove()
    })
    painel.querySelector<HTMLFormElement>('.recrutador-form')?.addEventListener('submit', (evento) => {
        evento.preventDefault()
        const form = evento.currentTarget as HTMLFormElement
        const dados = new FormData(form)
        const erro = painel.querySelector<HTMLElement>('.recrutador-form-error')!
        const email = String(dados.get('email')).trim()
        const cpf = String(dados.get('cpf')).trim()
        const existentes = recrutadorService.listarPorEmpresa(empresa.cpnj)

        if (!validarEmail(email)) {
            erro.textContent = 'Informe um e-mail válido.'
            return
        }
        if (!validarCpf(cpf)) {
            erro.textContent = 'Informe um CPF válido no formato 000.000.000-00.'
            return
        }
        if (candidatoService.listarCandidatos().some((candidato) => candidato.email === email) ||
            empresaService.listarEmpresas().some((item) => item.email === email) ||
            recrutadorService.listar().some((item) => item.email === email)) {
            erro.textContent = 'Este e-mail já está cadastrado.'
            return
        }
        if (existentes.some((item) => item.cpf === cpf)) {
            erro.textContent = 'Este CPF já está cadastrado.'
            return
        }

        try {
            recrutadorService.cadastrar({
                empresaCnpj: empresa.cpnj,
                nome: String(dados.get('nome')).trim(),
                area: String(dados.get('area')).trim(),
                descricao: String(dados.get('descricao')).trim(),
                email,
                cpf,
                dataNascimento: String(dados.get('dataNascimento')),
                senha: String(dados.get('senha'))
            })
            form.reset()
            erro.textContent = ''
            atualizarLista()
        } catch (falha) {
            erro.textContent = falha instanceof Error ? falha.message : 'Não foi possível cadastrar.'
        }
    })
}

export function inicializarTriagem(
    raiz: HTMLElement,
    recrutador: Recrutador,
    aoSair: () => void
): void {
    raiz.querySelector<HTMLElement>('.recrutador-name')!.textContent = recrutador.nome
    raiz.querySelector<HTMLElement>('.recrutador-area')!.textContent = `Área: ${recrutador.area}`
    raiz.querySelector<HTMLElement>('.recrutador-description')!.textContent = recrutador.descricao

    const lista = raiz.querySelector<HTMLElement>('.recrutador-lista')!
    const vagas = vagaService.listarVagas()
    const renderizarVagas = () => {
        const vagasDaArea = recrutadorService.listarVagasDaArea(recrutador, vagas)
        if (!vagasDaArea.length) {
            lista.innerHTML = '<p class="recrutador-vazio">Não há vagas da sua área nesta empresa.</p>'
            return
        }
        lista.innerHTML = vagasDaArea.map((vaga, indice) => `
            <article class="recrutador-vaga-card">
                <h2>${escapar(vaga.nome)}</h2>
                <p class="recrutador-area-tag">${escapar(vaga.area ?? '')}</p>
                <p>${escapar(vaga.descricao)}</p>
                <div class="recrutador-tags">
                    ${vaga.competencias.map((item) => `<span>${escapar(item)}</span>`).join('')}
                </div>
                <button type="button" data-vaga-index="${indice}">Analisar candidatos</button>
            </article>
        `).join('')
        lista.querySelectorAll<HTMLButtonElement>('[data-vaga-index]').forEach((botao) => {
            botao.addEventListener('click', () => {
                const vaga = vagasDaArea[Number(botao.dataset.vagaIndex)]
                if (vaga) renderizarCandidatos(vaga)
            })
        })
    }

    const renderizarCandidatos = (vaga: Vaga) => {
        const candidatos: Candidato[] = candidatoService.listarCandidatos()
        const anonimos = recrutadorService.listarCandidatosAnonimos(recrutador, vaga, candidatos)
        lista.innerHTML = `
            <button type="button" class="recrutador-voltar">Voltar às vagas</button>
            <h2>${escapar(vaga.nome)} · Candidatos anônimos</h2>
            ${anonimos.length ? anonimos.map((perfil, indice) => `
                <article class="recrutador-candidato-card">
                    <h3>Perfil anônimo</h3>
                    <p>${escapar(perfil.descricao)}</p>
                    ${perfil.formacoes.length ? `
                        <p><strong>Formação:</strong> ${perfil.formacoes.map(escapar).join(', ')}</p>
                    ` : ''}
                    <div class="recrutador-tags">
                        ${perfil.competencias.map((item) => `<span>${escapar(item)}</span>`).join('')}
                    </div>
                    <div class="recrutador-acoes">
                        <button type="button" data-avaliacao="${indice}" data-like="false">Passar</button>
                        <button type="button" data-avaliacao="${indice}" data-like="true">Curtir</button>
                    </div>
                </article>
            `).join('') : '<p class="recrutador-vazio">Todos os perfis disponíveis já foram avaliados.</p>'}
        `
        lista.querySelector('.recrutador-voltar')?.addEventListener('click', renderizarVagas)
        lista.querySelectorAll<HTMLButtonElement>('[data-avaliacao]').forEach((botao) => {
            botao.addEventListener('click', () => {
                const anonimo = anonimos[Number(botao.dataset.avaliacao)]
                if (!anonimo) return
                const inserida = recrutadorService.registrarAvaliacao(
                    recrutador, anonimo.id, vaga, botao.dataset.like === 'true'
                )
                if (!inserida) {
                    window.alert('Já existe uma avaliação registrada para este candidato e esta vaga.')
                }
                renderizarCandidatos(vaga)
            })
        })
    }

    const renderizarHistorico = () => {
        const historico = recrutadorService.listarHistorico(recrutador, vagas)
        lista.innerHTML = `
            <h2>Histórico da equipe</h2>
            ${historico.length ? historico.map((avaliacao) => `
                <article class="recrutador-historico-card">
                    <strong>${escapar(
                        recrutadorService.listar().find((item) => item.id === avaliacao.recrutadorId)?.nome ??
                            'Recrutador indisponível'
                    )}</strong>
                    <span>${avaliacao.curtiu ? 'Curtiu' : 'Passou'} · Vaga #${avaliacao.vagaId}</span>
                    ${avaliacao.recrutadorId === recrutador.id ? '' : '<span>Outro recrutador da equipe</span>'}
                </article>
            `).join('') : '<p class="recrutador-vazio">Ainda não há avaliações registradas.</p>'}
        `
    }

    raiz.querySelector('.recrutador-vagas')?.addEventListener('click', renderizarVagas)
    raiz.querySelector('.recrutador-historico')?.addEventListener('click', renderizarHistorico)
    raiz.querySelector('.recrutador-sair')?.addEventListener('click', aoSair)
    renderizarVagas()
}

export function matchesRecrutadorParaCandidato(candidato: Candidato): string {
    const matches = recrutadorService.listarMatchesCandidato(
        candidato,
        empresaService.listarEmpresas(),
        vagaService.listarVagas()
    )
    return matches.map((match) => `
        <article class="recrutador-match-card">
            <h3>${escapar(match.nomeVaga)} · ${escapar(match.nomeEmpresa)}</h3>
            <p>${escapar(match.descricaoVaga)}</p>
            <p>${escapar(match.descricaoEmpresa)}</p>
            <p>Recrutador responsável: ${escapar(match.recrutadorNome)}</p>
            <a href="mailto:${escapar(match.recrutadorEmail)}">${escapar(match.recrutadorEmail)}</a>
        </article>
    `).join('')
}
