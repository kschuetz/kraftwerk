package examples.tutorial;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.CharRange;

import static software.kes.kraftwerk.Generators.chooseOneOf;
import static software.kes.kraftwerk.Generators.generateChar;

public class LettersExample {
    public static void main(String[] args) {
        Generator<Character> uppercaseLetters = generateChar(CharRange.from('A').to('Z'));
        Generator<Character> lowercaseLetters = generateChar(CharRange.from('a').to('z'));
        chooseOneOf(uppercaseLetters, lowercaseLetters)
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //g
        //X
        //C
        //w
        //S
        //W
        //s
        //q
        //z
        //j
    }
}
