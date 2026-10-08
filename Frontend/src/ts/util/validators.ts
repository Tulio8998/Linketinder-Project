export const REGEX = {
    email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    cpf: /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
    cnpj: /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/,
    cep: /^\d{5}-\d{3}$/
} as const

export const validarEmail = (valor: string): boolean => REGEX.email.test(valor)

export const validarCpf = (valor: string): boolean => REGEX.cpf.test(valor)

export const validarCnpj = (valor: string): boolean => REGEX.cnpj.test(valor)

export const validarCep = (valor: string): boolean => REGEX.cep.test(valor)