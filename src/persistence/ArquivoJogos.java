package src.persistence;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import src.model.ConsoleGame;
import src.model.Game;
import src.model.PCGame;

public class ArquivoJogos {

    public static final String CAMINHO_PADRAO = "dados/jogos.txt";

    public void salvar(List<Game> jogos, String caminho) throws IOException {
        if (jogos == null || caminho == null || caminho.trim().isEmpty()) {
            throw new IOException("Não foi possível salvar: informe a lista e o caminho do arquivo.");
        }

        List<String> linhas = new ArrayList<>();
        for (Game jogo : jogos) {
            if (jogo == null) {
                throw new IOException("Não foi possível salvar: a lista contém um jogo nulo.");
            }
            linhas.add(formatarLinha(jogo));
        }

        gravarArquivo(localizarArquivo(caminho.trim()), linhas);
    }

    public List<Game> carregar(String caminho) throws IOException {
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new IOException("Informe o caminho do arquivo.");
        }

        File arquivo = localizarArquivo(caminho.trim());
        if (!arquivo.exists()) {
            throw new IOException("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }

        List<Game> jogos = new ArrayList<>();
        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
        try {
            String linha = leitor.readLine();
            int numeroLinha = 1;
            while (linha != null) {
                if (!linha.trim().isEmpty()) {
                    jogos.add(lerJogo(linha, numeroLinha));
                }
                linha = leitor.readLine();
                numeroLinha++;
            }
        } finally {
            leitor.close();
        }
        return jogos;
    }

    private String formatarLinha(Game jogo) throws IOException {
        String tipo;
        String campo1;
        String campo2;

        if (jogo instanceof PCGame) {
            PCGame pc = (PCGame) jogo;
            tipo = "PC";
            campo1 = pc.getPlataformaPC();
            campo2 = pc.getRequisitosMinimos();
        } else if (jogo instanceof ConsoleGame) {
            ConsoleGame console = (ConsoleGame) jogo;
            tipo = "CONSOLE";
            campo1 = console.getNomeConsole();
            campo2 = String.valueOf(console.isMidiaFisica());
        } else {
            throw new IOException("Não foi possível salvar um tipo de jogo desconhecido.");
        }

        return tipo + ";" + jogo.getId()
                + ";" + textoSimples(jogo.getTitulo())
                + ";" + textoSimples(jogo.getGenero())
                + ";" + jogo.getHorasJogadas()
                + ";" + jogo.getAvaliacao()
                + ";" + jogo.isZerado()
                + ";" + textoSimples(campo1)
                + ";" + textoSimples(campo2);
    }

    private String textoSimples(String valor) throws IOException {
        if (valor == null || valor.indexOf(';') >= 0) {
            throw new IOException("Os textos do jogo não podem conter ponto e vírgula.");
        }
        return valor;
    }

    private Game lerJogo(String linha, int numeroLinha) throws IOException {
        String[] campos = linha.split(";");
        if (campos.length != 9) {
            throw new IOException(
                    "A linha " + numeroLinha + " do arquivo está incompleta ou incorreta."
            );
        }

        try {
            int id = Integer.parseInt(campos[1].trim());
            String titulo = campos[2];
            String genero = campos[3];
            double horas = Double.parseDouble(campos[4].trim());
            double nota = Double.parseDouble(campos[5].trim());
            boolean zerado = lerBooleano(campos[6], numeroLinha);
            String tipo = campos[0].trim();

            if (tipo.equalsIgnoreCase("PC")) {
                return new PCGame(
                        id, titulo, genero, horas, nota, zerado, campos[7], campos[8]
                );
            }
            if (tipo.equalsIgnoreCase("CONSOLE") || tipo.equalsIgnoreCase("Console")) {
                return new ConsoleGame(
                        id, titulo, genero, horas, nota, zerado,
                        campos[7], lerBooleano(campos[8], numeroLinha)
                );
            }
            throw new IOException(
                    "A linha " + numeroLinha + " tem um tipo desconhecido. Use PC ou CONSOLE."
            );
        } catch (NumberFormatException erro) {
            throw new IOException("A linha " + numeroLinha + " contém um número inválido.");
        } catch (IllegalArgumentException erro) {
            throw new IOException(
                    "A linha " + numeroLinha + " está inválida: " + erro.getMessage()
            );
        }
    }

    private boolean lerBooleano(String valor, int numeroLinha) throws IOException {
        if (valor != null && valor.trim().equalsIgnoreCase("true")) {
            return true;
        }
        if (valor != null && valor.trim().equalsIgnoreCase("false")) {
            return false;
        }
        throw new IOException(
                "A linha " + numeroLinha + " deve usar true ou false."
        );
    }

    private File localizarArquivo(String caminho) {
        File informado = new File(caminho);
        if (informado.isAbsolute() || informado.exists()) {
            return informado;
        }

        File pasta = new File(System.getProperty("user.dir"));
        for (int nivel = 0; nivel < 4 && pasta != null; nivel++) {
            File candidato = new File(pasta, caminho);
            if (candidato.exists()) {
                return candidato;
            }
            pasta = pasta.getParentFile();
        }
        return informado;
    }

    private void gravarArquivo(File destino, List<String> linhas) throws IOException {
        File temporario = new File(destino.getPath() + ".tmp");
        BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(temporario), "UTF-8"));
        try {
            for (String linha : linhas) {
                escritor.write(linha);
                escritor.newLine();
            }
        } catch (IOException erro) {
            throw new IOException("Não foi possível gravar os jogos. O arquivo original foi mantido.");
        } finally {
            escritor.close();
        }

        if (destino.exists() && !destino.delete()) {
            temporario.delete();
            throw new IOException("Não foi possível substituir o arquivo " + destino.getPath() + ".");
        }
        if (!temporario.renameTo(destino)) {
            throw new IOException("Não foi possível salvar o arquivo " + destino.getPath() + ".");
        }
    }
}
