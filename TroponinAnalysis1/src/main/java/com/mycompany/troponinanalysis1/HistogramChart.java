/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.troponinanalysis1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author SERPİL
 */
public class HistogramChart extends javax.swing.JFrame {

    /**
     * Creates new form HistogramChart
     */
    public HistogramChart() {
        initComponents();
        drawHistogram();//Histogram grafiğini çizdirdim
    }

    private void drawHistogram() {
        try {
            //Troponin verilerini okudum
            double[] data = readTroponinValues("src/main/java/com/mycompany/troponinanalysis1/Medicaldataset.csv");

            //Histogram veri kümesi oluşturdum
            HistogramDataset dataset = new HistogramDataset();
            dataset.addSeries("Troponin", data, 30); // 30 sütunluk histogram

            //Histogram grafğini oluşturdum
            JFreeChart chart = ChartFactory.createHistogram(
                    "Troponin Histogramı",//başlık
                    "Troponin Değeri",    //x ekseni
                    "Frekans",            //y ekseni
                    dataset,              //veriseti
                    PlotOrientation.VERTICAL,//dikey çizim
                    true, true, false     //legend ve tooltips aktif
            );

            //Grafiği CahrtPanel'e yerleştirdim
            ChartPanel cPanel = new ChartPanel(chart);
            cPanel.setPreferredSize(new java.awt.Dimension(chartPanel.getWidth(), chartPanel.getHeight()));
            chartPanel.setLayout(new java.awt.BorderLayout());
            chartPanel.removeAll();//önceki bileşenleri temizledim
            chartPanel.add(cPanel, java.awt.BorderLayout.CENTER);//yeni paneli yerleştirdim
            chartPanel.validate();//paneli günceledim

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "CSV dosyası okunamadı: " + e.getMessage());
        }
    }

    //CSV dosyasından troponin verilerini okudum
    private double[] readTroponinValues(String filePath) throws IOException {
        List<Double> values = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        boolean isFirstLine = true;

        //Stır satır CSV dosyasını okudum
        while ((line = br.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;//başlık satırı geçildi
            }
            String[] parts = line.split(",");
            if (parts.length > 7) {
                try {
                    //troponin sütunununalıp listeye ekledim
                    values.add(Double.parseDouble(parts[7])); // Troponin sütunu
                } catch (NumberFormatException ignored) {
                }
            }
        }
        br.close();

        //Listeyi diziye çevirdim
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chartPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 568, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HistogramChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistogramChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistogramChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistogramChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HistogramChart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel;
    // End of variables declaration//GEN-END:variables
}
