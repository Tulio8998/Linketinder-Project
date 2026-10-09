package com.zg.linketinder.factory

import groovy.sql.Sql

interface DatabaseFactory {
    Sql criarConexao()
}
