export function recrutadorDash(): string {
    return `
        <section class="candidatoDash-class recrutadorDash-class">
            <aside class="candidato-profile">
                <a class="dashboard-brand" href="#"><span class="brand-mark">LT</span><span>Linketinder</span></a>
                <div class="profile">
                    <div class="profile-content">
                        <p class="name"></p>
                        <div><p class="area"></p></div>
                    </div>
                    <div class="sidebar-section about">
                        <p class="about-title">Sobre mim</p>
                        <p class="description"></p>
                    </div>
                    <div class="sidebar-section skills">
                        <p class="skill-title">Área de atuação</p>
                        <div class="recruiter-area"></div>
                    </div>
                    <nav class="nav-dash" aria-label="Menu do recrutador">
                        <p class="nav-title">Triagem</p>
                        <button class="find-vaga" type="button">Vagas da empresa</button>
                        <button class="match-cand" type="button">Histórico de avaliações</button>
                    </nav>
                    <div class="log-out">
                        <button class="btn-out" type="button">Sair da conta</button>
                    </div>
                </div>
            </aside>
            <main class="dash-content">
                <header class="search-bar">
                    <div class="dashboard-heading">
                        <p class="eyebrow">Área do recrutador</p>
                        <h1>Encontre perfis alinhados à sua equipe</h1>
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
