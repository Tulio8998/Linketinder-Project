export function signIn(): string {
    return `
        <main class="main-content auth-page">
            <section class="container auth-layout">
                <aside class="login-brand">
                    <a class="brand-mark" href="#" aria-label="Linketinder">LT</a>
                    <p class="eyebrow">Conexões que movem carreiras</p>
                    <h1>Talentos certos.<br>Oportunidades reais.</h1>
                    <p class="brand-copy">Uma experiência mais próxima e simples para conectar pessoas e empresas.</p>
                    <div class="brand-highlights">
                        <span>01 <b>Descubra</b> novas oportunidades</span>
                        <span>02 <b>Conecte-se</b> com propósito</span>
                    </div>
                </aside>
                <div class="signIn-content">
                    <div class="form-content">
                        <a class="brand-home" href="#">Linketinder</a>
                        <div class="title-class">
                            <h2>Boas-vindas de volta</h2>
                            <p class="subtitle">Entre na sua conta para continuar.</p>
                        </div>
                        <form autocomplete="on">
                            <label class="auth-field">
                                <span>E-mail</span>
                                <input class="input-in" type="email" placeholder="voce@exemplo.com" autocomplete="username" required>
                            </label>
                            <label class="auth-field">
                                <span>Senha</span>
                                <input class="input-in" type="password" placeholder="Sua senha" autocomplete="current-password" required>
                            </label>
                            <button class="primary-button" type="submit">Entrar na plataforma</button>
                        </form>
                        <div class="signup-options">
                            <p>Novo por aqui?</p>
                            <a class="signUp-link link-cand" href="./signUp.html">Criar perfil de candidato</a>
                            <a class="signUp-link link-emp" href="./signUp.html">Cadastrar uma empresa</a>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    `
}
