import { ESTADOS, PAISES } from '../ts/util/constants'
import { escapeHtml } from '../ts/util/html'

function renderizarOpcoes(colecao: string[]): string {
    return colecao.map((item) => `<option value="${escapeHtml(item)}">${escapeHtml(item)}</option>`).join('')
}

function renderizarSkills(skills: string[]): string {
    return skills.map((item) => `<p class="skill">${escapeHtml(item)}</p>`).join('')
}

export function viewPanel(
    name: string,
    address: string,
    description: string,
    match: string,
    skills: string[]
): string {
    return `
        <div class="detail-dialog">
            <button class="close-card" type="button" aria-label="Fechar detalhes">×</button>
            <div class="match-cards">
                <article class="card">
                    <div class="info-job">
                        ${match ? `<p class="porcent-match">${escapeHtml(match)}</p>` : ''}
                        ${name ? `<h2 class="name">${escapeHtml(name)}</h2>` : ''}
                        <p class="address">${escapeHtml(address)}</p>
                        <p class="description">${escapeHtml(description)}</p>
                        <div class="skills-class">${renderizarSkills(skills)}</div>
                    </div>
                    <div class="choice">
                        <button class="pass" type="button">Passar</button>
                        <button class="like" type="button">Curtir</button>
                    </div>
                </article>
            </div>
        </div>
    `
}

export function viewVagaCand(
    name: string,
    address: string,
    description: string,
    match: string,
    skills: string[]
): string {
    return viewPanel(name, address, description, match, skills)
}

export function editProfile(): string {
    return `
        <section class="edit-class">
            <div class="container">
                <div class="editCandidato-content profile-form-card">
                    <header class="title-class">
                        <p class="eyebrow">Configurações da conta</p>
                        <h1 class="title">Editar perfil</h1>
                        <p class="subtitle">Atualize suas informações e competências.</p>
                    </header>
                    <form>
                        <div class="input-form">
                            <section class="info-personal form-section">
                                <h2 class="title-personal">Informações pessoais</h2>
                                <div>
                                    <label class="input-name">Nome completo</label>
                                    <input class="input-data" data-field="nome" type="text" required>
                                </div>
                                <div class="div-row">
                                    <div>
                                        <label class="input-name">CPF</label>
                                        <input class="input-data" data-field="cpf" type="text" required>
                                    </div>
                                    <div>
                                        <label class="input-name">Data de nascimento</label>
                                        <input class="input-data" data-field="dataNascimento" type="date" required>
                                    </div>
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
                                <h2 class="title-personal">Perfil profissional</h2>
                                <div><label class="input-name">Sobre você</label><textarea placeholder="Conte sobre sua trajetória..." required></textarea></div>
                                <div>
                                    <label class="input-name" for="input-skill">Competências</label>
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

export function deleteCandidatoProfile(): string {
    return `
        <section class="delete-class">
            <div class="container">
                <div class="deleteCandidato-content delete-dialog">
                    <p class="eyebrow">Atenção</p>
                    <h2 class="delete-title">Desativar sua conta?</h2>
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

export function candidatoDash(): string {
    return `
        <section class="candidatoDash-class">
            <aside class="candidato-profile">
                <a class="dashboard-brand" href="#"><span class="brand-mark">LT</span><span>Linketinder</span></a>
                <div class="profile">
                    <div class="profile-content">
                        <p class="name"></p>
                        <div><p>Estado</p><p class="location-separator">·</p><p>País</p></div>
                    </div>
                    <div class="sidebar-section about">
                        <p class="about-title">Sobre mim</p>
                        <p></p>
                    </div>
                    <div class="sidebar-section skills">
                        <p class="skill-title">Competências</p>
                        <div></div>
                    </div>
                    <nav class="nav-dash" aria-label="Menu do candidato">
                        <p class="nav-title">Seu espaço</p>
                        <button class="find-vaga" type="button">Encontrar oportunidades</button>
                        <button class="match-cand" type="button">Meus matches</button>
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
                        <p class="eyebrow">Área do candidato</p>
                        <h1>Descubra sua próxima oportunidade</h1>
                    </div>
                    <label class="search-control">
                        <span class="visually-hidden">Pesquisar vagas</span>
                        <input class="input-search" type="search" placeholder="Pesquise vagas">
                    </label>
                </header>
                <div class="match-cards"></div>
            </main>
        </section>
    `
}
