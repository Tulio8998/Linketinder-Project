import { ESTADOS, PAISES } from '../ts/util/constants'

export const estados = ESTADOS
export const paises = PAISES

export function signUpEmpresa(): string {
    return `
        <main class="signUp-class">
            <div class="container">
                <section class="signUpEmpresa-content">
                    <header class="title-class">
                        <a class="brand-home" href="#">Linketinder</a>
                        <p class="eyebrow">Encontre pessoas que fazem a diferença</p>
                        <h1 class="title">Apresente sua empresa</h1>
                        <p class="subtitle">Crie um perfil e publique oportunidades com propósito.</p>
                    </header>
                    <form>
                        <div class="input-form">
                            <section class="info-personal form-section">
                                <h2 class="title-personal">Informações da empresa</h2>
                                <div>
                                    <label class="input-name" for="company-name">Nome da empresa</label>
                                    <input id="company-name" class="input-data" data-field="nome" type="text" placeholder="Nome da organização" autocomplete="organization" required>
                                </div>
                                <div class="div-row">
                                    <div>
                                        <label class="input-name" for="company-cnpj">CNPJ</label>
                                        <input id="company-cnpj" class="input-data" data-field="cnpj" type="text" placeholder="00.000.000/0001-00" required>
                                    </div>
                                    <div>
                                        <label class="input-name" for="company-email">E-mail corporativo</label>
                                        <input id="company-email" class="input-data" data-field="email" type="email" placeholder="contato@empresa.com" autocomplete="email" required>
                                    </div>
                                </div>
                                <div>
                                    <label class="input-name" for="company-password">Senha</label>
                                    <input id="company-password" class="input-data" data-field="senha" type="password" placeholder="Crie uma senha" autocomplete="new-password" required>
                                </div>
                            </section>

                            <section class="info-location form-section">
                                <h2 class="title-personal">Localização</h2>
                                <div class="div-row">
                                    <div>
                                        <label class="input-name">País</label>
                                        <select aria-label="País">${paises.map((item) => `<option value="${item}">${item}</option>`).join('')}</select>
                                    </div>
                                    <div>
                                        <label class="input-name">Estado</label>
                                        <select aria-label="Estado">${estados.map((item) => `<option value="${item}">${item}</option>`).join('')}</select>
                                    </div>
                                </div>
                                <div class="div-row">
                                    <div>
                                        <label class="input-name" for="company-city">Cidade</label>
                                        <input id="company-city" class="input-data" data-field="cidade" type="text" placeholder="Cidade" autocomplete="address-level2" required>
                                    </div>
                                    <div>
                                        <label class="input-name" for="company-zip">CEP</label>
                                        <input id="company-zip" class="input-data" data-field="cep" type="text" placeholder="00000-000" autocomplete="postal-code" required>
                                    </div>
                                </div>
                            </section>

                            <section class="info-professional form-section">
                                <h2 class="title-personal">Perfil da empresa</h2>
                                <div>
                                    <label class="input-name" for="company-about">Sobre a empresa</label>
                                    <textarea id="company-about" placeholder="Compartilhe sua cultura, valores e atuação..." required></textarea>
                                </div>
                                <div>
                                    <label class="input-name" for="input-skill">Competências valorizadas</label>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Busque uma competência e pressione Enter">
                                    <small>Escolha somente competências disponíveis no catálogo.</small>
                                </div>
                            </section>
                            <div class="button-class">
                                <button class="cancel" type="button">Voltar</button>
                                <button class="create" type="submit">Criar perfil da empresa</button>
                            </div>
                        </div>
                    </form>
                </section>
            </div>
        </main>
    `
}
