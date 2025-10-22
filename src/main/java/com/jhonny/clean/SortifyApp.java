package com.jhonny.clean;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class SortifyApp {

    private static final Map<String, String> EXTENSION_MAP = Map.ofEntries(
            Map.entry("jpg", "Imagens"),
            Map.entry("png", "Imagens"),
            Map.entry("jpeg", "Imagens"),
            Map.entry("gif", "Imagens"),
            Map.entry("pdf", "Documentos"),
            Map.entry("docx", "Documentos"),
            Map.entry("txt", "Textos"),
            Map.entry("zip", "Compactados"),
            Map.entry("rar", "Compactados")
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java -jar sortify.jar <caminho_da_pasta>");
            return;
        }

        Path pasta = Paths.get(args[0]);
        if (!Files.exists(pasta) || !Files.isDirectory(pasta)) {
            System.err.println("Erro: caminho inválido -> " + pasta);
            return;
        }

        try {
            Files.list(pasta)
                    .filter(Files::isRegularFile)
                    .forEach(SortifyApp::moverArquivo);
            System.out.println("✅ Organização concluída com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao listar arquivos: " + e.getMessage());
        }
    }

    private static void moverArquivo(Path arquivo) {
        String nome = arquivo.getFileName().toString();
        String extensao = nome.contains(".") ?
                nome.substring(nome.lastIndexOf(".") + 1).toLowerCase() : "outros";

        String categoria = EXTENSION_MAP.getOrDefault(extensao, "Outros");

        Path destinoDir = arquivo.getParent().resolve(categoria);
        Path destino = destinoDir.resolve(nome);

        try {
            if (!Files.exists(destinoDir)) {
                Files.createDirectory(destinoDir);
            }
            Files.move(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📁 " + nome + " → " + categoria);
        } catch (IOException e) {
            System.err.println("Erro ao mover " + nome + ": " + e.getMessage());
        }
    }
}
