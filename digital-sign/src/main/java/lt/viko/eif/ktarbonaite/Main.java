package lt.viko.eif.ktarbonaite;

public class Main {
    public static void main(String[] args) {
        Thread receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Receiver.receiver();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Sender sender = new Sender();
                    sender.setVisible(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        receiverThread.start();
        senderThread.start();
    }
}


