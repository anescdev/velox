package es.anescdev.velox.core.utils;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author AnesCDev
 */
public class DurationCollector implements Collector<Duration, AtomicLong, Duration> {

    @Override
    public Supplier<AtomicLong> supplier() {
        return () -> new AtomicLong(0);
    }

    @Override
    public BiConsumer<AtomicLong, Duration> accumulator() {
        return (ac, duration) -> ac.addAndGet(duration.toNanos());
    }

    @Override
    public BinaryOperator<AtomicLong> combiner() {
        return (left, right) -> {
            left.addAndGet(right.get());
            return left;
        };
    }

    @Override
    public Function<AtomicLong, Duration> finisher() {
        return ac -> Duration.ofNanos(ac.get());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }

}
