package lu;

import java.util.Random;
import java.lang.Thread;
import java.lang.System;
import java.lang.InterruptedException;

public class CoinFlip implements Runnable{
	public int numIterations;
	private  int headCount = 0;	

	Random r =  new Random();

	public CoinFlip(int numIterations){
		this.numIterations = numIterations;
	}
	protected boolean flip(){return r.nextBoolean();}

	protected  void increaseHead(){
			headCount++;
	}
	
	public int getCount(){return headCount;}
	public void run(){
		for(int i=0; i<numIterations; i++){
			if(flip()) increaseHead();
		}
	}
	
	public static void main(String args[]){
		if(args.length != 2){
			System.out.println("Usage: CoinFlip #threads #iterations");
			return;
		}
		
		long startTime = System.nanoTime();

		int numThreads = Integer.parseInt(args[0]);
		int totalIterations = Integer.parseInt(args[1]);
		int itrPerThread = totalIterations/numThreads;
		int remain = totalIterations%numThreads;
		int heads = 0;
		Thread[] thread = new Thread[numThreads];
		CoinFlip[] coinflips = new CoinFlip[numThreads];

		coinflips[0] = new CoinFlip(itrPerThread+remain);
		thread[0] = new Thread(coinflips[0]);
		thread[0].start();
		for(int i=1; i<numThreads; i++){
			coinflips[i] = new CoinFlip(itrPerThread);
			thread[i] = new Thread(coinflips[i]);
			thread[i].start();
		}

		for(int i=0; i<numThreads; i++){
			try{thread[i].join();}
			catch(InterruptedException e){
				System.out.println("Thread interrupted."+e.toString());
			}
		}
		for(CoinFlip c: coinflips){
			heads+=c.getCount();
		}
		long timeSpan = System.nanoTime() - startTime;
		System.out.println(""+heads+" heads in "+totalIterations+" coin tosses");
		System.out.println("Elapsed time: "+Long.toString(timeSpan/1000000)+" ms");
	}

}
