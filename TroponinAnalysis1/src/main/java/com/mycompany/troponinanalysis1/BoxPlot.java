/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.troponinanalysis1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 *
 * @author SERPİL
 */
public class BoxPlot extends javax.swing.JFrame {

    /**
     * Creates new form BoxPlot
     */
    public BoxPlot() {
        initComponents();
        drawZoomedBoxPlot();//Yakınlaştırılmış oxplot grafiğini çizdiriyorum.
        drawBoxPlot();//Boxplot grafiğini çizdirdim
    }
    // Bu metotta CSV dosyasındaki tüm troponin değerlerini alıp pozitif ve negatif olarak ayırdım
    // Ardından bu iki gruba ait boxplot grafiğini sol panel olan panelNormal içine çizdirdim

    private void drawBoxPlot() {
        try {
            List<Double> positiveList = new ArrayList<>(); //Pozitif tanılı hastaların değerlerini tuttum
            List<Double> negativeList = new ArrayList<>(); //Negatif tanılı hastaların değerlerini tuttum

            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/mycompany/troponinanalysis1/Medicaldataset.csv"));
            String line;
            boolean isFirstLine = true; //İlk satırı başlık olduğu için atlayacağım

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 8 && !parts[7].isEmpty()) {
                    try {
                        double troponin = Double.parseDouble(parts[7]); //Troponin değeri alıyorum
                        String result = parts[8].trim().toLowerCase();  //Tanıyı küçük harfe çeviriyorum

                        //Tanıya göre ilgili listeye ekliyorum
                        if (result.equals("positive")) {
                            positiveList.add(troponin);
                        } else if (result.equals("negative")) {
                            negativeList.add(troponin);
                        }
                    } catch (NumberFormatException ignored) {
                        //Sayıya çevrilemeyen veri varsa atladım
                    }
                }
            }
            br.close(); //Dosya okuma işini bitirip kapattım

            //Boxplot için verileri dataset içine yerleştirdim
            DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(positiveList, "Troponin", "Pozitif");
            dataset.add(negativeList, "Troponin", "Negatif");

            //Grafik başlıkları ve eksenleriyle boxplot grafiğini oluşturdum
            JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                    "Tüm Troponin Değerleri",
                    "Tanı", //X ekseni:pozitif mi negatif mi?
                    "Troponin Değeri", //Y ekseni:sayısal değerler
                    dataset,
                    true //Açıklama kutusu(legend)görünsün istedim
            );

            CategoryPlot plot = chart.getCategoryPlot();
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setAutoRange(false); //Y ekseni aralığını ben belirledim
            rangeAxis.setRange(0.0, 15.0); //0–15 arası değerleri gösterdim

            //Ortalama değeri göstermesin diye renderer ayarı yaptım
            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
            renderer.setMeanVisible(true);
            plot.setRenderer(renderer);

            //ChartPanel içine grafiği yerleştirdim ve GUI paneliyle bağladım
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(panelNormal.getSize());
            panelNormal.setLayout(new java.awt.BorderLayout());
            panelNormal.removeAll();
            panelNormal.add(chartPanel, java.awt.BorderLayout.CENTER);
            panelNormal.validate();

        } catch (Exception e) {
            e.printStackTrace(); //Hata olursa konsola yazsın diye çıktıyı verdim
        }
    }

    //Bu metotta sadece troponin değeri küçük olanları (0–0.25) alıp pozitif/negatif olarak ayırdım
    //Ardından bu veriler için daha dar aralıklı bir boxplot oluşturdum
    private void drawZoomedBoxPlot() {
        try {
            List<Double> positiveZoom = new ArrayList<>();
            List<Double> negativeZoom = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/mycompany/troponinanalysis1/Medicaldataset.csv"));
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 8 && !parts[7].isEmpty()) {
                    try {
                        double troponin = Double.parseDouble(parts[7]);
                        String result = parts[8].trim().toLowerCase();

                        //Sadece küçük troponin değerlerini alıyorum (0–0.25)
                        if (troponin >= 0 && troponin <= 0.25) {
                            if (result.equals("positive")) {
                                positiveZoom.add(troponin);
                            } else if (result.equals("negative")) {
                                negativeZoom.add(troponin);
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            br.close();

            DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(positiveZoom, "Troponin", "Pozitif (Zoomed)");
            dataset.add(negativeZoom, "Troponin", "Negatif (Zoomed)");

            // İkinci boxplot grafiğini oluşturdum (sadece küçük değerler için)
            JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                    "Zoomed Troponin Değerleri (0–0.25)",
                    "Tanı",
                    "Troponin Değeri",
                    dataset,
                    true
            );

            CategoryPlot plot = chart.getCategoryPlot();
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setAutoRange(false);
            rangeAxis.setRange(0.0, 0.25); //Küçük aralıkta gösterim yaptım

            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
            renderer.setMeanVisible(true);
            plot.setRenderer(renderer);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(panelZoomed.getSize());
            panelZoomed.setLayout(new java.awt.BorderLayout());
            panelZoomed.removeAll();
            panelZoomed.add(chartPanel, java.awt.BorderLayout.CENTER);
            panelZoomed.validate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNormal = new javax.swing.JPanel();
        panelZoomed = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout panelNormalLayout = new javax.swing.GroupLayout(panelNormal);
        panelNormal.setLayout(panelNormalLayout);
        panelNormalLayout.setHorizontalGroup(
            panelNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panelNormalLayout.setVerticalGroup(
            panelNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelZoomedLayout = new javax.swing.GroupLayout(panelZoomed);
        panelZoomed.setLayout(panelZoomedLayout);
        panelZoomedLayout.setHorizontalGroup(
            panelZoomedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );
        panelZoomedLayout.setVerticalGroup(
            panelZoomedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(panelNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelZoomed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelNormal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelZoomed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(BoxPlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BoxPlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BoxPlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BoxPlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoxPlot().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelNormal;
    private javax.swing.JPanel panelZoomed;
    // End of variables declaration//GEN-END:variables
}
