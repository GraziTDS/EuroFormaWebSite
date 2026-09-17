package com.eurofarma.euroforma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * Serve o build do Angular a partir de src/main/resources/static e, para qualquer caminho que
 * não corresponda a um arquivo real ali (ex.: /educando/home, resultado de uma rota do Angular
 * Router acessada direto pela URL), devolve o index.html para o roteador do Angular assumir.
 * Como o Spring resolve os controllers de /api/** antes dos resource handlers, isso nunca
 * interfere nas rotas da API.
 */
@Configuration
public class SpaController implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requested = location.createRelative(resourcePath);
                        if (requested.exists() && requested.isReadable()) {
                            return requested;
                        }
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}
