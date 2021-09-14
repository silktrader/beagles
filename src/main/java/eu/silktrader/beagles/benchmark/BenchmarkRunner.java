package eu.silktrader.beagles.benchmark;

import eu.silktrader.beagles.Archiver;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkRunner {

  private final String rootPath = Paths.get("data").toString();

  public static void main(String[] args) throws RunnerException {
    new Runner(new OptionsBuilder().include(BenchmarkRunner.class.getSimpleName()).build()).run();
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Fork(value = 1, warmups = 0)
  @Warmup(iterations = 0)
  @Measurement(iterations = 1)
  public void clearArchives() {

    // clear all zip files in the directory
    Arrays.stream(new File(rootPath).listFiles())
            .filter(f -> f.getName().endsWith(".zip"))
            .forEach(File::delete);

    }

  @Benchmark
  @Fork(value = 1, warmups = 0)
  @Warmup(iterations = 0)
  @Measurement(iterations = 1)
    public void createArchives() throws IOException {
        var archiver = new Archiver();
        Files.list(Paths.get(rootPath))
                  .filter(file -> !Files.isDirectory(file))
                  .map(Path::getFileName)
                .forEach(archiver::archive);

        }
    }
