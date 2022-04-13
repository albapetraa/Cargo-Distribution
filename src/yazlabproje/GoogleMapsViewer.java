/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlabproje;

import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class GoogleMapsViewer extends JFXPanel {

    private WebEngine engine = null;
    private WebView webView = null;

    public void loadMap(String mapLocation) {
        Platform.runLater(() -> {
            webView = new WebView();
            engine = webView.getEngine();
            engine.setJavaScriptEnabled(true);
            setScene(new Scene(webView));
            File f = new File(getClass().getClassLoader().getResource(mapLocation).getFile());
            engine.load(f.toURI().toString());
//            webView.setOnMouseClicked(event -> System.out.println("Mouse Clicked!"));

        });

    }

    public void addClickListener(MapsEvent<LatLng> handle) {
        Platform.runLater(() -> {
            webView.setOnMouseClicked((event) -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Object obj = engine.executeScript("event_click.shift();");
                    if (obj instanceof JSObject) {
                        JSObject js = (JSObject) obj;
                        LatLng latlng = new LatLng(js.eval("this.latLng.lat()").toString(), js.eval("this.latLng.lng()").toString());
                        handle.handle(latlng);

                    }
                }
            });
        });
    }

    public void cizgiCizme() {
        Platform.runLater(() -> {
            engine.executeScript("cizgiCiz();");
        });
    }

    public void x() {
        Platform.runLater(() -> {
            engine.executeScript("calcRoute();");
        });
    }

    public void addKurye(double lat, double lng, String key, String label) {
        Platform.runLater(() -> {
            engine.executeScript("addKurye(" + lat + "," + lng + ",'" + key + "'," + label + ");");
        });
    }

    public void addMarker(double lat, double lng, String key, String label) {
        Platform.runLater(() -> {
            engine.executeScript("addMarker(" + lat + "," + lng + ",'" + key + "'," + label + ");");
        });
    }

    public void initMap() {
        Platform.runLater(() -> {
            engine.executeScript("initMap();");
        });
    }

    public void setKuryeMarker(double lat, double lng, String key, String label) {
        Platform.runLater(() -> {
            engine.executeScript("setKuryeMarker(" + lat + "," + lng + ",'" + key + "'," + label + ");");
        });
    }

    public void deleteMarkers() {
        Platform.runLater(() -> {
            engine.executeScript("deleteMarkers();");
        });

    }

}
