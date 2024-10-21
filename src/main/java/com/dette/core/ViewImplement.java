package com.dette.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public abstract class ViewImplement<T> implements View<T> {
    protected static Scanner scanner;

    public ViewImplement(Scanner scanner) {
        ViewImplement.scanner = scanner;
    }

    // ------------------------------------------------------------------------------------------------------------

    @Override
    public void afficher(List<T> listes) {
        for (T t : listes) {
            System.out.println(t);
        }
    }

    // ------------------------------------------------------------------------------------------------------------

    public static LocalDate formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    // ------------------------------------------------------------------------------------------------------------
    public static LocalTime formatHeure(String heure) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(heure, formatter);
    }

    // ----------------------------------------------------------------------------------------------

    @Override
    public boolean askToContinue() {
        int choix;
        do {
            System.out.println("1- Oui ");
            System.out.println("2- Non");
            choix = scanner.nextInt();
        } while (choix < 1 || choix > 2);
        scanner.nextLine();
        return choix == 1;

    }
}
