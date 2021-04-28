package parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс-парсер для преобразования полей в строки xml.
 *
 * @autor komyak9
 */
public class ObjectToXMLParser implements Serializable {
    private void writeSingleElement(File file, String tag, String value, int tabCount) throws IOException {
        String line = tabCount(tabCount) + '<' + tag + '>' + value + "</" + tag + ">\n";
        byte[] buffer = line.getBytes();
        FileOutputStream fstream = new FileOutputStream(file, true);
        fstream.write(buffer);
    }

    public void writeSingleElementWithAttributes(File file, String tag, String value, int tabCount, HashMap map) throws IOException {
        String attributes = "";
        String[] keys = (String[]) map.keySet().toArray(new String[0]);
        for (int i = 0; i < map.size(); i++) {
            attributes += ' ' + keys[i] + "=\"" + map.get(keys[i]) + '\"';
        }

        String line = tabCount(tabCount) + '<' + tag + attributes + '>' + value + "</" + tag + ">\n";
        byte[] buffer = line.getBytes();
        FileOutputStream fstream = new FileOutputStream(file, true);
        fstream.write(buffer);
    }

    private void writeMultiValue(File file, String dataLine, int tabCount) throws IOException {     // recursion can be met
        FileOutputStream fstream = new FileOutputStream(file, true);
        String tag = "";
        String value = "";

        Pattern pattern = Pattern.compile("\\{.+}");
        Matcher matcher = pattern.matcher(dataLine);
        while (matcher.find()) {
            dataLine = dataLine.substring(matcher.start(), matcher.end());
            dataLine = dataLine.substring(1, dataLine.length() - 1);            // data from outer {..} by greedy search
        }

        if (!dataLine.contains("{") && !dataLine.contains("}")) {        // if no inner user data types are present (only single values are left)
            String[] data = dataLine.trim().split(", ");
            tabCount += 1;
            for (String line : data) {
                pattern = Pattern.compile(".+?=");
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    tag = line.substring(matcher.start(), matcher.end());
                    tag = tag.substring(0, tag.length() - 1);
                }
                pattern = Pattern.compile("=.+");
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    value = line.substring(matcher.start(), matcher.end());
                    value = value.substring(1);
                }
                writeSingleElement(file, tag, value, tabCount);
            }
            tabCount -= 1;
        } else {
            String currentData = "";
            pattern = Pattern.compile("\\{.+?\\{");
            matcher = pattern.matcher(dataLine);
            while (matcher.find()) {
                currentData = dataLine.substring(matcher.start(), matcher.end());
                currentData = currentData.substring(1, currentData.length() - 1);       // data to write from {...{ (present single values)
            }

            String leftData = "";
            pattern = Pattern.compile("\\{.+?}");
            matcher = pattern.matcher(dataLine);
            while (matcher.find()) {
                leftData = dataLine.substring(matcher.start(), matcher.end());
                leftData = leftData.substring(1);        // data from inner {...}
            }

            String[] data = currentData.trim().split(", ");
            tabCount += 1;
            for (int i = 0; i < data.length; i++) {
                pattern = Pattern.compile(".+?=");
                matcher = pattern.matcher(data[i]);
                while (matcher.find()) {
                    tag = data[i].substring(matcher.start(), matcher.end());
                    tag = tag.substring(0, tag.length() - 1);
                }

                pattern = Pattern.compile("=.+");
                matcher = pattern.matcher(data[i]);
                while (matcher.find()) {
                    value = data[i].substring(matcher.start(), matcher.end());
                    value = value.substring(1);
                    value.toLowerCase();
                }

                if (i == data.length - 1) {                  // to go into inner data from {...}
                    while (value.contains(".")) {            // to get rid of package names of an user data type
                        pattern = Pattern.compile("\\..+?");
                        matcher = pattern.matcher(data[i]);
                        while (matcher.find()) {
                            value = data[i].substring(matcher.start(), matcher.end());
                            value = value.substring(1);
                        }
                    }
                    String element = tabCount(tabCount) + '<' + tag + ">\n";
                    byte[] buffer = element.getBytes();
                    fstream.write(buffer);

                    writeMultiValue(file, leftData, tabCount);      // recursion starts here

                    element = tabCount(tabCount) + "</" + tag + ">\n";
                    buffer = element.getBytes();
                    fstream.write(buffer);
                } else
                    writeSingleElement(file, tag, value, tabCount);
            }
            tabCount -= 1;
        }
    }

    public void writeDataToXMLFile(File file, String data, String elementName) throws IOException {
        FileOutputStream fstream = new FileOutputStream(file, true);
        int tabCount = 1;       // for tabs in the file output
        String tag = "";
        String value = "";
        Pattern pattern;
        Matcher matcher;

        String element = tabCount(tabCount) + '<' + elementName + ">\n";     // an opening outer tag
        byte[] buffer = element.getBytes();
        fstream.write(buffer);

        String[] lines = data.trim().split("\n");       // to go through the data line by line
        for (String line : lines) {
            tabCount += 1;
            if (line.contains("Tag") && line.contains("value")) {
                pattern = Pattern.compile("Tag: .+?,");
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    tag = line.substring(matcher.start(), matcher.end());
                    tag = tag.substring(5, tag.length() - 1);
                }

                pattern = Pattern.compile("value: .+");
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    value = line.substring(matcher.start(), matcher.end());
                    value = value.substring(7);
                }

                if (value.contains("{") && value.contains("}")) {        // for user data types like Address, Town, etc.
                    element = tabCount(tabCount) + '<' + tag + ">\n";        // an opening outer tag
                    buffer = element.getBytes();
                    fstream.write(buffer);

                    writeMultiValue(file, value, tabCount);

                    element = tabCount(tabCount) + "</" + tag + ">\n";       // a closing outer tag
                    buffer = element.getBytes();
                    fstream.write(buffer);
                } else
                    writeSingleElement(file, tag, value, tabCount);
            }
            tabCount -= 1;
        }
        element = tabCount(tabCount) + "</" + elementName + ">\n";       // a closing outer tag
        buffer = element.getBytes();
        fstream.write(buffer);
    }

    private String tabCount(int count) {
        String line = "";
        for (int i = 0; i < count; i++) {
            line += '\t';
        }
        return line;
    }
}
