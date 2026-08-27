export function signUp(): string {
    return `
        <main class="main-content">
            <section class="container">
                <div class="signUp-content">
                    <div class="form-content">
                        <h2>Cadastro de usuario</h2>
                        <form action="">
                            <div class="input-data">
                                <div>
                                    <p>Nome:</p>
                                    <input class="input-up" type="text" placeholder="nome">
                                </div>
                                <div>
                                    <p>Email:</p>
                                    <input class="input-up" type="email" placeholder="@email.com">
                                </div>
                                <div>
                                    <p>Senha:</p>
                                    <input class="input-up" type="password" placeholder="senha">
                                </div>
                            </div>
                            <button>Cadastrar</button>
                        </form>
                        <a class="signIn-link" href="./signIn.html">Entrar</a>
                    </div>
                </div>
            </section>
        </main>
    `
}