package com.eurofarma.euroforma.storage;

import com.eurofarma.euroforma.common.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path raiz;

    public LocalStorageService(@Value("${euroforma.storage.local-dir}") String diretorio) {
        this.raiz = Path.of(diretorio).toAbsolutePath().normalize();
        try {
            Files.createDirectories(raiz);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar o diretório de armazenamento: " + raiz, e);
        }
    }

    @Override
    public String armazenar(MultipartFile arquivo, String prefixo) {
        try {
            String extensao = extrairExtensao(arquivo.getOriginalFilename());
            String nomeArmazenado = prefixo + "-" + UUID.randomUUID() + extensao;
            Path destino = raiz.resolve(nomeArmazenado).normalize();
            if (!destino.getParent().equals(raiz)) {
                throw ApiException.requisicaoInvalida("Nome de arquivo inválido");
            }
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nomeArmazenado;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao armazenar arquivo", e);
        }
    }

    @Override
    public Resource carregar(String path) {
        try {
            Path arquivo = raiz.resolve(path).normalize();
            if (!arquivo.getParent().equals(raiz)) {
                throw ApiException.requisicaoInvalida("Caminho de arquivo inválido");
            }
            Resource resource = new UrlResource(arquivo.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw ApiException.naoEncontrado("Arquivo não encontrado");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw ApiException.naoEncontrado("Arquivo não encontrado");
        }
    }

    private String extrairExtensao(String nomeOriginal) {
        if (nomeOriginal == null || !nomeOriginal.contains(".")) {
            return "";
        }
        return nomeOriginal.substring(nomeOriginal.lastIndexOf('.'));
    }
}
