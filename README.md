# kraftwerk

[![kraftwerk](https://img.shields.io/maven-central/v/dev.marksman/kraftwerk.svg)](http://search.maven.org/#search%7Cga%7C1%7Cdev.marksman.kraftwerk)
[![Javadoc](https://javadoc-badge.appspot.com/dev.marksman/kraftwerk.svg?label=javadoc)](https://kschuetz.github.io/kraftwerk/javadoc/)
[![CircleCI](https://circleci.com/gh/kschuetz/kraftwerk.svg?style=svg)](https://circleci.com/gh/kschuetz/kraftwerk)

#### Table of Contents
 - [What is it?](#what-is-it)
 - [Features](#features)
 - [Installation](#installation)
 - [Examples](#examples)
 - [Tutorial](#tutorial)
    - [Generating integers](#generating-integers)
    - [Specifying a range](#specifying-a-range)
    - [Choosing an initial seed](#choosing-an-initial-seed)
    - [The `run` method](#the-run-method)
    - [Mapping a generator](#mapping-a-generator)
    - [Mapping to a different type](#mapping-to-a-different-type)
    - [Combining generators](#combining-generators)
    - [Combining into a custom type](#combining-into-a-custom-type)
    - [Generating `Collection`s](#generating-collections)
    - [Controlling the size of a generated collection](#controlling-the-size-of-a-generated-collection)
    - [Postfix methods on generators](#postfix-methods-on-generators)
    - [Choosing from a set of items](#choosing-from-a-set-of-items)
    - [Choosing from a set of weighted items](#choosing-from-a-set-of-weighted-items)
    - [`FrequencyMap`s](#frequency-maps)
    - [Composing generators with `flatMap`](#flatMap)
    - [Use with *lambda*](#use-with-lambda)
    - [Filtering](#filtering)
    - [Shuffling](#shuffling)
 - [More on "purely functional"](#purely-functional)
 - [License](#license)

# <a name="what-is-it">What is it?</a>

*kraftwerk* is a purely-functional Java library for (pseudo-)randomly generating values of simple or complex data types. It provides several built-in "generators" that can used by themselves, or composed with other generators in arbitrarily complex ways. 
     
The property testing framework [Gauntlet](https://github.com/kschuetz/gauntlet) uses *kraftwerk* for sample generation. However, *kraftwerk* is designed to be general purpose and is not limited to the application of property testing.  

*kraftwerk* requires Java 1.8 or higher. It depends on [lambda](https://github.com/palatable/lambda) and supports the generation of several *lambda* types.

For more details, check out the [javadoc](https://kschuetz.github.io/kraftwerk/javadoc/).

# <a name="features">Features</a>

*Kraftwerk*'s built-in generators include support for:
* Primitives
* Strings
* Enums
* Collections
* Choosing from sets of items
* Weighting
* Products (tuples, custom product types)
* Coproducts (e.g., `Maybe`, `Either`, `Choice`, `These`)
* Temporal types (e.g., `LocalDate`, `LocalTime`, `LocalDateTime`, `Duration`, `Month`, `DayOfWeek`)
* Shuffling
* Functions
* Ranges

Using combinators like `product` and `flatMap`, these generators can be composed to create more complex generators.

# <a name="installation">Installation</a>

To install, add the dependency to the latest version to your `pom.xml` (Maven) or `build.gradle` (Gradle).

Follow this link to get the dependency info for your preferred build tool:
[![kraftwerk](https://img.shields.io/maven-central/v/dev.marksman/kraftwerk.svg)](http://search.maven.org/#search%7Cga%7C1%7Cdev.marksman.kraftwerk)

# <a name="examples">Examples</a>

Several examples, including the tutorial examples below, can be found in the [src/test/java/examples](https://github.com/kschuetz/kraftwerk/tree/master/src/test/java/examples) directory. [PersonExample](https://github.com/kschuetz/kraftwerk/blob/master/src/test/java/examples/PersonExample.java) is one of the more complex examples.

# <a name="tutorial">Tutorial</a>

A [`Generator<A>`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generator.html) is a strategy for generating random values of type `A`.  Several built-in `Generator`s are provided as static methods in [`dev.marksman.kraftwerk.Generators`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generators.html).

We will start with [`generateInt`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/Generators.html#generateInt--):

### <a name="generating-integers">Generating integers</a>

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

### <a name="specifying-a-range">Specifying a range</a>

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

### <a name="choosing-an-initial-seed">Choosing an initial seed</a>

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

### <a name="the-run-method">The `run` method</a>

What does the `run` method on a `Generator` do?  It returns a [`ValueSupply<A>`](https://kschuetz.github.io/kraftwerk/javadoc/dev/marksman/kraftwerk/ValueSupply.html), which is an infinite `Iterable<A>` with several additional methods for convenience.

Among other things, `ValueSupply`s can be iterated, mapped (using `fmap`), filtered (using `filter`), or converted to a Java `Stream` (using `stream`).  `ValueSupply`s are immutable and can be shared and iterated multiple times.  An instance of a `ValueSupply` will always yield the same sequence every time is is iterated.

### <a name="mapping-a-generator">Mapping a generator</a>

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

### <a name="mapping-to-a-different-type">Mapping to a different type</a>

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

### <a name="combining-generators">Combining generators</a>

Two or more (up to eight) generators can be combined to create a generator of `Tuple`s, using `Generators.generateTuple`:

```java
package examples.tutorial;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.kraftwerk.Generator;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateString;
import static dev.marksman.kraftwerk.Generators.generateTuple;

public class CombiningTwoGenerators {
    public static void main(String[] args) {
        Generator<Tuple2<Integer, String>> generator = generateTuple(generateInt(), generateString());
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
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateString;
import static dev.marksman.kraftwerk.Generators.generateTuple;

public class CombiningThreeGenerators {
    public static void main(String[] args) {
        Generator<Tuple3<Integer, String, Double>> generator = generateTuple(generateInt(),
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

### <a name="combining-into-a-custom-type">Combining into a custom type</a>

If you would prefer a product type other than `Tuple`, you can use `Generators.generateProduct`. This takes the component generators,
and a function to apply to all of the generated components in order to create the desired type.  Here is an example that
generates values of a custom type `RGB`:
                                       
```java
package examples.tutorial;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateProduct;

public class CustomProductTypesExample {
    public static void main(String[] args) {
        Generator<Integer> component = generateInt(IntRange.inclusive(0, 255));
        Generator<RGB> generateRGB = generateProduct(component, component, component, RGB::new);

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

### <a name="generating-collections">Generating `Collection`s</a>

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

### <a name="controlling-the-size-of-a-generated-collection">Controlling the size of a generated collection</a>

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

### <a name="postfix-methods-on-generators">Postfix methods on generators</a>

There are several methods available on `Generator`s themselves for the purpose of generating collections (and other types), such as `.arrayList()`. These are provided for convenience, and behave the same as their counterparts, e.g., `myGenerator.arrayList()` is  equivalent to `generateArrayList(myGenerator)`.

Here is an example that uses `.arrayList()`:

```java 
package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListPostfixExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(1).to(10))
                .arrayList()
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        //[3, 3, 8, 8, 2, 4, 5, 2, 1, 8, 2, 1, 1, 4, 9]
        //[2, 5, 8]
        //[8, 2, 10, 3, 7, 9, 4, 1]
        //[1, 5, 5, 2]
        //[3, 4, 3, 10, 5, 6]
    }
}
```

### <a name="choosing-from-a-set-of-items">Choosing from a set of items</a>

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

### <a name="choosing-from-a-set-of-weighted-items">Choosing from a set of weighted items</a>

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

### <a name="frequency-maps">`FrequencyMap`s</a>

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

### <a name="flatmap">Composing generators with `flatMap`</a>

If you need the parameters of one generator to be determined by the output of another, you can compose these generators using `flatMap`. 

One possible application for `flatMap` is having one generator generate a size, and then have a second generator use that size to generate a collection.

This example uses `flatMap` to generate lists of letters of varying lengths:

```java
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.generateAlphaChar;
import static dev.marksman.kraftwerk.Generators.generateSize;

public class SizedExample1 {
    public static void main(String[] args) {
        generateSize()
                .flatMap(size -> generateAlphaChar().vectorOfSize(size))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //Vector(n, P, e)
        //Vector(w, f, o, W, i, t, y, v, p, i, K, O)
        //Vector(q, W, X, Q, D, S, T, M, D, l, P, p, E, O, K)
        //Vector(b, c, H, N, H, m, I, K)
        //Vector(m, J, Q, F, I, d, Z, C, b)
        //Vector(Q)
        //Vector(d, i, U, o, M, T)
        //Vector(F, S, I, R, s, l, t, V)
        //Vector(Q, V)
        //Vector(B, p, Q)
    }
}
```   

Note that the above example was only for illustrating how to use `flatMap`. `Generators.sized` is available for creating generators that generate things of various sizes.  The following example does the equivalent:

```java
package examples.tutorial;

import static dev.marksman.kraftwerk.Generators.generateAlphaChar;
import static dev.marksman.kraftwerk.Generators.sized;

public class SizedExample2 {
    public static void main(String[] args) {
        sized(size -> generateAlphaChar().vectorOfSize(size))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //Vector(E, t, z, h, G, Q, V, A, P, q, X)
        //Vector(d, W, E, y, p, F, l, N, u, Q, k, C)
        //Vector(d, x, e, C, Z, W, m, t, T, F, J, L, G, Q)
        //Vector(d, C)
        //Vector(G, p, I, T, q, c, X, l, R, w, v, e, m, G, F)
        //Vector(r, f, B, m, v, I)
        //Vector(r, q, o)
        //Vector(x, Q, N, b)
        //Vector(c, A, f, h, J, H, O, T, A, E, o, E, p, B)
        //Vector(z, N, P, F, X, B, B, q, u, U)        
    }
}
```

`sized` and `flatMap` are lower-level operations, and again, the above examples only illustrate how to use them. It will not be very common for you to need to use them for any of the built-in generators; all of the built-in collection generators already allow for specifying a size range. `sized` and `flatMap` remain available for use in building generators for custom collection types, however.

Here is another (contrived) example for `flatMap`:

```java 
package examples.tutorial;
import dev.marksman.kraftwerk.constraints.IntRange;
import java.util.Collections;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ContrivedFlatMapExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(3).to(10))
                .flatMap(size ->
                        chooseOneOfValues("$", "£", "€", "¥")
                                .fmap(ch -> String.join("", Collections.nCopies(size, ch))))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //¥¥¥¥¥¥¥¥¥
        //£££
        //$$$$$$
        //££££££
        //££££££
        //€€€€€€€€
        //$$$$$$$$
        //¥¥¥¥¥¥¥¥¥
        //££££££££££
        //$$$$
    }
}
```    

...and here is a more practical example:

```java 
package examples.tutorial;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.time.LocalDate;
import java.time.Year;

import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;

public class DateOfBirthExample {
    private static final int currentYear = LocalDate.now().getYear();

    private static final Generator<Integer> generateAge =
            frequencyMap(weighted(1, generateInt(IntRange.from(2).to(9))))
                    .add(weighted(2, generateInt(IntRange.from(10).to(19))))
                    .add(weighted(3, generateInt(IntRange.from(20).to(29))))
                    .add(weighted(3, generateInt(IntRange.from(30).to(39))))
                    .add(weighted(3, generateInt(IntRange.from(40).to(49))))
                    .add(weighted(3, generateInt(IntRange.from(50).to(59))))
                    .add(weighted(2, generateInt(IntRange.from(60).to(69))))
                    .add(weighted(2, generateInt(IntRange.from(70).to(79))))
                    .add(weighted(2, generateInt(IntRange.from(80).to(99))))
                    .toGenerator();

    private static final Generator<LocalDate> generateDateOfBirth =
            generateAge.flatMap(age -> generateLocalDateForYear(Year.of(currentYear - age)));

    public static void main(String[] args) {
        generateDateOfBirth
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //1988-10-18
        //2002-05-03
        //1967-01-04
        //1985-12-24
        //1995-04-18
        //1962-11-25
        //1999-04-22
        //1976-02-22
        //1947-08-25
        //1946-03-12
    }
}
```     

The above example generates ages based on a customized distribution, and then `flatMap`s the age into a generator that generators a date for a particular year.

### <a name="use-with-lambda">Use with *lambda*</a>

If you are familiar with *[lambda](https://github.com/palatable/lambda)*, you probably recognize `fmap` and `flatMap` from the `Functor` and `Monad` interfaces, respectively.  `Generator`s implement both of these interfaces.

*kraftwerk* also supports the generation of several *lambda* types.  These include `Maybe`, `Either`, `Choice`, `These`, and (as you have already seen) `Tuple`.

To generate `Maybe<A>`s, simply call `.maybe()` on a `Generator<A>`.  These will create a `Generator<Maybe<A>>` that yields `Nothing` roughly 10% of the time:

```java
package examples.tutorial;
import static dev.marksman.kraftwerk.Generators.generateString;
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
```  

If you want control over how frequently `Nothing` occurs, you can use `MaybeWeights`.  In the following example, `Nothings` will occur about 25% of the time:

```java
package examples.tutorial;
import dev.marksman.kraftwerk.weights.MaybeWeights;
import static dev.marksman.kraftwerk.Generators.generateString;

public class MaybeExampleWithWeights {
    public static void main(String[] args) {
        generateString().maybe(MaybeWeights.justs(3).toNothings(1))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //Just 9oo*=4+f!OCW
        //Just ^oSbZU
        //Just K)[B3:x]ob^8\s
        //Nothing
        //Nothing
        //Just  [cMM
        //Nothing
        //Just L
        //Just :_{MS
        //Just O`0>Q0        
    }
}
```

The following example illustrates generation of some of the other coproduct types in *lambda*:

```java 
package examples.tutorial;
import dev.marksman.kraftwerk.Generators;
import java.time.Year;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateEither;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateMaybe;
import static dev.marksman.kraftwerk.Generators.generateThese;
import static dev.marksman.kraftwerk.Generators.generateTuple;

public class CoProductExample {
    public static void main(String[] args) {
        generateTuple(generateMaybe(generateLocalDateForYear(Year.now())),
                generateEither(generateInt(), Generators.generateDoubleFractional()),
                generateThese(generateLong(), chooseOneOfValues("foo", "bar", "baz")))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //HList{ Just 2020-07-10 :: Left{l=1290020209} :: These{b=baz} }
        //HList{ Just 2020-04-09 :: Right{r=0.480723935139732} :: These{a=3160121273074965838} }
        //HList{ Just 2020-03-18 :: Left{l=891956632} :: These{b=bar} }
        //HList{ Just 2020-09-24 :: Right{r=0.10862715298091574} :: These{both=HList{ -7826076449853900017 :: bar }} }
        //HList{ Just 2020-02-07 :: Right{r=0.5608597418874514} :: These{a=-860230134582980058} }
        //HList{ Just 2020-02-17 :: Left{l=2112321697} :: These{b=bar} }
        //HList{ Just 2020-09-07 :: Right{r=0.709292621079071} :: These{b=baz} }
        //HList{ Just 2020-01-16 :: Left{l=1919118581} :: These{both=HList{ -2545935683596921432 :: baz }} }
        //HList{ Just 2020-03-22 :: Left{l=-1905433010} :: These{b=bar} }
        //HList{ Just 2020-10-02 :: Right{r=0.6120825155638645} :: These{b=baz} }
    }
}
``` 

Notice that `generateMaybe` is also available, in addition to the postfix `.maybe()`.

The following example demonstrates the `ChoiceBuilder` API, which is use to generate *lambda* `Choice` values (of arities from 2-8).
The example generates one of the eight Java primitive types (albeit boxed), with `Integer`s and `Long`s occurring slightly more frequently:

```java 
package examples.tutorial;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.Generator;
import static dev.marksman.kraftwerk.Generators.choiceBuilder;
import static dev.marksman.kraftwerk.Generators.generateAsciiPrintableChar;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateFloatFractional;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateShort;

public class WeightedChoiceExample {
    public static void main(String[] args) {
        Generator<Choice8<Integer, Double, Float, Boolean, Long, Byte, Short, Character>> primitiveGenerator =
                choiceBuilder(generateInt().weighted(2))
                        .or(generateDoubleFractional())
                        .or(generateFloatFractional())
                        .or(generateBoolean())
                        .or(generateLong().weighted(2))
                        .or(generateByte())
                        .or(generateShort())
                        .or(generateAsciiPrintableChar())
                        .toGenerator();

        primitiveGenerator
                .run()
                .take(20)
                .forEach(System.out::println);

        // sample output:
        //Choice8{a=67463646}
        //Choice8{c=0.3798052}
        //Choice8{c=0.5343417}
        //Choice8{e=8542724707820389757}
        //Choice8{e=7723229941486178995}
        //Choice8{a=983401700}
        //Choice8{a=2065183438}
        //Choice8{e=4697885099367790210}
        //Choice8{b=0.42306257966421357}
        //Choice8{e=8807970083702066543}
        //Choice8{a=-1526959351}
        //Choice8{f=-105}
        //Choice8{d=true}
        //Choice8{g=26791}
        //Choice8{f=-81}
        //Choice8{h=0}
        //Choice8{h=#}
        //Choice8{e=6994438299090618730}
        //Choice8{h=,}
        //Choice8{a=-99648217}        
    }
}
```

### <a name="filtering">Filtering</a>

TODO

### <a name="shuffling">Shuffling</a>

TODO

# <a name="purely-functional">More on "purely functional"</a>

All types in the *kraftwerk* API, including builders, are immutable and can be shared safely.  All methods on a class in the *kraftwerk* API that *appear* to mutate will actually create and return a new instance of that object with the change, and leave the original object intact.

All methods in the *kraftwerk* API are also pure and referentially transparent.  There *are* two exceptions to this, but these methods are provided for convenience only:
* The overloads of the `run` methods that do not take an initial seed as a parameter; these will generate a new seed internally each time they are called
* `Seed.random` - this will randomly generate a seed

`Generator` implements `Functor` and `Monad` (from [lambda](https://github.com/palatable/lambda)), so a properly-designed generator should obey the functor and monad laws.

# <a name="license">License</a>

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fkschuetz%2Fkraftwerk.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fkschuetz%2Fkraftwerk?ref=badge_shield)

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
