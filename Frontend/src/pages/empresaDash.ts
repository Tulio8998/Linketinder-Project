import { ESTADOS, PAISES } from '../ts/util/constants'
import { viewPanel } from './candidatoDash'

export { viewPanel }

function renderizarOpcoes(colecao: string[]): string {
    return colecao.map((e) => `<option value="${e}">${e}</option>`).join('')
}

function templateEditavel(titulo: string, subtitulo: string, dados: {
    tituloPerfil: string,
    botoes: 'salvar-deletar' | 'cancelar-salvar'
}): string {
    const botoes = dados.botoes === 'salvar-deletar'
        ? `<button class="btn-cancel" type="submit">Cancelar</button>
           <button class="btn-delete" type="submit">Excluir</button>
           <button class="btn-create" type="submit">Salvar</button>`
        : `<button class="cancel" type="submit">Cancelar</button>
           <button class="create" type="submit">Salvar</button>`

    return `
        <section class="edit-class">
            <div class="container">
                <div class="editVaga-content">
                    <div class="title-class">
                        <p class="title">${titulo}</p>
                        <p class="subtitle">${subtitulo}</p>
                    </div>
                    <form>
                        <div class="input-form">
                            <div class="info-personal">
                                <p class="title-personal">${dados.tituloPerfil}</p>
                                <div>
                                    <p class="input-name">Tipo de vaga</p>
                                    <input class="input-data" type="text" placeholder="Desenvolvedor...">
                                </div>
                            </div>
                            <div class="info-location">
                                <p class="title-personal">LOCALIZAÇÃO</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">País</p>
                                        <select>${renderizarOpcoes(PAISES)}</select>
                                    </div>
                                    <div>
                                        <p class="input-name">Estado</p>
                                        <select>${renderizarOpcoes(ESTADOS)}</select>
                                    </div>
                                    <div>
                                        <p class="input-name">Cidade</p>
                                        <select>${renderizarOpcoes(ESTADOS)}</select>
                                    </div>
                                </div>
                            </div>
                            <div class="info-professional">
                                <p class="title-personal">PERFIL DA EMPRESA</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">Bio / Descrição</p>
                                        <textarea placeholder="Escreva mais sobre a empresa, cultura, valores, etc..."></textarea>
                                    </div>
                                </div>
                                <div>
                                    <p class="input-name">Habilidades requeridas</p>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Digite e aperte Enter">
                                </div>
                            </div>
                            <div class="button-class">${botoes}</div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    `
}

export function addVaga(): string {
    return templateEditavel('Vaga Linketinder', 'Adicione suas vagas', {
        tituloPerfil: 'INFORMAÇÃO VAGA',
        botoes: 'cancelar-salvar'
    })
}

export function editVaga(): string {
    return templateEditavel('Editar Vaga Linketinder', 'Edite sua vaga', {
        tituloPerfil: 'INFORMAÇÃO VAGA',
        botoes: 'salvar-deletar'
    })
}

export function editEmpProfile(): string {
    return `
        <section class="edit-class">
            <div class="container">
                <div class="editempresa-content">
                    <div class="title-class">
                        <p class="title">Edição Linketinder</p>
                        <p class="subtitle">Edite aqui suas informações</p>
                    </div>
                    <form>
                        <div class="input-form">
                            <div class="info-personal">
                                <p class="title-personal">INFORMAÇÃO EMPRESARIAL</p>
                                <div>
                                    <p class="input-name">Nome da empresa</p>
                                    <input class="input-data" type="text" placeholder="nome">
                                </div>
                                <div>
                                    <p class="input-name">CNPJ</p>
                                    <input class="input-data" type="text" placeholder="00.000.000/0001-00">
                                </div>
                            </div>
                            <div class="info-location">
                                <p class="title-personal">LOCALIZAÇÃO</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">País</p>
                                        <select>${renderizarOpcoes(PAISES)}</select>
                                    </div>
                                    <div>
                                        <p class="input-name">Estado</p>
                                        <select>${renderizarOpcoes(ESTADOS)}</select>
                                    </div>
                                    <div>
                                        <p class="input-name">CEP</p>
                                        <input class="input-data" type="text" placeholder="00000-00">
                                    </div>
                                </div>
                            </div>
                            <div class="info-professional">
                                <p class="title-personal">PERFIL DA EMPRESA</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">Bio / Descrição</p>
                                        <textarea placeholder="Escreva mais sobre a empresa, cultura, valores, etc..."></textarea>
                                    </div>
                                </div>
                                <div>
                                    <p class="input-name">Habilidades requeridas</p>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Digite e aperte Enter">
                                </div>
                            </div>
                            <div class="button-class">
                                <button class="btn-cancel" type="submit">Cancelar</button>
                                <button class="btn-delete" type="submit">Excluir</button>
                                <button class="btn-create" type="submit">Salvar</button>
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
                <div class="deleteEmpresa-content">
                    <p class="delete-title">Deseja deletar a conta?</p>
                    <form>
                        <button class="cancel-delete">Não</button>
                        <button class="confirm-delete">Sim</button>
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
            <div class="empresa-profile">
                <h2>Linketinder</h2>
                <div class="profile">
                    <div class="profile-content">
                        <p class="name"></p>
                        <div><p>Estado</p><p>-</p><p>País</p></div>
                    </div>
                    <p class="line"></p>
                    <div class="about">
                        <p class="about-title">Sobre a empresa</p>
                        <p></p>
                    </div>
                    <div class="skills">
                        <p class="skill-title">Habilidades requeridas</p>
                        <div></div>
                    </div>
                    <div class="nav-dash">
                        <p class="nav-title">Dashboard</p>
                        <nav class="find-cand">Encontrar candidatos</nav>
                        <nav class="match-cand">Matches</nav>
                        <nav class="add-vaga">Adicionar Vaga</nav>
                        <nav class="view-vaga">Ver Vagas</nav>
                    </div>
                    <div class="log-out">
                        <p class="line"></p>
                        <div>
                            <button class="btn-out">Sair</button>
                            <button class="btn-edit">Editar</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="dash-content">
                <div class="search-bar">
                    <div><input class="input-search" type="text" placeholder="Pesquise por competência"></div>
                </div>
                <div class="graph-class">
                    <p class="graph-title">Candidatos por competência</p>
                    <div class="graph" id="graph"></div>
                </div>
                <div class="match-cards"></div>
            </div>
        </section>
    `
}