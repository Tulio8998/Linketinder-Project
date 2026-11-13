export interface ApiRequestOptions extends Omit<RequestInit, 'body'> {
    body?: unknown
    authenticated?: boolean
}

export class ApiError extends Error {
    readonly status: number
    readonly details: unknown

    constructor(message: string, status: number, details?: unknown) {
        super(message)
        this.name = 'ApiError'
        this.status = status
        this.details = details
    }
}

interface BasicCredentials {
    email: string
    password: string
}

let credentials: BasicCredentials | null = null

export function setApiCredentials(email: string, password: string): void {
    credentials = { email, password }
}

export function clearApiCredentials(): void {
    credentials = null
}

export function getApiEmail(): string | null {
    return credentials?.email ?? null
}

export function getApiCredentials(): Readonly<BasicCredentials> | null {
    return credentials
}

function basicAuthorizationHeader(): string | null {
    if (!credentials) return null

    const value = `${credentials.email}:${credentials.password}`
    const bytes = new TextEncoder().encode(value)
    let binary = ''
    bytes.forEach((byte) => {
        binary += String.fromCharCode(byte)
    })
    return `Basic ${btoa(binary)}`
}

function apiBaseUrl(): string {
    return (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/+$/, '')
}

async function responseError(response: Response): Promise<ApiError> {
    const contentType = response.headers.get('content-type') ?? ''
    let details: unknown

    if (contentType.includes('application/json')) {
        try {
            details = await response.json()
        } catch {
            details = undefined
        }
    } else {
        details = await response.text()
    }

    const body = details && typeof details === 'object'
        ? details as Record<string, unknown>
        : null
    const message = typeof body?.mensagem === 'string'
        ? body.mensagem
        : typeof body?.message === 'string'
            ? body.message
            : typeof details === 'string' && details.trim()
                ? details
                : `A API respondeu com HTTP ${response.status}.`

    return new ApiError(message, response.status, details)
}

export async function apiFetch<T>(
    path: string,
    options: ApiRequestOptions = {}
): Promise<T> {
    const { body, authenticated = true, headers: requestHeaders, ...requestOptions } = options
    const headers = new Headers(requestHeaders)
    headers.set('Accept', 'application/json')
    headers.set('Content-Type', 'application/json')

    const authorization = basicAuthorizationHeader()
    if (authorization) {
        headers.set('Authorization', authorization)
    } else if (authenticated) {
        throw new ApiError('Autenticação necessária. Entre novamente.', 401)
    }

    let response: Response
    try {
        response = await fetch(`${apiBaseUrl()}${path.startsWith('/') ? path : `/${path}`}`, {
            ...requestOptions,
            headers,
            body: body === undefined ? undefined : JSON.stringify(body)
        })
    } catch (error) {
        if (error instanceof DOMException && error.name === 'AbortError') throw error
        throw new ApiError(
            'Não foi possível conectar à API. Verifique se o backend está ativo.',
            0,
            error
        )
    }

    if (!response.ok) throw await responseError(response)
    if (response.status === 204) return undefined as T

    const responseText = await response.text()
    if (!responseText) return undefined as T

    try {
        return JSON.parse(responseText) as T
    } catch (error) {
        throw new ApiError('A API retornou uma resposta JSON inválida.', response.status, error)
    }
}
