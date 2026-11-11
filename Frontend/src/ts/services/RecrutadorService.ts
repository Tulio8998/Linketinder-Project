import type {
    AvaliacaoRecrutador,
    CandidatoAnonimo,
    MatchRecrutador,
    Recrutador
} from '../models/Recrutador'
import type { Candidato } from '../models/Candidato'
import type { Empresa } from '../models/Empresa'
import type { Vaga } from '../models/Vaga'

const RECRUTADORES_KEY = 'recrutadores_db'
const AVALIACOES_KEY = 'avaliacoes_recrutador_db'
const RECRUTADOR_ATUAL_KEY = 'recrutador_atual'

export class RecrutadorService {
    private readonly storage: Storage

    constructor(storage: Storage) {
        this.storage = storage
    }

    listar(): Recrutador[] {
        return this.ler<Recrutador[]>(RECRUTADORES_KEY, [])
    }

    listarPorEmpresa(empresaCnpj: string): Recrutador[] {
        return this.listar().filter((recrutador) => recrutador.empresaCnpj === empresaCnpj)
    }

    cadastrar(dados: Omit<Recrutador, 'id' | 'ativo'>): Recrutador {
        const recrutadores = this.listar()
        if (recrutadores.some((item) => item.email.toLowerCase() === dados.email.toLowerCase())) {
            throw new Error('Este e-mail já está cadastrado.')
        }
        if (recrutadores.some((item) => item.cpf === dados.cpf)) {
            throw new Error('Este CPF já está cadastrado.')
        }

        const recrutador: Recrutador = {
            ...dados,
            id: recrutadores.reduce((maior, item) => Math.max(maior, item.id), 0) + 1,
            email: dados.email.trim().toLowerCase(),
            ativo: true
        }
        this.storage.setItem(RECRUTADORES_KEY, JSON.stringify([...recrutadores, recrutador]))
        return recrutador
    }

    desativar(id: number, empresaCnpj: string): void {
        const recrutadores = this.listar()
        const recrutador = recrutadores.find(
            (item) => item.id === id && item.empresaCnpj === empresaCnpj
        )
        if (!recrutador || !recrutador.ativo) throw new Error('Recrutador não encontrado ou inativo.')
        recrutador.ativo = false
        this.storage.setItem(RECRUTADORES_KEY, JSON.stringify(recrutadores))
    }

    autenticar(email: string, senha: string): Recrutador | null {
        const recrutador = this.listar().find(
            (item) => item.email.toLowerCase() === email.trim().toLowerCase() && item.senha === senha
        )
        return recrutador?.ativo ? recrutador : null
    }

    estaAtivo(id: number): boolean {
        return this.listar().some((recrutador) => recrutador.id === id && recrutador.ativo)
    }

    recrutadorAtual(): Recrutador | null {
        return this.ler<Recrutador | null>(RECRUTADOR_ATUAL_KEY, null)
    }

    salvarSessao(recrutador: Recrutador): void {
        this.storage.setItem(RECRUTADOR_ATUAL_KEY, JSON.stringify(recrutador))
    }

    limparSessao(): void {
        this.storage.removeItem(RECRUTADOR_ATUAL_KEY)
    }

    listarVagasDaArea(recrutador: Recrutador, vagas: Vaga[]): Vaga[] {
        if (!this.estaAtivo(recrutador.id)) return []
        return vagas.filter(
            (vaga) => vaga.empresa.cpnj === recrutador.empresaCnpj &&
                vaga.area?.trim().toLocaleLowerCase() === recrutador.area.trim().toLocaleLowerCase()
        )
    }

    listarCandidatosAnonimos(
        recrutador: Recrutador,
        vaga: Vaga,
        candidatos: Candidato[]
    ): CandidatoAnonimo[] {
        const avaliados = new Set(
            this.listarAvaliacoes().filter((item) =>
                item.empresaCnpj === recrutador.empresaCnpj && item.vagaId === vaga.id
            ).map((item) => item.candidatoId)
        )

        return candidatos
            .filter((candidato) => candidato.id !== undefined && !avaliados.has(candidato.id))
            .map((candidato) => ({
                id: candidato.id!,
                descricao: candidato.descricao,
                competencias: [...candidato.competencias],
                formacoes: [...(candidato.formacoes ?? [])]
            }))
    }

    registrarAvaliacao(
        recrutador: Recrutador,
        candidatoId: number,
        vaga: Vaga,
        curtiu: boolean
    ): boolean {
        if (!this.estaAtivo(recrutador.id)) throw new Error('Recrutador inativo não pode avaliar candidatos.')
        if (!vaga.id) throw new Error('A vaga ainda não possui identificador.')
        const avaliacoes = this.listarAvaliacoes()
        const existente = avaliacoes.some((item) =>
            item.empresaCnpj === recrutador.empresaCnpj &&
            item.candidatoId === candidatoId &&
            item.vagaId === vaga.id
        )
        if (existente) return false

        const avaliacao: AvaliacaoRecrutador = {
            id: avaliacoes.reduce((maior, item) => Math.max(maior, item.id), 0) + 1,
            candidatoId,
            vagaId: vaga.id,
            empresaCnpj: recrutador.empresaCnpj,
            recrutadorId: recrutador.id,
            curtiu,
            createdAt: new Date().toISOString()
        }
        this.storage.setItem(AVALIACOES_KEY, JSON.stringify([...avaliacoes, avaliacao]))
        return true
    }

    listarHistorico(recrutador: Recrutador, vagas: Vaga[]): AvaliacaoRecrutador[] {
        const vagaIds = new Set(this.listarVagasDaArea(recrutador, vagas).map((vaga) => vaga.id))
        return this.listarAvaliacoes().filter(
            (item) => item.empresaCnpj === recrutador.empresaCnpj && vagaIds.has(item.vagaId)
        )
    }

    listarMatchesCandidato(
        candidato: Candidato,
        empresas: Empresa[],
        vagas: Vaga[]
    ): MatchRecrutador[] {
        if (candidato.id === undefined) return []
        const curtidas = this.ler<Array<{ candidato: Candidato; like: boolean; vaga: Vaga }>>(
            'curtidas_db',
            []
        )
        const curtidasPositivas = new Set(
            curtidas.filter((item) =>
                item.like && (item.candidato.id ?? -1) === candidato.id
            ).map((item) => item.vaga.id).filter((id): id is number => id !== undefined)
        )
        const listaVagas = new Map(vagas.map((vaga) => [vaga.id, vaga]))
        const listaEmpresas = new Map(empresas.map((empresa) => [empresa.cpnj, empresa]))

        return this.listarAvaliacoes()
            .filter((avaliacao) => avaliacao.candidatoId === candidato.id &&
                avaliacao.curtiu && curtidasPositivas.has(avaliacao.vagaId))
            .flatMap((avaliacao) => {
                const vaga = listaVagas.get(avaliacao.vagaId)
                const empresa = listaEmpresas.get(avaliacao.empresaCnpj)
                const recrutador = this.listar().find(
                    (item) => item.id === avaliacao.recrutadorId
                )
                if (!vaga || !empresa || !recrutador) return []
                return [{
                    candidatoId: candidato.id!,
                    vagaId: avaliacao.vagaId,
                    nomeVaga: vaga.nome,
                    descricaoVaga: vaga.descricao,
                    competenciasVaga: [...vaga.competencias],
                    nomeEmpresa: empresa.nome,
                    descricaoEmpresa: empresa.descricao,
                    recrutadorNome: recrutador.nome,
                    recrutadorEmail: recrutador.email
                }]
            })
    }

    private listarAvaliacoes(): AvaliacaoRecrutador[] {
        return this.ler<AvaliacaoRecrutador[]>(AVALIACOES_KEY, [])
    }

    private ler<T>(chave: string, padrao: T): T {
        const dados = this.storage.getItem(chave)
        return dados ? JSON.parse(dados) as T : padrao
    }
}
