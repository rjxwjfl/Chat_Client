package Controller.Thread;

import Controller.Thread.Interface.OutputThreadListener;
import Controller.Thread.Interface.ScmListener;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OutputThread extends Thread implements ScmListener {
    private List<PrintWriter> pws = new ArrayList<>();
    private OutputThreadListener listener;

    public OutputThread(OutputThreadListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
    }

    @Override
    public void onSendingMessage(String msg) {
        Iterator<PrintWriter> iterator = pws.listIterator();
        synchronized (pws){
            while (iterator.hasNext()){
                PrintWriter pw = iterator.next();
                pw.println(msg);
                pw.flush();
            }
            removePrintWriter();
        }
    }

    public synchronized void addPrintWriter(PrintWriter pw) {
        pws.add(pw);
    }

    public synchronized void removePrintWriter() {
        pws.clear();
    }
}
