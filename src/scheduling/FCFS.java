package scheduling;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import view.ExecuteView;

/**
 * This class calculate the average time using the algorithm FCFS.
 *
 */
public class FCFS extends Thread {
    private List<Jobs> readyQueue = new ArrayList<Jobs>();
    private ExecuteView executeView;
    
    public FCFS(List<Jobs> readyQueue, ExecuteView executeView) {
        this.readyQueue = readyQueue;
        this.executeView = executeView;
        
        Collections.sort(this.readyQueue, firstArrived);
    }
    
    /**
     * Method to sort the jobs according to arrival times     
     */
    public static Comparator<Jobs> firstArrived = new Comparator<Jobs>() {
        @Override
        public int compare(Jobs job1, Jobs job2) {
            if (job1.getArrivalTime() > job2.getArrivalTime()) {
                return 1;
            } else if (job1.getArrivalTime() < job2.getArrivalTime()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * Method to calculte the average time.      
     */
    @Override
    public void run() {
        double totalTime = 0;
        double avgTime = 0;
        double totalJobs = readyQueue.size();
        double lastTime = this.readyQueue.get(0).getArrivalTime();
        
        for (Jobs jobs : readyQueue) {
            for (int i = jobs.getBurstTime(); i > 0; i--) {
                this.print(Color.black, "Processo P" + jobs.getId() + " está executando (Burst Time = " + i + " segundos)");
            }

            this.print(Color.red, "Processo P" + jobs.getId() + " foi finalizado (Burst Time = 0 segundos)");
            
            totalTime = totalTime + lastTime;
            lastTime = lastTime + jobs.getBurstTime();
        }

        avgTime = totalTime / totalJobs;
        
        DecimalFormat df2 = new DecimalFormat("#.##");
        df2.setRoundingMode(RoundingMode.DOWN);
        this.executeView.getAvgResult().setText("TM = " + df2.format(avgTime) + " segundos");
        this.executeView.getScrollPane().getVerticalScrollBar().removeAdjustmentListener(this.executeView.getListener());
    }

    private void print(Color color, String message) {
        JLabel label = new JLabel();
        label.setText(message);
        label.setForeground(color);
        this.executeView.getResultPane().add(label);
        this.executeView.getResultPane().revalidate();
        this.executeView.getResultPane().repaint();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FCFS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
