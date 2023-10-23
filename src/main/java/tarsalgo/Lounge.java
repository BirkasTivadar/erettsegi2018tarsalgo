package tarsalgo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lounge {

    private final List<PersonMove> personMoveList = new ArrayList<>();

    public void loadFromFile(Path path) {
        String line;
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            while ((line = bufferedReader.readLine()) != null) {
                createAndAddToList(line);
            }
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not read file", ioException);
        }
    }

    public Integer getFirstEntrant(LocalTime start) {
        PersonMove personMove = personMoveList.stream()
                .filter(szm -> szm.time().isAfter(start.minusMinutes(1)) && szm.direction() == Direction.IN)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rossz időpont"));
        return personMove.id();
    }

    public Integer getLastExit(LocalTime end) {
        List<PersonMove> exits = personMoveList.stream()
                .filter(szm -> szm.time().isBefore(end.plusMinutes(1)) && szm.direction() == Direction.OUT)
                .toList();

        if (exits.isEmpty()) {
            System.out.println("Rossz időpont");
        }
        return exits.get(exits.size() - 1).id();
    }

    public Map<Integer, Long> getThroughsTheDoor() {
        return personMoveList.stream()
                .map(PersonMove::id)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public void writeToFile(Path path) {
        Map<Integer, Long> throughsTheDoor = getThroughsTheDoor();
        SortedSet<Integer> sortedKeys = new TreeSet<>(throughsTheDoor.keySet());

        writeBySortedKeys(path, sortedKeys, throughsTheDoor);
    }

    public void printListInFinish() {
        System.out.print("A végén a társalgóban voltak: ");
        getIdListInLoungeFinish()
                .forEach(n -> System.out.print(String.valueOf(n).concat(" ")));
    }


    private LocalTime getMostInLoungeTime() {
        int counter = 0;
        int max = 0;
        LocalTime time = null;
        time = getLocalTime(counter, max, time);
        return time;
    }


    public void printMostInLoungeTime() {
        System.out.printf("Például %tR-kor voltak a legtöbben a társalgóban. ", getMostInLoungeTime());
    }

    public void printInLoungeById(int id) {
        System.out.println(getInLoungeById(id));
    }

    public void printMinutesInLoungeById(int id) {
        int minutes = 0;
        boolean inLongue = false;
        String[] timesInLongue = getInLoungeById(id).split(System.lineSeparator());
        for (String line : timesInLongue) {
            if (line.length() != 11) {
                line = line.concat("15:00");
                inLongue = true;
            }
            minutes = getMinutes(line, minutes);
        }

        String isIn = inLongue ? "" : "nem ";

        System.out.printf("A(z) %d. személy összesen %d percet volt bent, a megfigyelés végén %sa társalgóban volt.", id, minutes, isIn);
    }

    private LocalTime getLocalTime(int counter, int max, LocalTime time) {
        for (PersonMove personMove : personMoveList) {
            if (personMove.direction() == Direction.IN) counter++;
            if (personMove.direction() == Direction.OUT) counter--;

            if (counter > max) {
                max = counter;
                time = personMove.time();
            }
        }
        return time;
    }

    private static int getMinutes(String line, int minutes) {
        String[] times = line.split("-");
        String[] startArray = times[0].split(":");
        String[] endArray = times[1].split(":");
        LocalTime start = LocalTime.of(Integer.parseInt(startArray[0]), Integer.parseInt(startArray[1]));
        LocalTime end = LocalTime.of(Integer.parseInt(endArray[0]), Integer.parseInt(endArray[1]));
        minutes += (int) start.until(end, ChronoUnit.MINUTES);
        return minutes;
    }

    private String getInLoungeById(int id) {
        StringBuilder result = new StringBuilder();
        personMoveList.stream()
                .filter(pm -> pm.id() == id).
                forEach(pm -> result
                        .append(pm.direction() == Direction.IN ? String.format("%tR-", pm.time())
                                : String.format("%tR", pm.time()).concat(System.lineSeparator())));

        return result.toString();
    }

    private List<Integer> getIdListInLoungeFinish() {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Long> throughsTheDoor = getThroughsTheDoor();
        SortedSet<Integer> sortedKeys = new TreeSet<>(throughsTheDoor.keySet());
        sortedKeys.stream()
                .filter(k -> throughsTheDoor.get(k) % 2 == 1)
                .forEach(result::add);
        return result;
    }

    private static void writeBySortedKeys(Path path, SortedSet<Integer> sortedKeys, Map<Integer, Long> ajtoathaladasok) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Integer key : sortedKeys) {
                bufferedWriter.write(String.valueOf(key)
                        .concat(" ")
                        .concat(String.valueOf(ajtoathaladasok.get(key))
                                .concat(System.lineSeparator())));
            }
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not write file", ioException);
        }
    }

    private void createAndAddToList(String line) {
        String[] personMoveArray = line.split(" ");
        LocalTime time = LocalTime.of(Integer.parseInt(personMoveArray[0]), Integer.parseInt(personMoveArray[1]));
        Integer id = Integer.parseInt(personMoveArray[2]);
        Direction direction = personMoveArray[3].equals("be") ? Direction.IN : Direction.OUT;
        personMoveList.add(new PersonMove(time, id, direction));
    }
}
