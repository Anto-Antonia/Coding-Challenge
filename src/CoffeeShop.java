import java.io.BufferedReader;
import java.io.IOException;
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

    // main method, expecting 3 command-line arguments (user x coords, user y coords, URL of the coffee shop CSV file)
    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Error: Missing arguments. Usage: <user_x_coordinate> <user_y_coordinate> <shop_data_url>"); // more debugging friendly
            return;
        }
        try{
            // parse user coordinates
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            String shopUrl = args[2];

            //fetch coffee shops and calculates the distance
            List<CoffeeShop> coffeeShopList = fetchCoffeeShops(shopUrl, x, y);

            // sort shops by distance
            coffeeShopList.sort(Comparator.comparingDouble(shop -> shop.distance));

            int count = Math.min(3, coffeeShopList.size()); // print top 3 closest coffee shops
            for(int i = 0; i < count; i++){
                CoffeeShop shop = coffeeShopList.get(i);
                System.out.printf("%s,%.4f\n",shop.name, shop.distance);
            }
        } catch(NumberFormatException e){
            System.out.println("Error: Invalid coordinate format. Please enter valid numbers");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: Missing arguments. Please provide X and Y coordinates.");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Error: Failed to fetch coffee shops. Please check if the URL is correct and accessible");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("An unexpected error has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<CoffeeShop> fetchCoffeeShops(String url, double userX, double userY) throws Exception{
        List<CoffeeShop> shops = new ArrayList<>(); // list for storing coffee shops
        URL url1 = new URL(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url1.openStream())); // using buffer reader to read from urls
        String line;

        while((line = reader.readLine()) != null){
            String[] parts = line.split(",");

            // skip invalid lines that don't match the expected format
            if(parts.length != 3){
                System.out.println("Skipping invalid entry: " + line);
                continue;
            }
            try{
                // Parse coffee shop data
                String name = parts[0].trim();
                double shopX = Double.parseDouble(parts[1].trim()); // added .trim()
                double shopY = Double.parseDouble(parts[2].trim());

                //calculate distance form user
                double distance = calculateDistance(userX,userY, shopX, shopY);
                shops.add(new CoffeeShop(name, shopX, shopY,distance));
            } catch(NumberFormatException e){
                System.out.println("Skipping invalid entry due to bad number format: " + line);
            }
        }
        reader.close();
        return shops;
    }

    private static double calculateDistance(double userX, double userY, double shopX, double shopY) { // using Euclidean formula to calculate the distance between 2 points (user and coffee shop)
        double distance =  Math.sqrt(Math.pow(userX - shopX, 2) + Math.pow(shopY - userY, 2));        // distance formula: sqrt((x2 -x1)^2 + (y2 -y1)^2)
        return Math.round(distance * 10000.0) / 10000.0;  // rounding to 4 decimals
    }
}