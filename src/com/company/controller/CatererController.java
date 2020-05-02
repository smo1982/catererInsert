package com.company.controller;

import com.company.model.CatererRepo;
import com.company.model.City;
import com.company.model.Ingredient;
import com.company.model.Meal;

import java.util.ArrayList;
import java.util.Scanner;

public class CatererController {

    public void start() {
        ArrayList<String> choice = new ArrayList<>();
        String choiceOne = "1.Menü Auswahl";
        choice.add(choiceOne);
        String choiceTwo = "2.Zutaten Auswahl";
        choice.add(choiceTwo);
        String choiceThree = "3.Stadt Auswahl";
        choice.add(choiceThree);
        String choiceFour = "4.Auswertungen";
        choice.add(choiceFour);
        String choiceFive = "5.Beenden";
        choice.add(choiceFive);
        String[] menuChoice = {"1.Menü hinzufügen", "2.Menü löschen", "3.Beenden"};
        String[] ingredientChoice = {"1.Zutat hinzufügen", "2.Zutat löschen", "3.Beenden"};
        String[] cityChoice = {"1.Stadt hinzufügen", "2.Stadt löschen", "3.Beenden"};
        Scanner myScanner = new Scanner(System.in);
        Scanner myIntScanner = new Scanner(System.in);
        CatererRepo catRepo = new CatererRepo();


        boolean run = true;
        while (run) {
            System.out.println("Hallo, was möchten sie gerne machen?" + choice);
            int userChoice = myIntScanner.nextInt();
            if (userChoice == 1) {
                printStringArray(menuChoice);
                int userMenuChoice = myIntScanner.nextInt();
                switch (userMenuChoice) {
                    case 1:
                        setMenu(myIntScanner, myScanner, catRepo);
                        break;
                    case 2:
                        deleteMenu(myScanner, catRepo);
                        break;
                    case 3:
                        run = false;
                        break;
                    default:
                        System.out.println("Eingabe wurde nicht erkannt!");
                        break;
                }
                continue;
            }
            if (userChoice == 2) {
                printStringArray(ingredientChoice);
                int userIngredientChoice = myIntScanner.nextInt();
                switch (userIngredientChoice) {
                    case 1:
                        setIngredient(myScanner, catRepo);
                        break;
                    case 2:
                        deleteIngredient(myScanner, catRepo);
                        break;
                    case 3:
                        run = false;
                        break;
                    default:
                        System.out.println("Eingabe wurde nicht erkannt!");
                        break;
                }
                continue;
            }
            if (userChoice == 3) {
                printStringArray(cityChoice);
                int userCityChoice = myIntScanner.nextInt();
                switch (userCityChoice) {
                    case 1:
                        setCity(myScanner, myIntScanner, catRepo);
                        break;
                    case 2:
                        deleteCity(myScanner, catRepo);
                        break;
                    case 3:
                        run = false;
                        break;
                    default:
                        System.out.println("Eingabe wurde nicht erkannt!");
                        break;
                }
                continue;
            }
            if (userChoice == 4) {
                evaluation(catRepo);
                continue;
            }
            if (userChoice == 5) {
                run = false;
                System.out.println("Danke und bis zum nächsten mal.");
                continue;
            } else {
                System.out.println("Eingabe wurde nicht erkannt!");
            }

        }
    }


    private static void setIngredient(Scanner myScanner, CatererRepo catRepo) {
        System.out.println("Bitte geben sie eine Zutat ein:");
        Ingredient ingredient = new Ingredient(myScanner.next());
        catRepo.createIngredient(ingredient);
        System.out.println("Ihre Auswahl wurde hinzugefügt.");
    }

    private static void setMenu(Scanner myIntScanner, Scanner myScanner, CatererRepo catererRepo) {
        System.out.println("Bitte geben sie einen Menünamen ein:");
        String menuName = myScanner.nextLine();
        System.out.println("Bitte geben sie einen Menüpreis ein:");
        double menuPrice = myIntScanner.nextDouble();
        Meal meal = new Meal(menuName, menuPrice);

        boolean run = true;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (run) {
            System.out.println("1.Zutat zu " + menuName + " hinzufügen 2.Beenden");
            int ingredientRun = myIntScanner.nextInt();
            if (ingredientRun == 1) {
                System.out.println("Bitte geben sie eine Zutat ein:");
                String ingredientName = myScanner.next();
                Ingredient ingredient = new Ingredient(ingredientName);
                ingredients.add(ingredient);

                System.out.println("Ihre Auswahl wurde hinzugefügt.");
            } else {
                run = false;
            }
            meal.setIngredients(ingredients);
        }
        catererRepo.createMeal(meal);
    }

    private static void setCity(Scanner myScanner, Scanner myIntScanner, CatererRepo catRepo) {
        City city = new City();
        System.out.println("Bitte geben sie eine Stadt ein:");
        city.setName(myScanner.nextLine());
        System.out.println("Bitte geben sie einen Lieferpreis für " + city.getName() + " ein: ");
        city.setDeliveryPrice(myIntScanner.nextDouble());
        catRepo.createCity(city);
        System.out.println("Ihre Auswahl wurde hinzugefügt.");
    }

    private static void deleteMenu(Scanner myScanner, CatererRepo catRepo) {
        String table = "menu";
        System.out.println("Bitte geben Sie den Menünamen ein welcher gelöscht werden soll!");
        String menuName = myScanner.nextLine();
        System.out.println("Sind sie sicher? " + menuName + " wird gelöscht! Mit JA bestätigen.");
        String yesOrNo = myScanner.nextLine();
        if (yesOrNo.equalsIgnoreCase("JA")) {
            catRepo.delete(menuName, table);
            System.out.println("Ihre Auswahl wurde gelöscht.");
        } else {
            System.out.println("Vorgang abgebrochen!!!");
        }
    }

    private static void deleteCity(Scanner myScanner, CatererRepo catRepo) {
        String table = "city";
        System.out.println("Bitte geben Sie den Stadtnamen ein welcher gelöscht werden soll!");
        String cityName = myScanner.nextLine();
        System.out.println("Sind sie sicher? " + cityName + " wird gelöscht! Mit JA bestätigen.");
        String yesOrNo = myScanner.nextLine();
        if (yesOrNo.equalsIgnoreCase("JA")) {
            catRepo.delete(cityName, table);
            System.out.println("Ihre Auswahl wurde gelöscht.");

        } else {
            System.out.println("Vorgang abgebrochen!!!");
        }

    }

    private static void deleteIngredient(Scanner myScanner, CatererRepo catRepo) {
        String table = "ingredient";
        System.out.println("Bitte geben Sie den Zutatennamen ein welcher gelöscht werden soll!");
        String ingredientName = myScanner.nextLine();
        System.out.println("Sind sie sicher? " + ingredientName + " wird gelöscht! Mit JA bestätigen.");
        String yesOrNo = myScanner.nextLine();
        if (yesOrNo.equalsIgnoreCase("JA")) {
            catRepo.delete(ingredientName, table);
            System.out.println("Ihre Auswahl wurde gelöscht.");
        } else {
            System.out.println("Vorgang abgebrochen!!!");
        }
    }

    private static void printStringArray(String[] text) {
        for (int i = 0; i < text.length; i++) {
            System.out.println(text[i] + "\t");

        }

    }

    private static void evaluation(CatererRepo catRepo) {
        catRepo.evalutionResult();
    }

}
