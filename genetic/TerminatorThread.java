package genetic;

public class TerminatorThread extends Thread {

    private WriterRunnable writer;

    public TerminatorThread(WriterRunnable writer){
        this.writer = writer;
    }

    public void run() {
        writer.end();
    }
}