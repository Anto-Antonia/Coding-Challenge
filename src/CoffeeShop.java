import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CoffeeShop {
    String name;
    double x;
    double y;
    double distance;

    public CoffeeShop(String name, double x, double y, double distance) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Usage: <user_x_coordinate> <user_y_coordinate> <shop_data_url>");
            return;
        }
        try{
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            String shopUrl = args[2];

            List<CoffeeShop> coffeeShopList = fetchCoffeeShops(shopUrl, x, y);

            coffeeShopList.sort(Comparator.comparingDouble(shop -> shop.distance));

            int count = Math.min(3, coffeeShopList.size());
            for(int i = 0; i < count; i++){
                CoffeeShop shop = coffeeShopList.get(i);
                System.out.printf("%s,%.4f\n",shop.name, shop.distance);
            }
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<CoffeeShop> fetchCoffeeShops(String url, double userX, double userY) throws Exception{
        List<CoffeeShop> shops = new ArrayList<>();
        URL url1 = new URL(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url1.openStream()));
        String line;

        while((line = reader.readLine()) != null){
            String[] parts = line.split(",");

            if(parts.length != 3){
                System.out.println("Invalid data format: "+ line);
                continue;
            }
            try{
                String name = parts[0].trim();
                double shopX = Double.parseDouble(parts[1]);
                double shopY = Double.parseDouble(parts[2]);
                double distance = calculateDistance(userX,userY, shopX, shopY);
                shops.add(new CoffeeShop(name, shopX, shopY,distance));
            } catch(NumberFormatException e){
                System.out.println("Invalid coffee shop format found. " + line);
            }
        }
        reader.close();
        return shops;
    }

    private static double calculateDistance(double userX, double userY, double shopX, double shopY) {
        double distance =  Math.sqrt(Math.pow(userX - shopX, 2) + Math.pow(shopY - userY, 2));
        return Math.round(distance * 10000.0) / 10000.0;  // rounding to 4 decimals
    }
}