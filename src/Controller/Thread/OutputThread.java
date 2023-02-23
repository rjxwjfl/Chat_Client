package src.Controller.Thread;

import src.Controller.Thread.Interface.OutputThreadListener;
import src.Controller.Thread.Interface.ScmListener;

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
        System.out.println("\"PRINT-WRITER CREATED\"");
        pws.add(pw);
    }

    public synchronized void removePrintWriter() {
        pws.clear();
        System.out.println("\"PRINT-WRITER REMOVED\"");
    }
}
