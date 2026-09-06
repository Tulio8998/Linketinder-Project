import type { Candidato } from "./Candidato";
import type { Empresa } from "./Empresa";
import type { Vaga } from "./Vaga";

export interface Curtida {
    like: boolean
    vaga: Vaga
}

export interface CurtidaCandidato extends Curtida{
    candidato: Candidato
}

export interface CurtidaEmpresa extends Curtida {
    empresa: Empresa
    candidato: Candidato
}