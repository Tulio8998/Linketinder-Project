import { ESTADOS, PAISES } from '../ts/util/constants'
import { viewPanel } from './candidatoDash'
import { escapeHtml } from '../ts/util/html'

export { viewPanel }

function renderizarOpcoes(colecao: string[]): string {
    return colecao.map((item) => `<option value="${escapeHtml(item)}">${escapeHtml(item)}</option>`).join('')
}

function templateEditavel(titulo: string, subtitulo: string, dados: {
    tituloPerfil: string
    botoes: 'salvar-deletar' | 'cancelar-salvar'
}): string {
    const botoes = dados.botoes === 'salvar-deletar'
        ? `<button class="btn-cancel" type="button">Cancelar</button>
           <button class="btn-delete" type="button">Excluir vaga</button>
           <button class="btn-create" type="submit">Salvar alterações</button>`
        : `<button class="cancel" type="button">Cancelar</button>
           <button class="create" type="submit">Publicar vaga</button>`

    return `
        <section class="edit-class">
            <div class="container">
                <div class="editVaga-content profile-form-card">
                    <header class="title-class">
                        <p class="eyebrow">Gestão de oportunidades</p>
                        <h1 class="title">${escapeHtml(titulo)}</h1>
                        <p class="subtitle">${escapeHtml(subtitulo)}</p>
                    </header>
                    <form>
                        <div class="input-form">
                            <section class="info-personal form-section">
                                <h2 class="title-personal">${escapeHtml(dados.tituloPerfil)}</h2>
                                <div>
                                    <label class="input-name">Título da vaga</label>
                                    <input class="input-data" data-field="nome" type="text" placeholder="Ex.: Pessoa desenvolvedora" required>
                                </div>
                                <div>
                                    <label class="input-name">Área de atuação</label>
                                    <input class="input-data" data-field="area" type="text" placeholder="Ex.: Tecnologia" required>
                                </div>
                            </section>
                            <section class="info-location form-section">
                                <h2 class="title-personal">Localização</h2>
                                <div class="div-row">
                                    <div><label class="input-name">País</label><select>${renderizarOpcoes(PAISES)}</select></div>
                                    <div><label class="input-name">Estado</label><select>${renderizarOpcoes(ESTADOS)}</select></div>
                                    <div><label class="input-name">Cidade</label><input class="input-data" data-field="cidade" type="text" placeholder="Cidade" required></div>
                                </div>
                            </section>
                            <section class="info-professional form-section">
                                <h2 class="title-personal">Sobre a oportunidade</h2>
                                <div><label class="input-name">Descrição</label><textarea placeholder="Responsabilidades, desafios e contexto da vaga..." required></textarea></div>
                                <div>
                                    <label class="input-name" for="input-skill">Competências desejadas</label>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Busque uma competência e pressione Enter">
                                </div>
                            </section>
                            <div class="button-class">${botoes}</div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    `
}

export function addVaga(): string {
    return templateEditavel('Nova vaga', 'Apresente uma oportunidade para novos talentos.', {
        tituloPerfil: 'Informações da vaga',
        botoes: 'cancelar-salvar'
    })
}

export function editVaga(): string {
    return templateEditavel('Editar vaga', 'Mantenha os detalhes da oportunidade atualizados.', {
        tituloPerfil: 'Informações da vaga',
        botoes: 'salvar-deletar'
    })
}

export function editEmpProfile(): string {
    return `
        <section class="edit-class">
            <div class="container">
                <div class="editempresa-content profile-form-card">
                    <header class="title-class">
                        <p class="eyebrow">Configurações da organização</p>
                        <h1 class="title">Editar perfil da empresa</h1>
                        <p class="subtitle">Mantenha as informações institucionais atualizadas.</p>
                    </header>
                    <form>
                        <div class="input-form">
                            <section class="info-personal form-section">
                                <h2 class="title-personal">Informações da empresa</h2>
                                <div>
                                    <label class="input-name">Nome da empresa</label>
                                    <input class="input-data" data-field="nome" type="text" required>
                                </div>
                                <div>
                                    <label class="input-name">CNPJ</label>
                                    <input class="input-data" data-field="cnpj" type="text" required>
                                </div>
                            </section>
                            <section class="info-location form-section">
                                <h2 class="title-personal">Localização</h2>
                                <div class="div-row">
                                    <div><label class="input-name">País</label><select>${renderizarOpcoes(PAISES)}</select></div>
                                    <div><label class="input-name">Estado</label><select>${renderizarOpcoes(ESTADOS)}</select></div>
                                </div>
                                <div class="div-row">
                                    <div><label class="input-name">Cidade</label><input class="input-data" data-field="cidade" type="text" required></div>
                                    <div><label class="input-name">CEP</label><input class="input-data" data-field="cep" type="text" required></div>
                                </div>
                            </section>
                            <section class="info-professional form-section">
                                <h2 class="title-personal">Sobre a empresa</h2>
                                <div><label class="input-name">Descrição</label><textarea placeholder="Conte sobre a cultura e a atuação da empresa..." required></textarea></div>
                                <div>
                                    <label class="input-name" for="input-skill">Competências valorizadas</label>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Busque uma competência e pressione Enter">
                                </div>
                            </section>
                            <div class="button-class">
                                <button class="btn-cancel" type="button">Cancelar</button>
                                <button class="btn-delete" type="button">Excluir conta</button>
                                <button class="btn-create" type="submit">Salvar alterações</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    `
}

export function deleteEmpresaProfile(): string {
    return `
        <section class="delete-class">
            <div class="container">
                <div class="deleteEmpresa-content delete-dialog">
                    <p class="eyebrow">Atenção</p>
                    <h2 class="delete-title">Desativar conta da empresa?</h2>
                    <p>Esta ação não poderá ser desfeita.</p>
                    <form>
                        <button class="cancel-delete" type="button">Manter conta</button>
                        <button class="confirm-delete" type="button">Desativar</button>
                    </form>
                </div>
            </div>
        </section>
    `
}

export function viewMatch(): string { return '' }
export function viewVagaEmp(): string { return '' }

export function empresaDash(): string {
    return `
        <section class="empresaDash-class">
            <aside class="empresa-profile">
                <a class="dashboard-brand" href="#"><span class="brand-mark">LT</span><span>Linketinder</span></a>
                <div class="profile">
                    <div class="profile-content">
                        <p class="name"></p>
                        <div><p>Estado</p><p class="location-separator">·</p><p>País</p></div>
                    </div>
                    <div class="sidebar-section about">
                        <p class="about-title">Sobre a empresa</p>
                        <p></p>
                    </div>
                    <div class="sidebar-section skills">
                        <p class="skill-title">Competências das vagas</p>
                        <div></div>
                    </div>
                    <nav class="nav-dash" aria-label="Menu da empresa">
                        <p class="nav-title">Gestão</p>
                        <button class="find-cand" type="button">Recrutadores</button>
                        <button class="match-cand" type="button">Matches</button>
                        <button class="add-vaga" type="button">Adicionar vaga</button>
                        <button class="view-vaga" type="button">Ver vagas</button>
                    </nav>
                    <div class="log-out">
                        <button class="btn-out" type="button">Sair da conta</button>
                        <button class="btn-edit" type="button">Editar perfil</button>
                    </div>
                </div>
            </aside>
            <main class="dash-content">
                <header class="search-bar">
                    <div class="dashboard-heading">
                        <p class="eyebrow">Área da empresa</p>
                        <h1>Encontre talentos para sua equipe</h1>
                    </div>
                    <label class="search-control">
                        <span class="visually-hidden">Pesquisar por competência</span>
                        <input class="input-search" type="search" placeholder="Pesquise por competência">
                    </label>
                </header>
                <section class="graph-class">
                    <div class="graph-heading">
                        <p class="eyebrow">Visão geral</p>
                        <h2 class="graph-title">Vagas por área</h2>
                    </div>
                    <div class="graph" id="graph"></div>
                </section>
                <div class="match-cards"></div>
            </main>
        </section>
    `
}
