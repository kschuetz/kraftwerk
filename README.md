# kraftwerk

[![kraftwerk](https://img.shields.io/maven-central/v/dev.marksman/kraftwerk.svg)](http://search.maven.org/#search%7Cga%7C1%7Cdev.marksman.kraftwerk)
[![Javadoc](https://javadoc-badge.appspot.com/dev.marksman/kraftwerk.svg?label=javadoc)](https://kschuetz.github.io/kraftwerk/javadoc/)
[![CircleCI](https://circleci.com/gh/kschuetz/kraftwerk.svg?style=svg)](https://circleci.com/gh/kschuetz/kraftwerk)

#### Table of Contents
 - [What is it?](#what-is-it)
 - [Tutorial](#tutorial)
 - [Generators](#generators)
 - [License](#license)

# <a name="what-is-it">What is it?</a>

*kraftwerk* is a purely-functional Java library for (pseudo-)randomly generating values of simple or complex data types. It provides several built-in "generators" that can used by themselves, or composed with other generators in arbitrarily complex ways. 
     
The property testing framework [Gauntlet](https://github.com/kschuetz/gauntlet) uses *kraftwerk* for sample generation. However, *kraftwerk* is designed to be general purpose and is not limited to the application of property testing.  

*kraftwerk* requires Java 1.8 or higher. It depends on [lambda](https://github.com/palatable/lambda) and supports the generation of several *lambda* types.

# <a name="tutorial">Tutorial</a>

Several built-in generators can be found in the [`dev.marksman.kraftwerk.Generators`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generators.html) package.  We will start with [`generateInt`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generators.html#generateInt--):

### Generating integers

The following example will generate a supply of random integers, and print the first five to the console.

```java
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class IntegerExample {
    public static void main(String[] args) {
        generateInt()
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // -806894999
        // -2088055255
        // 519165596
        // -247082188
        // 2073514567
    }
}
```      

## Specifying a range

The integers in the above example will all lie between `Integer.MIN_VALUE` and `Integer.MAX_VALUE`.  
If we want to limit the integers to a specific range, there is another version of `generateInt` that accepts a range parameter.
The following will limit the output to be between 1 and 100 (inclusive):

```java 
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class IntegerWithinRangeExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(1).to(100))
                .run()
                .take(5)
                .forEach(System.out::println);
        // sample output:
        // 48
        // 82
        // 24
        // 41
        // 32
    }
}
```       

### Choosing an initial seed

The `run` method we used previously randomly generates a new initial seed each time it is called. You may have observed that that isn't purely functional at all!
That is true; it is a method that's provided for convenience, useful for quick examples like these. 

There *is* a version of `run` that is pure.  In the pure version, you need to pass in an initial `Seed`.  The same seed value will yield the same sequence every time.

```java
package examples.tutorial;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class InitialSeedExample {
    public static void main(String[] args) {
        Seed initialSeed = Seed.create(123456L);
        generateInt(IntRange.from(1).to(100))
                .run(initialSeed)
                .take(5)
                .forEach(System.out::println);

        // output:
        // 24
        // 48
        // 68
        // 86
        // 39    
        // These will be the same on every run because we are using the same initial seed.
    }
}
```                          

### Mapping a generator

A generator can be "mapped" using `fmap`. `fmap` maps the output of a generator to a new value using a function, and yields a new generator.
The following example multiplies the initial generator's output by 1000:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class MappingExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(0).to(100))
                .fmap(n -> n * 1000)
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // 64000
        // 34000
        // 60000
        // 58000
        // 61000
    }
}
```    

### Mapping to a different type

The function passed to `fmap` does not need to return the same type as the input. The following example converts a generator
of `Integer`s to a generator of `LocalDate`s:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import java.time.LocalDate;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class MappingToADifferentType {
    public static void main(String[] args) {
        generateInt(IntRange.from(0).to(100))
                .fmap(n -> LocalDate.of(2020, 1, 1).plusDays(n))
                .run()
                .take(5)
                .forEach(System.out::println);
        
        // sample output:
        // 2020-02-27
        // 2020-03-08
        // 2020-01-19
        // 2020-04-09
        // 2020-01-03
    }
}
```        

### Combining generators

Two or more (up to eight) generators can be combined to create a generator of `Tuple`s, using `Generators.tupled`:

```java
package examples.tutorial;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateString;

public class CombiningTwoGenerators {
    public static void main(String[] args) {
        Generator<Tuple2<Integer, String>> generator = Generators.tupled(generateInt(),
                generateString());

        generator.run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // HList{ 1085224429 :: Sp`b}tM#@E|r }
        // HList{ -354995125 :: Zh:b4 }
        // HList{ -41728349 :: C8T[8aD }
        // HList{ 981101761 :: 'z }
        // HList{ -1434780244 :: uX }
    }
}
```        

Here is another example that combines three generators:

```java
package examples.tutorial;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateString;

public class CombiningThreeGenerators {
    public static void main(String[] args) {
        Generator<Tuple3<Integer, String, Double>> generator = Generators.tupled(generateInt(),
                generateString(),
                generateDoubleFractional());

        generator.run()
                .take(5)
                .forEach(System.out::println);
        
        // sample output:
        // HList{ 1730204138 :: A(@'y)p#e: :: 0.11402224544546236 }
        // HList{ 1909756109 :: ';B :: 0.9884475029496926 }
        // HList{ 1809180523 :: "W>.<eS :: 0.5097816977203855 }
        // HList{ -540828092 :: ^Tld^2a#C}>N6U@ :: 0.7904007899645681 }
        // HList{ -829429249 ::  :: 0.3125739749760317 }
    }
}
```        

### Combining into a custom type

If you would prefer a product type other than `Tuple`, you can use `Generators.product`. This takes the component generators,
and a function to apply to all of the generated components in order to create the desired type.  Here is an example that
generates values of a custom type `RGB`:
                                       
```java
package examples.tutorial;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class CustomProductTypesExample {
    public static void main(String[] args) {
        Generator<Integer> component = generateInt(IntRange.inclusive(0, 255));
        Generator<RGB> generateRGB = Generators.product(component, component, component, RGB::new);

        generateRGB.run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // RGB{red=121, green=48, blue=174}
        // RGB{red=193, green=0, blue=18}
        // RGB{red=201, green=76, blue=22}
        // RGB{red=221, green=221, blue=118}
        // RGB{red=188, green=169, blue=66}
    }

    public static class RGB {
        private final int red;
        private final int green;
        private final int blue;
        public RGB(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
        public int getRed() { return red; }
        public int getGreen() { return green; }
        public int getBlue() { return blue; }
        
        @Override
        public String toString() {
            return "RGB{" +
                    "red=" + red +
                    ", green=" + green +
                    ", blue=" + blue +
                    '}';
        }
    }
}                         
```       

### Generating `Collection`s

`Generators` contains some built-in generators for building collections. These generators take as a parameter a generator for its elements.
The following example generates `ArrayList`s of integers:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateArrayList;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListExample {
    public static void main(String[] args) {
        generateArrayList(generateInt(IntRange.from(1).to(10)))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //[3, 3, 8, 4, 4, 3, 7, 9]
        //[3, 2, 7, 3, 9, 1]
        //[9, 8, 10, 4, 3, 4, 9, 3, 1, 6, 7, 6, 3]
        //[3, 3, 10, 10]
        //[8, 1]
        //[6, 3, 7, 3, 5, 2, 3, 3, 6, 8, 1, 5, 9]
        //[]
        //[8, 7, 8]
        //[8, 2, 10, 5]
        //[9, 9, 8, 1, 2, 9]
    }
}
```

Notice that the lists that were generated are of various sizes, including empty.

### Controlling the size of a generated collection

Most collection generators allow you to specify the size of the collection.  This example generates `ArrayList`s of length 5:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateArrayListOfSize;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListOfSizeExample {
    public static void main(String[] args) {
        generateArrayListOfSize(5, generateInt(IntRange.from(1).to(10)))
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        //[9, 9, 2, 7, 5]
        //[6, 5, 7, 5, 1]
        //[8, 7, 4, 7, 7]
        //[7, 8, 7, 4, 3]
        //[3, 3, 10, 5, 3]
    }
}
```   

You can also specify a size range:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListOfSizeRangeExample {
    public static void main(String[] args) {
        Generators.generateArrayListOfSize(IntRange.from(1).to(7),
                generateInt(IntRange.from(1).to(10)))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //[1, 5, 5, 3, 4, 8]
        //[8, 4, 5, 6, 3]
        //[1, 2, 3]
        //[10, 2, 4, 4, 2]
        //[4, 10, 2, 7, 6, 3, 10]
        //[4, 3, 6, 1, 2, 6]     
        //[3]
        //[9, 8, 10]
        //[4, 6, 3, 3, 1, 5, 4]
        //[2]
    }
}
```  

There are generators for other collection types as well, such as `Map`s and `Set`s.  The following example generates maps that have a characters for keys and integer for values:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import static dev.marksman.kraftwerk.Generators.generateChar;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateMap;

public class MapExample {
    public static void main(String[] args) {
        Generator<Character> keyGenerator = generateChar(CharRange.from('A').to('Z'));
        Generator<Integer> valueGenerator = generateInt(IntRange.from(1).to(10));
        generateMap(keyGenerator, valueGenerator)
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //{B=2, R=3, S=3, F=7, W=2, I=2, Y=9, O=3}
        //{Q=6, C=1, T=3, E=2, G=6, I=10, J=9, L=5, M=7}
        //{}
        //{P=10, A=5, B=7, S=7, T=4, W=3, G=3, H=1, Y=7, M=2, N=5, O=1}
        //{T=7, U=6, E=4, W=6, I=8, K=7, N=3}
        //{G=7, H=6, I=6, J=5, M=4, N=10}
        //{}
        //{}
        //{A=9, C=6, E=2, I=1, J=6, K=4, L=2, M=7, N=1, P=9, Q=3, S=4, U=9, W=1, X=8}
        //{B=8, D=4, T=1, E=10, G=1, Y=8, I=6, L=10, M=2, O=1}
    }
}
```

### Choosing from a set of items

Use `chooseOneOfValues` to choose from a set of one or more items:

```java
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;

public class RainbowExample {
    public static void main(String[] args) {
        chooseOneOfValues("red", "orange", "yellow", "green", "blue", "indigo", "violet")
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:        
        //violet
        //green
        //orange
        //violet
        //yellow
        //red
        //yellow
        //green
        //blue
        //violet
    }
}
```

`chooseOneOf` is similar to `chooseOneOfValues`, but it takes `Generator`s as parameters rather than the values themselves:

```java       
package examples.tutorial;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.CharRange;
import static dev.marksman.kraftwerk.Generators.chooseOneOf;
import static dev.marksman.kraftwerk.Generators.generateChar;

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
```         

### Choosing from a set of weighted items

Both `chooseOneOf` and `chooseOneOfValues` randomly select from their argument lists with an equal probability for each argument.
If you want some items to occur more than others, you can use `chooseOneOfWeightedValues` or `chooseOneOfWeighted`:

```java     
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeightedValues;
import static dev.marksman.kraftwerk.Weighted.weighted;

public class WeightedRainbowExample {
    public static void main(String[] args) {
        chooseOneOfWeightedValues(weighted(7, "red"),
                weighted(6, "orange"),
                weighted(5, "yellow"),
                weighted(4, "green"),
                weighted(3, "blue"),
                weighted(2, "indigo"),
                weighted(1, "violet"))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //red
        //orange
        //orange
        //violet
        //red
        //orange
        //yellow
        //red
        //red
        //green
    }
}
```

In the following example, the cardinal directions (N, S, W, E) will occur 8 times more frequently than the intercardinal directions (NW, NE, SW, SE):

```java           
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.chooseOneOfWeighted;

public class CardinalDirectionsExample {
    public static void main(String[] args) {
        chooseOneOfWeighted(chooseOneOfValues("N", "S", "W", "E").weighted(8),
                chooseOneOfValues("NW", "NE", "SW", "SE").weighted(1))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //W
        //S
        //E
        //N
        //W
        //S
        //SW
        //W
        //SE
        //N
    }
}
```

### `FrequencyMap`s

A `FrequencyMap` is an alternate way to express weights for several values and/or generators.  They can be instantiated using `FrequencyMap.frequencyMap` or `FrequencyMap.frequencyMapFirstValue`.  Several weighted generators or values can then be added.  To convert it to a `Generator` call `toGenerator`.

To be valid, a `FrequencyMap` must have at least one entry with a positive weight.

Note that a `FrequencyMap` is immutable.  All calls to the `add` methods yield a *new* `FrequencyMap`, while leaving the old one intact.

The following example is semantically equivalent to the `RainbowExample` above: 

```java   
package examples.tutorial;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMapFirstValue;

public class RainbowFrequencyMapExample {
    public static void main(String[] args) {
        frequencyMapFirstValue(weighted(7, "red"))
                .addValue(weighted(6, "orange"))
                .addValue(weighted(5, "yellow"))
                .addValue(weighted(4, "green"))
                .addValue(weighted(3, "blue"))
                .addValue(weighted(2, "indigo"))
                .addValue("violet")
                .toGenerator()
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:        
        //red
        //orange
        //yellow
        //green
        //green
        //orange
        //yellow
        //yellow
        //blue
        //yellow        
    }
}
```
This example is semantically equivalent to the `CardinalDirectionsExample` above.

```java
package examples.tutorial;
import dev.marksman.kraftwerk.Generator;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;

public class CardinalDirectionsFrequencyMapExample {
    public static void main(String[] args) {
        Generator<String> cardinals = chooseOneOfValues("N", "S", "W", "E");
        Generator<String> interCardinals = chooseOneOfValues("NW", "NE", "SW", "SE");

        frequencyMap(cardinals.weighted(8))
                .add(interCardinals)
                .toGenerator()
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //W
        //N
        //W
        //SE
        //W
        //SW
        //N
        //E
        //E
        //E
    }
}     
```

# <a name="generators">Generators</a>

A [`Generator<A>`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generator.html) is a strategy for generating random values of type `A`.  Several built-in `Generator`s are provided as static methods in [`dev.marksman.kraftwerk.Generators`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generators.html).

# <a name="license">License</a>

*kraftwerk* is distributed under [The MIT License](http://choosealicense.com/licenses/mit/).

The MIT License (MIT)

Copyright (c) 2019 Kevin Schuetz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
