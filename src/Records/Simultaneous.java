
package Records;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by ipopovich on 04.08.2014.
 */





    public class Simultaneous implements Runnable {



        protected static final int THREADS = 10;

        private static class Semaphore {
            public boolean set = false;
        }

        protected static final Semaphore[] semaphores =
                new Semaphore[THREADS];
        protected static final Thread[] threads = new Thread[THREADS];



        protected final int threadNum;
    protected  final String arg1;

        protected Simultaneous(int num, String arg1) {
            this.threadNum = num;
            this.arg1 = arg1;
        }



        public void run() {
            synchronized (semaphores) {
                semaphores[this.threadNum].set = true;
                semaphores.notify();
            }

            final long startExec = System.currentTimeMillis();



            try {
                Test.mains(arg1);
            } catch (XMLStreamException | ParseException | InterruptedException | WriteException | BiffException | IOException e) {
                e.printStackTrace();
            }
            final long finishExec = System.currentTimeMillis();
            System.out.println("Thread N" + this.threadNum + ". Start: " +
                    startExec + ". End: " + finishExec +
            ". Total: " + (finishExec - startExec) + ".");
        }


        public static void main(String[] args) {
            for (int i=0; i<args.length; i++) {
                semaphores[i] = new Semaphore();
            }

            System.out.println("Starting " + args.length + " threads...");

            synchronized (Simultaneous.class) {
                synchronized (semaphores) {
                    for (int i=0; i<args.length; i++) {

                        final Simultaneous mainThread = new Simultaneous(i, args[i]);
                        threads[i] = new Thread(mainThread, "Thread N" + i);
                        threads[i].setDaemon(true);
                        threads[i].start();
                    }

                    System.out.println("Waiting for simultaneous start...");

                    boolean isAllStarted = false;
                    while (!isAllStarted) {
                        try {
                            semaphores.wait();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        isAllStarted = true;
                        for (int i=0; i<args.length; i++) {
                            if (!semaphores[i].set) {
                                isAllStarted = false;
                                break;
                            }
                        }
                    }
                }
            }

            System.out.println("Waiting for threads to finish execution...");

            for (int i=0; i<args.length; i++) {
                try {
                    threads[i].join();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            System.out.println("Program terminated successfully.");
        }
    }


