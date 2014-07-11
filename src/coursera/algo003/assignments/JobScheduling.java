package coursera.algo003.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class JobScheduling {

	private Job[] jobs;

	public JobScheduling(int[][] jobs) {
		this.jobs = new Job[jobs.length];
		for (int i = 0; i < jobs.length; i++) {
			Job j = new Job();
			j.id = i;
			j.weight = jobs[i][0];
			j.length = jobs[i][1];
			j.completionTime = j.length;
			this.jobs[i] = j;
		}
	}

	public Job[] getSchedule(Comparator<Job> comparator) {
		Job[] jobClone = jobs.clone();
		Arrays.sort(jobClone, comparator);
		for (int i = 1; i < jobClone.length; i++)
			jobClone[i].completionTime = jobClone[i].length
					+ jobClone[i - 1].completionTime;

		return jobClone;
	}

	public static long aggregateCost(Job[] scheduled) {
		long cost = 0;
		for (Job j : scheduled)
			cost += (j.completionTime * j.weight);
		return cost;
	}

	class Job {
		int id;
		int weight;
		int length;
		long completionTime;

		@Override
		protected Object clone() throws CloneNotSupportedException {
			Job newJob = new Job();
			newJob.id = this.id;
			newJob.weight = this.weight;
			newJob.length = this.length;
			newJob.completionTime = this.completionTime;
			return newJob;
		}
	}

	public static final Comparator<Job> _DIFF_COMPARATOR = new Comparator<Job>() {

		@Override
		public int compare(Job o1, Job o2) {
			int diff1 = o1.weight - o1.length;
			int diff2 = o2.weight - o2.length;
			if (diff1 == diff2)
				return -1 * (o1.weight - o2.weight);
			else
				return -1 * (diff1 - diff2);
		}
	};

	public static final Comparator<Job> _RATIO_COMPARATOR = new Comparator<Job>() {

		@Override
		public int compare(Job o1, Job o2) {
			double ratio1 = ((double) o1.weight) / o1.length;
			double ratio2 = ((double) o2.weight) / o2.length;
			return -1 * Double.compare(ratio1, ratio2);
		}
	};

	public static void main(String[] args) {
		int[][] wength = readArrayFromFile("./data/jobs.txt");
		JobScheduling js = new JobScheduling(wength);
		Job[] diffSchedule = js.getSchedule(_DIFF_COMPARATOR);
		System.out.println("Diff Order cost: " +  aggregateCost(diffSchedule));

		Job[] ratioSchedule = js.getSchedule(_RATIO_COMPARATOR);
		System.out.println("Ratio Order cost: " +  aggregateCost(ratioSchedule));
	}

	private static int[][] readArrayFromFile(String filePath) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			int N = scanner.nextInt();
			int[][] wength = new int[N][2];
			for (int i = 0; i < N; i++) {
				wength[i][0] = scanner.nextInt();
				wength[i][1] = scanner.nextInt();
			}
			scanner.close();

			return wength;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
