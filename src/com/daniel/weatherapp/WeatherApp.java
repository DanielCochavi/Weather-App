package com.daniel.weatherapp;

import jdk.jfr.Frequency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class WeatherApp {

    public final static String ipStackAPIKey = "a8052a46dacb7db744149923ea1aa419";
    public final static String ipStackUrl = "http://api.ipstack.com/check?access_key=" + ipStackAPIKey;

    public static void checkIfRaining() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        ArrayList<Future<Boolean>> futures = new ArrayList<>(); // we use Future in order to get answer from

        futures.add(executor.submit(() -> {
            try {
                WeatherStackAPI wsa = new WeatherStackAPI();
                return wsa.isRaining(wsa.getWeatherAPI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }));

        futures.add(executor.submit(() -> {
            try {
                WeatherBitAPI wba = new WeatherBitAPI();
                return wba.isRaining(wba.getWeatherAPI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }));

        futures.add(executor.submit(() -> {
            try {
                OpenWeatherMapAPI owma = new OpenWeatherMapAPI();
                return owma.isRaining(owma.getWeatherAPI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }));

        boolean isRaining = false;
        while (!isRaining) {
            ListIterator<Future<Boolean>> iter = futures.listIterator();
            while (iter.hasNext()) {
                final Future nextFuture = iter.next(); // Because hasNext and next are not called on the same thread,
                // we cannot guaranteed that there will actually be a next when you call next().
                // so by creating final variable, hasNext and next in the same thread, making sure each hasNext is
                // immediately matched by a call to next.
                if (nextFuture.isDone()) {
                    isRaining = (boolean) nextFuture.get();
                    iter.remove(); // using 'ListIterator' we can manage the removing operation while iterating.
                    if (isRaining) {
                        isRaining = true;
                        break;
                    }
                }
            }
            if (futures.size() == 0) {
                break;
            }
        }

        for (Future future : futures) {
            future.cancel(true);
        }

        if (isRaining) {
            System.out.println("It's raining!");
        } else {
            System.out.println("It's not raining");
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int interval = 1;
        final int requestLimit = 5;
        IntervalTimeLimiter itl = new IntervalTimeLimiter(interval, requestLimit);
        String cmd = "";
        while (!cmd.equals("quit")) {
            if (itl.isRequestAllowed()) {
                checkIfRaining();
                System.out.println("Request made");
            }
            else {
                System.out.println("You can check only " + requestLimit + " times in " + interval + " minute");
            }
            Scanner myObj = new Scanner(System.in);
            System.out.println("To check if raining once again press enter, to exit enter 'quit'");
            cmd = myObj.nextLine();
        }
    }
}

