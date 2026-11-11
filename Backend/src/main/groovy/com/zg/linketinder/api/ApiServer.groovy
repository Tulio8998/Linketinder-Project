package com.zg.linketinder.api

import com.zg.linketinder.controller.ServeletController.CandidatoController
import com.zg.linketinder.controller.ServeletController.CompetenciaController
import com.zg.linketinder.controller.ServeletController.CurtidaController
import com.zg.linketinder.controller.ServeletController.EmpresaController
import com.zg.linketinder.controller.ServeletController.VagaController
import com.zg.linketinder.controller.ServeletController.RecrutadorController
import org.apache.catalina.Context
import org.apache.catalina.startup.Tomcat

import java.nio.file.Paths

class ApiServer {

    static void main(String[] args) {
        int porta = 8080

        Tomcat tomcat = new Tomcat()
        tomcat.setPort(porta)
        tomcat.getConnector()

        Context ctx = tomcat.addContext(
                '',
                Paths.get('.').toAbsolutePath().toString()
        )

        register(ctx, 'candidatos', new CandidatoController(), '/api/candidatos/*')
        register(ctx, 'empresas', new EmpresaController(), '/api/empresas/*')
        register(ctx, 'vagas', new VagaController(), '/api/vagas/*')
        register(ctx, 'competencias', new CompetenciaController(),'/api/competencias/*')
        register(ctx, 'curtidas', new CurtidaController(), '/api/curtidas/*')
        register(ctx, 'recrutadores', new RecrutadorController(), '/api/recrutadores/*')


        tomcat.start()
        println "API REST rodando em http://localhost:${porta}"
        tomcat.getServer().await()
    }

    private static void register(Context ctx, String nome, jakarta.servlet.http.HttpServlet servlet, String path) {
        Tomcat.addServlet(ctx, nome, servlet)
        ctx.addServletMappingDecoded(path, nome)
    }
}