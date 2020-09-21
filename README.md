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

The following example will generate a supply of random integers, and print the first five to the console.

```java
import dev.marksman.kraftwerk.Generators;

public static class IntegerExample {
    public static void main(String[] args) {
        Generators.generateInt()
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

The integers in the above example will all lie between `Integer.MIN_VALUE` and `Integer.MAX_VALUE`.  
If we want to limit the integers to a specific range, there is another version of `generateInt` that accepts a range parameter.
The following will limit the output to be between 1 and 100 (inclusive):

```java 
public static class IntegerWithinRangeExample {
    public static void main(String[] args) {
        Generators.generateInt(IntRange.from(1).to(100))
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

The `run` method we used previously randomly generates a new seed each time it is called. If we have a specific
initial seed value we would like to use, we can pass it in as a parameter:

```java
public static class InitialSeedExample {
    public static void main(String[] args) {
        Seed initialSeed = Seed.create(123456L);
        Generators.generateInt(IntRange.from(1).to(100))
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

A generator can be "mapped" using `fmap`. `fmap` maps the output of a generator to a new value using a function, and yields a new generator.
The following example multiplies the initial generator's output by 1000:

```java
public static class MappingExample {
    public static void main(String[] args) {
        Generators.generateInt(IntRange.from(0).to(100))
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

The function passed to `fmap` does not need to return the same type as the input. The following example converts a generator
of `Integer`s to a generator of `LocalDate`s:

```java
public static class MappingToADifferentType {
    public static void main(String[] args) {
        Generators.generateInt(IntRange.from(0).to(100))
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

Two or more (up to eight) generators can be combined to create a generator of `Tuple`s, using `Generators.tupled`:

```java
public static class CombiningTwoGenerators {
    public static void main(String[] args) {
        Generator<Tuple2<Integer, String>> generator = Generators.tupled(Generators.generateInt(),
                Generators.generateString());

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
public static class CombiningThreeGenerators {
    public static void main(String[] args) {
        Generator<Tuple3<Integer, String, Double>> generator = Generators.tupled(Generators.generateInt(),
                Generators.generateString(),
                Generators.generateDoubleFractional());

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

If you would prefer a product type other than `Tuple`, you can use `Generators.product`. This takes the component generators,
and a function to apply to all of the generated components in order to create the desired type.  Here is an example that
generates values of a custom type `RGB`:
                                       
```java
public static class CustomProductTypes {
        public static void main(String[] args) {
            Generator<Integer> component = Generators.generateInt(IntRange.inclusive(0, 255));
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

            public int getRed() {
                return red;
            }

            public int getGreen() {
                return green;
            }

            public int getBlue() {
                return blue;
            }

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
