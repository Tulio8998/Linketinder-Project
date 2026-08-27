export function signIn(): string {
    return `
        <main class="main-content">
            <section class="container">
                <div class="signIn-content">
                    <div class="form-content">
                        <h2>Login</h2>
                        <form action="">
                            <div class="input-data">
                                <div>
                                    <p>Email:</p>
                                    <input class="input-in" type="email" placeholder="@email.com">
                                </div>
                                <div>
                                    <p>Senha:</p>
                                    <input class="input-in" type="password" placeholder="senha">
                                </div>
                            </div>
                            <button>Entrar</button>
                        </form>
                        <a class="signUp-link" href="./signUp.html">
                            Cadastre-se aqui
                        </a>
                    </div>
                </div>
            </section>
        </main>
    `
}