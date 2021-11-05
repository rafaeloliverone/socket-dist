package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        // Configurando o socket
        ServerSocket serverSocket = new ServerSocket(7000);
        Socket socket = serverSocket.accept();

        // pegando uma referência do canal de saída do socket. Ao escrever nesse canal, está se enviando dados para o
        // servidor
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // pegando uma referência do canal de entrada do socket. Ao ler deste canal, está se recebendo os dados
        // enviados pelo servidor
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // laço infinito do servidor
        while (true) {
            System.out.println("Cliente: " + socket.getInetAddress());

            String mensagem = dis.readUTF();
            System.out.println(mensagem);

            String[] commandSplited = mensagem.split(" ");

            String command = commandSplited[0];
            String paramFirst = commandSplited[1];

            switch(command) {
                case "readdir":
                    // Exemplo do comando 
                    // readdir C:\Users\rafae\Documents -> Exemplo 
                    String result = "Diretório não encontrado";
                    File directory = new File(paramFirst);
                    System.out.println(directory.exists());

                    if (directory.exists()) {
                        result = "";
                        for (File file : directory.listFiles()) {
                            result += file.getName() + " ";
                        }
                    }
                    dos.writeUTF(result);
                    break;
                case "rename":
                    // Exemplo do comando 
                    // rename C:\Users\rafae\Documents\apps.txt C:\Users\rafae\Documents\apps-passwords.txt 
                    result = "Arquivo nao existe"; 
                    String newNameFile = commandSplited[2];

                    File file = new File(paramFirst);
                    File newFileWithName = new File(newNameFile);

                    if (file.exists()) {
                        file.renameTo(newFileWithName);
                        result = "Arquivo renomeado com sucesso!";
                    }
                    
                    dos.writeUTF(result);
                    break;
                case "create":
                    File newFile = new File(paramFirst);
                    newFile.createNewFile();
                    dos.writeUTF("Arquivo criado com sucesso!");
                    break;
                case "remove":
                    File remove = new File(paramFirst);
                    result = "Não foi encontrado arquivo com esse nome";
                    if (remove.exists()) {
                        remove.delete();
                        result = "Removido com sucesso!";
                    }
                    dos.writeUTF(result);
                    break;
                case "exit": {
                    socket.close();
                }
            }

            dos.writeUTF("Li sua mensagem: ");
        }
        /*
         * Observe o while acima. Perceba que primeiro se lê a mensagem vinda do cliente (linha 29, depois se escreve
         * (linha 32) no canal de saída do socket. Isso ocorre da forma inversa do que ocorre no while do Cliente2,
         * pois, de outra forma, daria deadlock (se ambos quiserem ler da entrada ao mesmo tempo, por exemplo,
         * ninguém evoluiria, já que todos estariam aguardando.
         */
    }
}
