package examples.tutorial;

import static software.kes.kraftwerk.Generators.generateString;

public class MaybeExample {
    public static void main(String[] args) {
        generateString().maybe()
                .run()
                .take(10)
                .forEach(System.out::println);
    }

    //sample output:
    //Just +_`ysR5:@m
    //Just v/Z.d+RG~o(Kp@i
    //Just =Vf
    //Just yO6p
    //Nothing
    //Just 8R>hq68
    //Just 5?-*{z_R2y
    //Just (^BpsPz}$"nS,
    //Just o{S]0&jn
    //Just oAj
}
