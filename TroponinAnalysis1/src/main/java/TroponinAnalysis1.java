import java.io.*;
import java.util.*;

public class TroponinAnalysis1{

    public static void main(String[] args) throws IOException {
        //Troponin değerlerini ve pozitif-negatif grupları ayrı ayrı saklayacağım listeleri oluşturdum
        List<Double> troponinValues = new ArrayList<>();
        List<Double> posGroup = new ArrayList<>();
        List<Double> negGroup = new ArrayList<>();
       
        //CSV dosyasını okumak için BufferedReader'ı tanımladım
        BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/mycompany/troponinanalysis1/Medicaldataset.csv"));
        String line;
        boolean isFirstLine = true;
       
        //Dosyadaki tüm satırları sırayla okuyarak her satırı işledim
        while ((line = br.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;//İlk satır başlık olduğu için atladım
                continue;
            }
            //Satırı virgül ile ayırdım
            String[] parts = line.split(",");
            if (parts.length > 8) {
                //7.sütundaki troponin değerini aldım
                double value = Double.parseDouble(parts[7]); 
                //8.sütundaki tanı bilgisini küçük harfe çevirerek aldım
                String result = parts[8].toLowerCase();    
                //Değeri genel troponin listesine ekledim
                troponinValues.add(value);

                //Eğer sonuç pozitifse pozitif gruba ekledim
                if (result.contains("positive")) {
                    posGroup.add(value);
                }
                //Eğer sonuç negatifse negatif gruba ekledim
                else if (result.contains("negative")) {
                    negGroup.add(value);
                }
            }
        }
        br.close();//Dosyayaı kapattım

        //Temel İstatistikleri hesapladım
        int n = troponinValues.size();
        double mean = getMean(troponinValues);               //ortalama
        double median = getMedian(troponinValues);           //medyan
        double variance = getVariance(troponinValues, mean); //varyans
        double stdDev = Math.sqrt(variance);                 //standart sapma
        double stdError = stdDev / Math.sqrt(n);             //standart hata

        //Ortalama için güven Aralığı(%95)
        double z = 1.96;
        double ciLower = mean - z * stdError;
        double ciUpper = mean + z * stdError;
        
        //Varyans için güven aralığı(%95)
        double varSE = variance * Math.sqrt(2.0 / (n - 1));
        double varCiLower = variance - z * varSE;
        double varCiUpper = variance + z * varSE;

        //Örneklem büyüklüğü tahmini(%90, E=0.1)
        double z90 = 1.645;
        double marginOfError = 0.1;
        double sampleSize = Math.pow((z90 * stdDev) / marginOfError, 2);

        //Hipotez testi(pozitif ve negatif grup ortalamaları arasındaki fark)
        double meanPos = getMean(posGroup);
        double meanNeg = getMean(negGroup);
        double varPos = getVariance(posGroup, meanPos);
        double varNeg = getVariance(negGroup, meanNeg);
        int n1 = posGroup.size();
        int n2 = negGroup.size();
        double se = Math.sqrt(varPos / n1 + varNeg / n2);
        double t_stat = (meanPos - meanNeg) / se;

        //Sonuçları ekrana yazdırdım
        System.out.println("========= TROPONIN ANALIZI =========");
        System.out.printf("Gozlem Sayisi         : %d\n", n);
        System.out.printf("Ortalama (Mean)       : %.4f\n", mean);
        System.out.printf("Medyan                : %.4f\n", median);
        System.out.printf("Varyans               : %.4f\n", variance);
        System.out.printf("Standart Sapma        : %.4f\n", stdDev);
        System.out.printf("Standart Hata         : %.4f\n", stdError);
        System.out.printf("%%95 Guven Araligi(ortalama    : [%.4f , %.4f]%n", ciLower, ciUpper);
        System.out.printf("%%95 Guven Araligi(varyans)  : [%.4f , %.4f]%n", varCiLower, varCiUpper);
        System.out.printf("Onerilen Orneklem     : %.0f kisii (%%90 guven, -+0.1 hata)\n", Math.ceil(sampleSize));
        System.out.println("----------- Hipotez Testi -----------");
        System.out.printf("Positive Ortalama     : %.4f\n", meanPos);
        System.out.printf("Negative Ortalama     : %.4f\n", meanNeg);
        System.out.printf("t-istatistigi         : %.4f\n", t_stat);
        System.out.println("p-degeri < 0 varsayimiyla anlamlilik yorumlanabilir.");
        System.out.println("=====================================");
    }

    //Ortalama hesaplayan metot
    public static double getMean(List<Double> data) {
        double sum = 0;
        for (double d : data) sum += d;
        return sum / data.size();
    }

    //Medyan hesaplayan metot(sıralama yapıp ortadaki değeri aldım)
    public static double getMedian(List<Double> data) {
        List<Double> sorted = new ArrayList<>(data);
        Collections.sort(sorted);
        int n = sorted.size();
        if (n % 2 == 0)
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        else
            return sorted.get(n / 2);
    }

    //varyans hesaplayan metot
    public static double getVariance(List<Double> data, double mean) {
        double sumSq = 0;
        for (double d : data)
            sumSq += Math.pow(d - mean, 2);
        return sumSq / (data.size() - 1);
    }
}