/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlabproje;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class GoogleMatrixRequest {

    public GoogleMatrixRequest() {
    }
    ArrayList<Integer> pathIndexleri = new ArrayList<Integer>();
    private int[][] uzakliklar = null;
    private int optimiziUzaklik = Integer.MAX_VALUE;
    public String optimizeRota = "";
    private Kargo kuryeKonum = null;
    private static final String API_KEY = "APIKEY";

    OkHttpClient client = new OkHttpClient();

    public int run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        JSONObject getSth = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance");
        int mesafe = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");
        return mesafe;
    }

    public void hesapla() throws IOException, InterruptedException {
        ArrayList<Kargo> kargolar = new ArrayList<Kargo>();
        if (kuryeKonum != null) {
            kargolar.add(kuryeKonum);
        }
        double kLat;
        double kLng;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = String.format("jdbc:mysql://35.202.83.160:3306/proje");
            Connection conn = DriverManager.getConnection(url, "root", "emre1441.");
            System.out.println("Veritabanına bağlandı.");
            String query = "SELECT kargoLat, kargoLng"
                    + " FROM kargolar WHERE teslimEdildiMi='false'";
            Statement ifade = conn.createStatement();
            ResultSet rs = ifade.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("kargoLat");
                String b = rs.getString("kargoLng");
                kLat = Double.parseDouble(a);
                kLng = Double.parseDouble(b);
                Kargo newKargo = new Kargo(kLat, kLng);
                kargolar.add(newKargo);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Veritabanına baglanamadı. " + e);
        }
        uzakliklar = new int[kargolar.size()][kargolar.size()];
        for (int i = 0; i < kargolar.size(); i++) {
            for (int j = 0; j < kargolar.size(); j++) {
                GoogleMatrixRequest request = new GoogleMatrixRequest();
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + kargolar.get(i).kargoLat + "%2C" + kargolar.get(i).kargoLng + "&destinations=" + kargolar.get(j).kargoLat + "%2C" + kargolar.get(j).kargoLng + "&key=" + API_KEY;
                int m = request.run(url);
                uzakliklar[i][j] = m;
            }
        }

        String path = "";
        int[] koseler = new int[kargolar.size() - 1];
        for (int i = 1; i < kargolar.size(); i++) {
            koseler[i - 1] = i;
        }
        prosedur(0, koseler, path, 0);

        System.out.println("Rota: " + optimizeRota + " Uzaklık = " + optimiziUzaklik);
        String[] paths = optimizeRota.split(Pattern.quote(","));
        for (int i = 0; i < paths.length; i++) {
            int pathIndex = Integer.parseInt(paths[i]);
            pathIndexleri.add(pathIndex);
        }
    }

    private int prosedur(int ilk, int koseler[], String path, int burayaKadarOlanMaliyet) {

        path = path + Integer.toString(ilk) + ",";
        int length = koseler.length;
        int yeniBurayaKadarOlanMaliyet;

        if (length == 0) {
            yeniBurayaKadarOlanMaliyet = burayaKadarOlanMaliyet + uzakliklar[ilk][0];

            if (yeniBurayaKadarOlanMaliyet < optimiziUzaklik) {
                optimiziUzaklik = yeniBurayaKadarOlanMaliyet;
                optimizeRota = path + "0";
            }
            return (uzakliklar[ilk][0]);

        } else if (burayaKadarOlanMaliyet > optimiziUzaklik) {

            return 0;

        } else {

            int[][] yeniKoseler = new int[length][(length - 1)];
            int maliyetMevcutDugum, maliyetKomsu;
            int eniyiMaliyet = Integer.MAX_VALUE;

            for (int i = 0; i < length; i++) {

                for (int j = 0, k = 0; j < length; j++, k++) {

                    if (j == i) {
                        k--;
                        continue;
                    }
                    yeniKoseler[i][k] = koseler[j];
                }

                maliyetMevcutDugum = uzakliklar[ilk][koseler[i]];
                yeniBurayaKadarOlanMaliyet = maliyetMevcutDugum + burayaKadarOlanMaliyet;
                maliyetKomsu = prosedur(koseler[i], yeniKoseler[i], path, yeniBurayaKadarOlanMaliyet);

                int tumMaliyet = maliyetKomsu + maliyetMevcutDugum;

                if (tumMaliyet < eniyiMaliyet) {
                    eniyiMaliyet = tumMaliyet;
                }
            }
            return (eniyiMaliyet);
        }
    }

    public void setKuryeKonum(Kargo kuryeKonum) {
        this.kuryeKonum = kuryeKonum;
    }

    public ArrayList<Integer> getPathIndexleri() {
        return pathIndexleri;
    }

}
