package testsupport;

import org.apache.commons.math3.special.Gamma;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.pow;
import static java.util.Arrays.stream;

public class VerifyUniformity {

    private static double x2Dist(double[] data) {
        double avg = stream(data).sum() / data.length;
//        System.out.println("avg = " + avg);
        double sqs = stream(data).reduce(0, (a, b) -> a + pow((b - avg), 2));
//        System.out.println("sqs = " + sqs);
        return sqs / avg;
    }

    private static double x2Prob(double dof, double distance) {
        return Gamma.regularizedGammaQ(dof / 2, distance / 2);
    }

    public static boolean x2IsUniform(double[] data, double significance) {
        return x2Prob(data.length - 1.0, x2Dist(data)) > significance;
    }

    public static boolean x2IsUniform(int[] data, double significance) {
        double[] converted = new double[data.length];
        for(int i = 0; i < data.length; i++) converted[i] = (double) data[i];
        return x2IsUniform(converted, significance);
    }

    public static boolean x2IsUniform(ArrayList<Integer> data, double significance) {
        int size = data.size();
        double[] converted = new double[data.size()];
        for(int i = 0; i < size; i++) converted[i] = (double) data.get(i);
        return x2IsUniform(converted, significance);
    }

    //From https://rosettacode.org/wiki/Verify_distribution_uniformity/Chi-squared_test#Java
    public static void main(String[] a) {
        double[][] dataSets = {{199809, 200665, 199607, 200270, 199649},
                {522573, 244456, 139979, 71531, 21461}};

        System.out.printf(" %4s %12s  %12s %8s   %s%n",
                "dof", "distance", "probability", "Uniform?", "dataset");

        for (double[] ds : dataSets) {
            int dof = ds.length - 1;
            double dist = x2Dist(ds);
            double prob = x2Prob(dof, dist);
            System.out.printf("%4d %12.3f  %12.8f    %5s    %6s%n",
                    dof, dist, prob, x2IsUniform(ds, 0.05) ? "YES" : "NO",
                    Arrays.toString(ds));
        }
    }
}