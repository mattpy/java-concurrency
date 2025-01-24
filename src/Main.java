public class Main {
    public static void main(String[] args) {
        Exchange e = new Exchange();
        Producer p = new Producer(e);
        Consumer c = new Consumer(e);

        new Thread(p).start();
        new Thread(c).start();
    }
}

class Exchange {
    private String message = "";
    private boolean hasMessage = false;

    public synchronized void putMessage(String message) {
        while (hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.message = message;
        hasMessage = true;
        notifyAll();
    }

    public synchronized String getMessage() {
        while (!hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        hasMessage = false;
        notifyAll();
        return message;
    }
}

class Producer implements Runnable {
    private final Exchange e;

    Producer(Exchange ex) {
        e = ex;
    }

    @Override
    public void run() {
        String[] messages = {"Hello", "world", "goodnight", "moon"};
        try {
            for (String message : messages) {
                e.putMessage(message);
                Thread.sleep(500);
            }
            e.putMessage("STOP");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final Exchange e;

    Consumer(Exchange ex) {
        e = ex;
    }

    @Override
    public void run() {
        String message = "";
        do {
            try {
                message = e.getMessage();
                System.out.println(message);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        } while (!message.equals("STOP"));
    }
}