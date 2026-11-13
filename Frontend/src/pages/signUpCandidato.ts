import { ESTADOS, PAISES } from '../ts/util/constants'

export const estados = ESTADOS
export const paises = PAISES

export function signUpCandidato(): string {
    return `
        <main class="signUp-class">
            <div class="container">
                <section class="signUpCandidato-content">
                    <header class="title-class">
                        <a class="brand-home" href="#">Linketinder</a>
                        <p class="eyebrow">Seu próximo passo começa aqui</p>
                        <h1 class="title">Crie seu perfil de candidato</h1>
                        <p class="subtitle">Conte um pouco sobre você e mostre suas habilidades.</p>
                    </header>
                    <form>
                        <div class="input-form">
                            <section class="info-personal form-section">
                                <h2 class="title-personal">Informações pessoais</h2>
                                <div>
                                    <label class="input-name" for="candidate-name">Nome completo</label>
                                    <input id="candidate-name" class="input-data" data-field="nome" type="text" placeholder="Seu nome" autocomplete="name" required>
                                </div>
                                <div class="div-row">
                                    <div>
                                        <label class="input-name" for="candidate-cpf">CPF</label>
                                        <input id="candidate-cpf" class="input-data" data-field="cpf" type="text" placeholder="000.000.000-00" required>
                                    </div>
                                    <div>
                                        <label class="input-name" for="candidate-birth">Data de nascimento</label>
                                        <input id="candidate-birth" class="input-data" data-field="dataNascimento" type="date" required>
                                    </div>
                                </div>
                                <div>
                                    <label class="input-name" for="candidate-email">E-mail</label>
                                    <input id="candidate-email" class="input-data" data-field="email" type="email" placeholder="voce@exemplo.com" autocomplete="email" required>
                                </div>
                                <div>
                                    <label class="input-name" for="candidate-password">Senha</label>
                                    <input id="candidate-password" class="input-data" data-field="senha" type="password" placeholder="Crie uma senha" autocomplete="new-password" required>
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
                                        <label class="input-name" for="candidate-city">Cidade</label>
                                        <input id="candidate-city" class="input-data" data-field="cidade" type="text" placeholder="Sua cidade" autocomplete="address-level2" required>
                                    </div>
                                    <div>
                                        <label class="input-name" for="candidate-zip">CEP</label>
                                        <input id="candidate-zip" class="input-data" data-field="cep" type="text" placeholder="00000-000" autocomplete="postal-code" required>
                                    </div>
                                </div>
                            </section>

                            <section class="info-professional form-section">
                                <h2 class="title-personal">Perfil profissional</h2>
                                <div>
                                    <label class="input-name" for="candidate-about">Sobre você</label>
                                    <textarea id="candidate-about" placeholder="Fale sobre sua trajetória, experiência e objetivos..." required></textarea>
                                </div>
                                <div>
                                    <label class="input-name" for="input-skill">Competências</label>
                                    <div class="skills-class"></div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Busque uma competência e pressione Enter">
                                    <small>Escolha somente competências disponíveis no catálogo.</small>
                                </div>
                            </section>
                            <div class="button-class">
                                <button class="cancel" type="button">Voltar</button>
                                <button class="create" type="submit">Criar meu perfil</button>
                            </div>
                        </div>
                    </form>
                </section>
            </div>
        </main>
    `
}
