export const estados = [
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

export const paises = [
    'Selecione o país',
    'Argentina',
    'Brasil',
    'Canadá',
    'China',
    'Coreia do Sul',
    'Cuba',
    'EUA',
    'Japão',
    'Rússia',
    'Suíça'
]

export function signUpEmpresa(): string {
    return `
        <section class="signUp-class">
            <div class="container">
                <div class="signUpEmpresa-content">
                    <div class="title-class">
                        <p class="title">Registro <span>Empresa</span> Linketinder</p>
                        <p class="subtitle">Crie o perfil da sua empresa</p>
                    </div>
                    <form>
                        <div class="input-form">
                            <div class="info-personal">
                                <p class="title-personal">INFORMAÇÃO EMPRESARIAL</p>
                                <div>
                                    <p class="input-name">Nome da empresa</p>
                                    <input class="input-data" type="text" placeholder="nome">
                                </div>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">CNPJ</p>
                                        <input class="input-data" type="text" placeholder="00.000.000/0001-00">
                                    </div>
                                    <div>
                                        <p class="input-name">Email corporativo</p>
                                        <input class="input-data" type="email" placeholder="email@exemplo.com">
                                    </div>
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
                                        <input class="input-data" type="text" placeholder="00000-00">
                                    </div>
                                </div>
                            </div>

                            <div class="info-professional">
                                <p class="title-personal">PERFIL DA EMPRESA</p>
                                <div class="div-row">
                                    <div>
                                        <p class="input-name">Bio / Descrição</p>
                                        <textarea name="" id="" placeholder="Escreva mais sobre a empresa, cultura, valores, etc..."></textarea>
                                    </div>
                                </div>
                                <div>
                                    <p class="input-name">Habilidades requeridas</p>
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
        <!-- </section> -->
    `
}