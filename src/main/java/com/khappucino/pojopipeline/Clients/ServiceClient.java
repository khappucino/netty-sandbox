package com.khappucino.pojopipeline.Clients;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.Random;

/**
 * ServiceClient is used to simulate fetching a random integer from a service.
 * ServiceClient executes asynchronously and after a delay of 2 seconds it returns
 * a random integer.
 */
public class ServiceClient {

  private Random rand = new Random();

  // returns a CompletableFuture which will return once the random number is generated
  public CompletableFuture<Integer> computeRandomNumber() { 
    CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
        Thread.sleep(2000);
        int randomNumber = rand.nextInt(1000);
        completableFuture.complete(randomNumber);
        return null;
    });

    return completableFuture;
  }
}