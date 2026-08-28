const estados = [
    'Selecione o estado',
    'Acre',
    'Alagoas',
    'Amapá',
    'Amazonas',
    'Bahia',
    'Ceará',
    'Distrito Federal',
    'Espírito Santo',
    'Goiás',
    'Maranhão',
    'Mato Grosso',
    'Mato Grosso do Sul',
    'Minas Gerais',
    'Pará',
    'Paraíba',
    'Paraná',
    'Pernambuco',
    'Piauí',
    'Rio de Janeiro',
    'Rio Grande do Norte',
    'Rio Grande do Sul',
    'Rondônia',
    'Roraima',
    'Santa Catarina',
    'São Paulo',
    'Sergipe',
    'Tocantins'
]

const paises = [
    'Selecione o estado',
    'Brasil'
]

export function signUpCandidato(): string {
    return `
        <section class="signUp-class">
            <div class="container">
                <div class="signUpCandidato-content">
                    <div class="title-class">
                        <p class="title">Registro Linketinder</p>
                        <p class="subtitle">Crie seu perfil de conta</p>
                    </div>
                    <form>
                        <div class="input-form">
                            <div class="info-personal">
                                <p class="title-personal">INFORMAÇÃO PESSOAL</p>
                                <div>
                                    <p class="input-name">Nome completo</p>
                                    <input class="input-data" type="text" placeholder="nome">
                                </div>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">CPF</p>
                                        <input class="input-data" type="text" placeholder="000.000.000-00">
                                    </div>
                                    <div>
                                        <p class="input-name">Idade</p>
                                        <input class="input-data" type="number" placeholder="25">
                                    </div>
                                </div>
                                <div>
                                    <p class="input-name">Email</p>
                                    <input class="input-data" type="email" placeholder="email@exemplo.com">
                                </div>
                                <div>
                                    <p class="input-name">Senha</p>
                                    <input class="input-data" type="password" placeholder="senha123">
                                </div>
                            </div>

                            <div class="info-location">
                                <p class="title-personal">LOCALIZAÇÃO</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">País</p>
                                        <select name="" id="">
                                            ${paises.map(e => `<option value="${e}">${e}</option>`).join('')}
                                        </select>
                                    </div>
                                    <div>
                                        <p class="input-name">Estado</p>
                                        <select name="" id="">
                                            ${estados.map(e => `<option value="${e}">${e}</option>`).join('')}
                                        </select>
                                    </div>
                                    <div>
                                        <p class="input-name">CEP</p>
                                        <input class="input-data" type="number" placeholder="00000-00">
                                    </div>
                                </div>
                            </div>

                            <div class="info-professional">
                                <p class="title-personal">PERFIL PROFISSIONAL</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">Bio / Descrição</p>
                                        <textarea name="" id="" placeholder="Escreva mais sobre você, expericências, etc..."></textarea>
                                    </div>
                                </div>
                                <div>
                                    <p class="input-name">Habilidades</p>
                                    <div class="skills-class">
                                    </div>
                                    <input id="input-skill" class="input-data" type="text" placeholder="Digite e aperte Enter">
                                </div>
                            </div>
                            <div class="button-class">
                                <button class="cancel" type="submit">Cancelar</button>
                                <button class="create" type="submit">Criar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    `
}