package lt.viko.eif.ktarbonaite;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sender extends JFrame{

    private JTextField messageField;
    private JTextArea messageArea;

    public Sender(){
        setTitle("Sender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2,2));
        inputPanel.setPreferredSize(new Dimension(250,70));

        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        inputPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        buttonPanel.add(sendButton);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(inputPanel,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public void sendMessage(){

        String text = messageArea.getText();
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(text.getBytes());
            byte[] signature = sig.sign();

            Socket socket = new Socket("localhost", 8080);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(text);
            oos.writeObject(signature);
            oos.writeObject(publicKey);
            oos.close();
            socket.close();

            JOptionPane.showMessageDialog(this, "Message sent, check receiver");
            messageArea.setText("");
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error sending message, check code u stupid");
        }
    }
}