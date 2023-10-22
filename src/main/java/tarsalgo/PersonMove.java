package tarsalgo;

import java.time.LocalTime;

public record PersonMove(LocalTime time, Integer id, Direction direction) {
}
