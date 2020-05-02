package com.company.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CatererRepo {
    private DatabaseConnector connector;

    public CatererRepo() {
        this.connector = DatabaseConnector.getInstance();
    }

    public void createIngredient(Ingredient ingredient) {
        List<Ingredient> ingDB = findAll();
        if (ingDB.size() == 0){
            String sql = "INSERT INTO ingredient (ingredient_name) Values ('" + ingredient.getName() + "') ";
            connector.deleteUpdateInsert(sql);
            ingredient.setId(fetchIngredientID(ingredient));
        } else {
            for (Ingredient ing : ingDB) {
                if (ing.equals(ingredient)) {
                    ingredient.setId(ing.getId());
                    break;
                } else {
                    String sql = "INSERT INTO ingredient (ingredient_name) Values ('" + ingredient.getName() + "') ";
                    connector.deleteUpdateInsert(sql);
                    ingredient.setId(fetchIngredientID(ingredient));
                }
            }
        }
    }

    public void createMeal(Meal meal) {
        String sql = "INSERT INTO menu (name, menu_price) Values ('" + meal.getName() + "', '" + meal.getPrice() + "') ";
        connector.deleteUpdateInsert(sql);
        meal.setId(fetchMealID(meal));
        for (Ingredient ingredient : meal.getIngredients()) {
            createIngredient(ingredient);
            createMealIngredientInsert(meal, ingredient);
        }
    }

    private void createMealIngredientInsert(Meal meal, Ingredient ingredient) {
        String sql = "INSERT INTO menu_ingredient (menu_id, ingredient_id) Values ('" + meal.getId() + "', '"
                + ingredient.getId() + "') ";
        connector.deleteUpdateInsert(sql);
    }

    private int fetchMealID(Meal meal) {
        int id = 0;
        ResultSet result = connector.fetchData("SELECT id FROM menu WHERE name ='" + meal.getName() + "'");
        if (result == null) {
            System.out.println("could not find mealID");
        }
        try {
            result.next();
            id = result.getInt("id");

        } catch (SQLException e) {
            System.out.println("error find mealID");
            System.out.println(e.getMessage());
        } finally {
            connector.closeConnection();
            return id;
        }
    }

    private int fetchIngredientID(Ingredient ingredient) {
        int id = 0;
        ResultSet result = connector.fetchData("SELECT id FROM ingredient WHERE ingredient_name ='" + ingredient.getName() + "'");
        if (result == null) {
            System.out.println("could not find IngredientID");
        }
        try {
            result.next();
            id = result.getInt("id");

        } catch (SQLException e) {
            System.out.println("error find IngredientID");
            System.out.println(e.getMessage());
        } finally {
            connector.closeConnection();
            return id;
        }
    }

    private int fetchDeliveryID(City city) {
        int id = 0;
        ResultSet result = connector.fetchData("SELECT id FROM delivery_price WHERE delivery_price ='" + city.getDeliveryPrice() + "'");
        if (result == null) {
            System.out.println("could not find DeliveryPrice");
        }
        try {
            result.next();
            id = result.getInt("id");

        } catch (SQLException e) {
            System.out.println("error find DeliveryPrice");
            System.out.println(e.getMessage());
        } finally {
            connector.closeConnection();
            return id;
        }
    }

    private List<Ingredient> findAll() {
        List<Ingredient> ingredientsAllDB = new ArrayList<>();
        ResultSet result = connector.fetchData("SELECT * FROM ingredient");
        if (result == null) {
            System.out.println("could not fetch ingredients");
            return null;
        }
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("ingredient_name");
                Ingredient ingredient = null;
                ingredient.setId(id);
                ingredientsAllDB.add(ingredient = new Ingredient(name));

            }
        } catch (SQLException e) {
            System.out.println("error while parsing all menu");
            System.out.println(e.getMessage());
        } finally {
            connector.closeConnection();
            return ingredientsAllDB;
        }
    }

    public void delete(String name, String table) {
        String sql = "delete from " + table + " where " + table + "_name ='" + name + "'";
        connector.deleteUpdateInsert(sql);
    }

    public void createCity(City city) {
        List<City> cityDB = findAllCity();
        boolean existDeliveryPrice = cityDB.contains(city.getDeliveryPrice());
        boolean existCity = cityDB.contains(city.getName());
        if (existDeliveryPrice) {
            for (City country : cityDB) {
                if (country.equals(city)) {
                    city.setDeliveryId(country.getDeliveryId());
                    break;
                }
            }
        }
        if (!existDeliveryPrice) {
            String sql = "INSERT INTO delivery_price (delivery_price) Values ('" + city.getDeliveryPrice() + "') ";
            connector.deleteUpdateInsert(sql);
            city.setDeliveryId(fetchDeliveryID(city));
        }
        if (!existCity) {
            String sql = "INSERT INTO city (city_name, delivery_id) Values ('" + city.getName() + "', '" + city.getDeliveryId() + "') ";
            connector.deleteUpdateInsert(sql);
        }
    }

    private List<City> findAllCity() {
        List<City> cityAllDB = new ArrayList<>();
        ResultSet result = connector.fetchData("SELECT * FROM city INNER JOIN delivery_price ON city.delivery_id " +
                "= delivery_price.id");
        if (result == null) {
            System.out.println("could not fetch Citys");
            return null;
        }
        try {
            while (result.next()) {
                City city = new City();
                city.setId(result.getInt("id"));
                city.setName(result.getString("city_name"));
                city.setDeliveryId(result.getInt("delivery_id"));
                city.setDeliveryPrice(result.getDouble("delivery_price"));
                cityAllDB.add(city);
            }
        } catch (SQLException e) {
            System.out.println("error while parsing all menu");
            System.out.println(e.getMessage());
        } finally {
            connector.closeConnection();
            return cityAllDB;
        }
    }

    public void evalutionResult() {
        Scanner myIntScanner = new Scanner(System.in);
        Scanner myScanner = new Scanner(System.in);
        String[] evaluationNumber = {"1.Wie viele Bestellungen gab es schon?", "2.Wie viele Bestellungen gab es je Kunde?"
                , "3.Wie viele Bestellungen gab es je Ortschaft?", "4.Umsätze (Gesamt/je Kunde/je Ortschaft?)",
                "5.Was wurde am häufigsten bestellt und wie oft?", "6.Absteigend, wie oft bestellt wurde.", "7.Beenden"};
        printStringArray(evaluationNumber);
        System.out.println("Bitte wählen sie eine Zahl aus:");
        int number = myIntScanner.nextInt();
        switch (number) {
            case 1:
                ResultSet rs = connector.fetchData("SELECT COUNT(*) FROM menu_order");
                try {
                    rs.next();
                    int count = rs.getInt("COUNT(*)");
                    System.out.println(count + " Bestellungen bis jetzt.");

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                    break;
                }
            case 2:
                rs = connector.fetchData("SELECT COUNT(*), customer_name FROM `menu_order` " +
                        "INNER JOIN customer on customer_id = customer.id GROUP BY customer_id");
                try {
                    while (rs.next()) {
                        int count = rs.getInt("Count(*)");
                        String name = rs.getString("customer_name");
                        System.out.println(count + " Bestellungen \tvon " + name);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                    break;
                }
            case 3:
                rs = connector.fetchData("SELECT COUNT(*), customer_city FROM `menu_order` INNER JOIN customer on customer_id =" +
                        " customer.id GROUP BY customer_city");
                try {
                    while (rs.next()) {
                        int count = rs.getInt("Count(*)");
                        String name = rs.getString("customer_city");
                        System.out.println(count + " Bestellungen in " + name);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                    break;
                }
            case 4:
                rs = connector.fetchData("SELECT sum(order_finish_price)as sales FROM menu_order");
                try {
                    rs.next();
                    double sales = rs.getDouble("sales");
                    System.out.println("Umsätze gesamt: " + sales + "€");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                }
                rs = connector.fetchData("SELECT sum(order_finish_price)as sales, customer_name as name  FROM menu_order INNER JOIN " +
                        "customer on customer_id = customer.id GROUP BY customer_name");
                System.out.println("Umsätze nach Kunden: ");
                try {
                    while (rs.next()) {
                        double sales = rs.getDouble("sales");
                        String customerName = rs.getString("name");
                        System.out.println(sales + "€ von " + customerName);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                }
                rs = connector.fetchData("SELECT sum(order_finish_price)as sales, customer_city as city  FROM menu_order INNER JOIN " +
                        "customer on customer_id = customer.id GROUP BY customer_city");
                System.out.println("Umsätze nach Ortschaft: ");
                try {
                    while (rs.next()) {
                        double sales = rs.getDouble("sales");
                        String customerCity = rs.getString("city");
                        System.out.println(sales + "€ nach " + customerCity);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                }
                break;
            case 5:
                rs = connector.fetchData("SELECT COUNT(*), menu.name FROM menu_order_details  " +
                        "INNER JOIN menu on menu.id = menu_order_details.menu_id " +
                        "GROUP BY menu_id");
                try {
                    rs.next();
                    int count = rs.getInt("Count(*)");
                    String menuName = rs.getString("name");
                    System.out.println(menuName + " wurde " + count + "mal bestellt.");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                }
                break;

            case 6:
                rs = connector.fetchData("SELECT COUNT(*), menu.name FROM menu_order_details  " +
                        "INNER JOIN menu on menu.id = menu_order_details.menu_id " +
                        "GROUP BY menu_id");
                try {
                    while (rs.next()) {
                        int count = rs.getInt("Count(*)");
                        String menuName = rs.getString("name");
                        System.out.println(count + "x " + menuName);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    connector.closeConnection();
                }
                break;

            case 7:
                break;
            default:
                System.out.println("Eingabe wurde nicht erkannt!");
                break;
        }
        if (number != 7) {
            System.out.println("Noch eine Auswertung? (J/N)");
            String runMore = myScanner.nextLine();
            if (runMore.equalsIgnoreCase("J")) {
                evalutionResult();
            }
        }
    }

    private static void printStringArray(String[] text) {
        for (int i = 0; i < text.length; i++) {
            System.out.println(text[i] + "\t");

        }

    }
}
