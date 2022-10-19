[![Maven Central](https://img.shields.io/maven-central/v/io.github.gonguasp/benchmark.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.gonguasp%22%20AND%20a:%22benchmark%22)

# Benchmark

### How it works:
Add the annotation @Benchmark to the method you want to know how fast it is.
After the Spring Boot application is started, all the method which have that
annotation will run automatically and at the end, it will give you a summary of
every method run and its execution time.

It is possible to config the annotations with multiples values:  
* Set the unit time in seconds, milliseconds, nanoseconds and some others.
* How many times a method should run.
* How many warmups the method should run.

See the next example ->  
@Benchmark (timeUnits = TimeUnit.NANOSECONDS, iterations = 10, warmups = 4)