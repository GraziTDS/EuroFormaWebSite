package com.eurofarma.euroforma.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Armazena o arquivo e retorna o caminho/chave interno usado para recuperá-lo depois.
     */
    String armazenar(MultipartFile arquivo, String prefixo);

    Resource carregar(String path);
}
