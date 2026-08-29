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

export function viewPanel(name: string, address: string, description: string,
    match: string, skills: string[]): string {
    return `
        <div>
            <button class="close-card">x</button>
            <div class="match-cards">
                <div class="card">
                    <div class="info-job">
                        ${match ? `<p class="porcent-match">${match}</p>` : ''}
                        ${name ? `<p class="name">${name}</p>` : ''}
                        <p class="address">${address}</p>
                        <p class="description">${description}</p>
                        <div>
                            ${skills.map(e => `<p class="skill">${e}</p>`).join('')}
                        </div>
                    </div>

                    <div class="choice">
                        <button class="pass">Passar</button>
                        <button class="like">Curtir</button>
                    </div>
                </div>
            </div>
        </div>
    `
}

export function viewVagaCand(name: string, address: string, description: string,
    match: string, skills: string[]): string {
    return `
        <div>
            <button class="close-card">x</button>
            <div class="match-cards">
                <div class="card">
                    <div class="info-job">
                        <p class="porcent-match">${match}</p>
                        <p class="name">${name}</p>
                        <p class="address">${address}</p>
                        <p class="description">${description}</p>
                        <div>
                            ${skills.map(e => `<p class="skill">${e}</p>`).join('')}
                        </div>
                    </div>

                    <div class="choice">
                        <button class="pass">Passar</button>
                        <button class="like">Curtir</button>
                    </div>
                </div>
            </div>
        </div>
    `
}



export function editProfile(): string {
    return `
        <section class="edit-class">
            <div class="container">
                <div class="editCandidato-content">
                    <div class="title-class">
                        <p class="title">Edição Linketinder</p>
                        <p class="subtitle">Edite aqui suas informações</p>
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
                                <button class="delete" type="submit">Excluir</button>
                                <button class="create" type="submit">Salvar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    `
}

export function viewMatch():string {
    return `
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div class="skills-class">
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="pointers">
                
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem, ipsum dolor sit amet consectetur adipisicing elit. Fugit nulla beatae deleniti tempora qui! Ex, enim nesciunt magnam alias omnis assumenda incidunt praesentium non magni eius pariatur nostrum? Minus, obcaecati.Lorem ipsum dolor sit amet consectetur adipisicing elit. Explicabo vitae labore velit nisi voluptatibus, officiis earum voluptas autem in temporibus modi, quo assumenda quidem dolorem dolore, similique necessitatibus iste consequuntur. Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur. Túlio</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
        <div class="card">
            <div class="info-job">
                <p class="name">Túlio</p>
                <p class="address">Belo horizonte, Minas Gerais</p>
                <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                <div>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                    <p class="skill">Java</p>
                </div>
            </div>
            <div class="choice">
                <button class="pass">Passar</button>
                <button class="like">Curtir</button>
            </div>
        </div>
    `
}

export function candidatoDash(): string {
    return `
        <section class="candidatoDash-class">
            <div class="candidato-profile">
                <h2>Linketinder</h2>
                <div class="profile">
                    <div class="profile-content">
                        <p class="name">Túilo Vilela Lopes</p>
                        <div>
                            <p>Minas Gerais</p>
                            <p>-</p>
                            <p>Brasil</p>
                        </div>
                    </div>
                    <p class="line"></p>
                    <div class="about">
                        <p class="about-title">Sobre mim</p>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Veritatis officia itaque quas quia quaerat, facere vel nam ullam dolorem corporis fugit a facilis. Quo, vitae. Autem consequatur in accusantium dolores?</p>
                    </div>
                    <div class="skills">
                        <p class="skill-title">Habilidades</p>
                        <div>
                            <p class="skill">Java</p>
                            <p class="skill">Java</p>
                            <p class="skill">Java</p>
                            <p class="skill">Java</p>
                            <p class="skill">Java</p>
                        </div>
                    </div>
                    <div class="nav-dash">
                        <p class="nav-title">Dashboard</p>
                        <nav class="find-vaga">
                            Encontrar vagas
                        </nav>
                        <nav class="match-cand">
                            Matches
                        </nav>
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
                    <div>
                        <input class="input-search" type="text" placeholder="Pesquise vagas">
                    </div>
                </div>
                <div class="match-cards">
                    <div class="card">
                        <div class="info-job">
                            <p class="porcent-match">MATCH: 90%</p>
                            <p class="name">Senior UI/UX</p>
                            <p class="address">Belo horizonte, Minas Gerais</p>
                            <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                            <div>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                            </div>
                        </div>
                        <div class="choice">
                            <button class="pass">Passar</button>
                            <button class="like">Curtir</button>
                        </div>
                    </div>
                    <div class="card">
                        <div class="info-job">
                            <p class="porcent-match">MATCH: 90%</p>
                            <p class="name">Senior UI/UX</p>
                            <p class="address">Belo horizonte, Minas Gerais</p>
                            <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                            <div>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                            </div>
                        </div>
                        <div class="pointers"></div>
                        <div class="choice">
                            <button class="pass">Passar</button>
                            <button class="like">Curtir</button>
                        </div>
                    </div>
                    <div class="card">
                        <div class="info-job">
                            <p class="porcent-match">MATCH: 90%</p>
                            <p class="name">Senior UI/UX</p>
                            <p class="address">Belo horizonte, Minas Gerais</p>
                            <p class="description">Lorem, ipsum dolor sit amet consectetur adipisicing elit. Fugit nulla beatae deleniti tempora qui! Ex, enim nesciunt magnam alias omnis assumenda incidunt praesentium non magni eius pariatur nostrum? Minus, obcaecati.Lorem ipsum dolor sit amet consectetur adipisicing elit. Explicabo vitae labore velit nisi voluptatibus, officiis earum voluptas autem in temporibus modi, quo assumenda quidem dolorem dolore, similique necessitatibus iste consequuntur. Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur. Túlio</p>
                            <div>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                            </div>
                        </div>
                        <div class="choice">
                            <button class="pass">Passar</button>
                            <button class="like">Curtir</button>
                        </div>
                    </div>
                    <div class="card">
                        <div class="info-job">
                            <p class="porcent-match">MATCH: 90%</p>
                            <p class="name">Senior UI/UX</p>
                            <p class="address">Belo horizonte, Minas Gerais</p>
                            <p class="description">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quibusdam, architecto aperiam? Illo rem vero libero facilis repudiandae doloremque nulla, sequi consectetur nobis nihil veritatis laudantium! Voluptatum cum fugiat iste tenetur.</p>
                            <div>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                                <p class="skill">Java</p>
                            </div>
                        </div>
                        <div class="pointers"></div>
                        <div class="choice">
                            <button class="pass">Passar</button>
                            <button class="like">Curtir</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    `
}