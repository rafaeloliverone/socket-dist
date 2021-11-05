package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) throws IOException {
        

        System.out.println("== Cliente ==");
        System.out.println("Abaixo comandos disponíveis:");
        System.out.println(ANSI_RED + "Variáveis em amarelo são argumentos que devem ser passados no comando:" + ANSI_RESET);
        System.out.println("==========================================================");
        System.out.println(ANSI_GREEN + "readdir " + ANSI_YELLOW + "PATH" + ANSI_RESET + " -> Listagem arquivos no caminho especificado." + ANSI_RESET);
        System.out.println(ANSI_GREEN + "rename " + ANSI_YELLOW + "nameFile newNameFile" + ANSI_RESET + " -> Renomeia arquivo passando nome atual e novo novo." + ANSI_RESET);
        System.out.println(ANSI_GREEN + "create " + ANSI_YELLOW + "NAME" + ANSI_RESET + " -> Criacao de arquivo com o nome especificado." + ANSI_RESET);
        System.out.println(ANSI_GREEN + "remove " + ANSI_YELLOW + "NAME" + ANSI_RESET + " -> Remocao arquivo com o nome especificado." + ANSI_RESET);
        System.out.println(ANSI_GREEN + "exit " + ANSI_RESET + " -> Sair." + ANSI_RESET);

        // configurando o socket
        Socket socket = new Socket("127.0.0.1", 7000);
        // pegando uma referência do canal de saída do socket. Ao escrever nesse canal, está se enviando dados para o
        // servidor
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // pegando uma referência do canal de entrada do socket. Ao ler deste canal, está se recebendo os dados
        // enviados pelo servidor
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // laço infinito do cliente
        while (true) {
            Scanner teclado = new Scanner(System.in);
            // escrevendo para o servidor

            // lendo o que o servidor enviou
            System.out.println("Escreva");
            
            // Scanner in = new Scanner(socket.getInputStream());
            String command = teclado.nextLine();
            dos.writeUTF(command);
            if (command.compareTo("exit") == 0) {
                socket.close();
                break;
            }

            String mensagem = dis.readUTF();
            System.out.println("Servidor falou: " + mensagem);
        }
        /*
         * Observe o while acima. Perceba que primeiro se escreve para o servidor (linha 27), depois se lê do canal de
         * entrada (linha 30), vindo do servidor. Agora observe o código while do Servidor2. Lá, primeiro se lê,
         * depois se escreve. De outra forma, haveria um deadlock.
         */
    }
}
