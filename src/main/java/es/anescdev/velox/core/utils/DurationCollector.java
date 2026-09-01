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
 * {@link java.util.stream.Collector} de utilidad para sumar una colección de
 * {@link java.time.Duration} (p. ej. el total de horas trabajadas de un {@code Sumatory}
 * a partir de sus {@code SumatoryEntry}).
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
