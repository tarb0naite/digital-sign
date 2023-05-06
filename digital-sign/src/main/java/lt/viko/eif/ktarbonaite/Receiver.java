package lt.viko.eif.ktarbonaite;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.*;

public class Receiver extends JFrame {
    private JTextArea resultArea;
    public Receiver(){
        setTitle("Receiver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setSize(250,150);

        setVisible(true);
    }
    public static void receiver() throws Exception {
        Receiver receiver = new Receiver();
        ServerSocket serverSocket = new ServerSocket(8080);
        receiver.resultArea.append("Waiting for connection...\n");

        Socket socket = serverSocket.accept();
        receiver.resultArea.append("Connected to sender.\n");

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        String text = (String) ois.readObject();
        byte[] signature = (byte[]) ois.readObject();
        PublicKey publicKey = (PublicKey) ois.readObject();
        ois.close();
        socket.close();

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(text.getBytes());
        boolean isValid = sig.verify(signature);

        if (isValid) {
            receiver.resultArea.append("The digital signature is valid.\n");
            receiver.resultArea.append("Received message: " + text + "\n");
        } else {
            receiver.resultArea.append("The digital signature is invalid.");
        }
    }

}
