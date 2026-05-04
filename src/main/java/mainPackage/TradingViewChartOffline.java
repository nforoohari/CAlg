package mainPackage;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;

public class TradingViewChartOffline extends Application {

    public static List<CryptoRecord> candles;

    @Override
    public void start(Stage stage) {

        WebView webView = new WebView();

        String url = getClass()
                .getResource("/chart/index.html")
                .toExternalForm();

        webView.getEngine().load(url);

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {

                System.out.println("HTML Loaded");

                String jsData = buildData(candles);

                webView.getEngine().executeScript("drawChart(" + jsData + ")");
            }
        });

        stage.setTitle("Trading Chart (Offline)");
        stage.setScene(new Scene(webView, 1200, 800));
        stage.show();
    }

    public static void show(List<CryptoRecord> data) {
        candles = data;
        launch();
    }

    // =========================
    // 📊 DATA (WITH VOLUME)
    // =========================
    private String buildData(List<CryptoRecord> candles) {

        StringBuilder dataJs = new StringBuilder("[");

        for (int i = 0; i < candles.size(); i++) {

            CryptoRecord c = candles.get(i);

            long time = c.getDate().getTime() / 1000;

            dataJs.append("{")
                    .append("time:").append(time).append(",")
                    .append("open:").append(c.getOpen()).append(",")
                    .append("high:").append(c.getHigh()).append(",")
                    .append("low:").append(c.getLow()).append(",")
                    .append("close:").append(c.getClose()).append(",")
                    .append("volume:").append(c.getVolume())
                    .append("}");

            if (i < candles.size() - 1) {
                dataJs.append(",");
            }
        }

        dataJs.append("]");

        return dataJs.toString();
    }
}

//package mainPackage;
//
//import javafx.application.Application;
//import javafx.concurrent.Worker;
//import javafx.scene.Scene;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class TradingViewChartOffline extends Application {
//
//    public static List<CryptoRecord> candles;
//
//    @Override
//    public void start(Stage stage) {
//
//        WebView webView = new WebView();
//
//        String url = getClass()
//                .getResource("/chart/index.html")
//                .toExternalForm();
//
//        webView.getEngine().load(url);
//
//        // بعد از لود کامل HTML، دیتا ارسال می‌شود
//        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//
//            if (newState == Worker.State.SUCCEEDED) {
//
//                System.out.println("HTML Loaded");
//
//                String jsData = buildData(candles);
//
//                webView.getEngine().executeScript("drawChart(" + jsData + ")");
//            }
//        });
//
//        stage.setTitle("Trading Chart (Offline)");
//        stage.setScene(new Scene(webView, 1200, 800));
//        stage.show();
//    }
//
//    public static void show(List<CryptoRecord> data) {
//        candles = data;
//        launch();
//    }
//
//    // تبدیل دیتا به JSON برای JS
//    private String buildData(List<CryptoRecord> candles) {
//
//        StringBuilder dataJs = new StringBuilder("[");
//
//        for (int i = 0; i < candles.size(); i++) {
//
//            CryptoRecord c = candles.get(i);
//
//            long time = c.getDate().getTime() / 1000;
//
//            dataJs.append("{")
//                    .append("time:").append(time).append(",")
//                    .append("open:").append(c.getOpen()).append(",")
//                    .append("high:").append(c.getHigh()).append(",")
//                    .append("low:").append(c.getLow()).append(",")
//                    .append("close:").append(c.getClose())
//                    .append("}");
//
//            if (i < candles.size() - 1) {
//                dataJs.append(",");
//            }
//        }
//
//        dataJs.append("]");
//
//        return dataJs.toString();
//    }
//}