package socketclient;
import java.net.*;
import java.io.*;
import java.lang.*;

public class SocketClient
{

    private Socket socket;

    public SocketClient(String ip, Integer port)
    {
        try
        {
            //Se crea el socket cliente
            socket = new Socket(ip, port);
            System.out.println(("Conectado"));

            socket.setSoLinger(true, 10);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sentString(String message)
    {
        try
        {
            DataOutputStream bufferOut = new DataOutputStream(socket.getOutputStream());
            DatoSocket aux = new DatoSocket(message);
            aux.writeObject(bufferOut);
            System.out.println("Cliente Java: Enviado " + aux.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void receiveString()
    {
        try {
        //Se obtiene un flujo de datos para recibir datos del servidor.
        DataInputStream bufferIn = new DataInputStream(socket.getInputStream());;
        DatoSocket dato = new DatoSocket("");
        dato.readObject(bufferIn);
        System.out.println("Cliente Java: Recibido " + dato.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}