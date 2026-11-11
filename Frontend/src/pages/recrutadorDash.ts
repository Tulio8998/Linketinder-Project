export function recrutadorDash(): string {
    return `
        <section class="recrutadorDash-class">
            <aside class="recrutador-profile">
                <h2>Linketinder</h2>
                <div class="recrutador-identity">
                    <p class="recrutador-name"></p>
                    <p class="recrutador-area"></p>
                    <p class="recrutador-description"></p>
                </div>
                <nav class="recrutador-nav">
                    <button class="recrutador-vagas">Vagas da minha área</button>
                    <button class="recrutador-historico">Histórico de avaliações</button>
                    <button class="recrutador-sair">Sair</button>
                </nav>
            </aside>
            <main class="recrutador-content">
                <header>
                    <h1>Triagem de candidatos</h1>
                    <p>As avaliações são vinculadas à vaga e a primeira decisão permanece no histórico.</p>
                </header>
                <div class="recrutador-lista"></div>
            </main>
        </section>
    `
}
