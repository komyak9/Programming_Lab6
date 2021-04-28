package parser;

import content.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс для преобразования объектов Worker в xml-файл и наоборот.
 *
 * @autor komyak9
 */
public class WorkerParser implements Serializable {
    public LinkedList<Worker> parseXMLToObject(File file) throws Exception {
        Scanner scanner = new Scanner(file);
        LinkedList<String> data = new LinkedList<>();
        XMLToObjectParser parser = new XMLToObjectParser();
        LinkedList<Worker> result = new LinkedList<>();

        String workerName = null;
        Coordinates coordinates;
        long coordinatesX = -1000;
        int coordinatesY = -1000;
        float salary = 0;

        LocalDateTime startDate = null;
        Date endDate = null;

        Position position = null;
        Organization organization;
        int annualTurnover = 0;
        OrganizationType type = null;
        Address address;
        String zipCode = null;

        Location location;
        String locationName = null;
        Integer locationX = null;
        int locationY = 0, id = 0;
        ZonedDateTime creationDate = null;


        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        try {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).contains("<worker>")) {
                    for (int j = i + 1; j < data.size() && !data.get(i).contains("</worker>"); j++) {
                        if (data.get(j).contains("<name>"))
                            workerName = parser.getSingleValue("name", data.get(j));
                        else if (data.get(j).contains("<id>"))
                            id = Integer.parseInt(parser.getSingleValue("id", data.get(j)));
                        else if (data.get(j).contains("<creationDate>"))
                            creationDate = ZonedDateTime.parse(parser.getSingleValue("creationDate", data.get(j)));
                        else if (data.get(j).contains("<coordinates>")) {
                            coordinatesX = Long.parseLong(parser.getMultiValues("coordinates", data, j).get("x"));
                            coordinatesY = Integer.parseInt(parser.getMultiValues("coordinates", data, j).get("y"));
                        } else if (data.get(j).contains("<startDate>")) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            startDate = LocalDateTime.parse(parser.getSingleValue("startDate", data.get(j)), formatter);
                        } else if (data.get(j).contains("<endDate>"))
                            endDate = new SimpleDateFormat("dd-MM-yyyy").parse(parser.getSingleValue("endDate", data.get(j)));
                        else if (data.get(j).contains("<salary>"))
                            salary = Float.parseFloat(parser.getSingleValue("salary", data.get(j)));
                        else if (data.get(j).contains("<position>"))
                            position = Position.valueOf(parser.getSingleValue("position", data.get(j)));
                        else if (data.get(j).contains("<organization>")) {
                            for (int k = j + 1; k < data.size() && !data.get(k).contains("</organization>"); k++) {
                                if (data.get(k).contains("<annualTurnover>"))
                                    annualTurnover = Integer.parseInt(parser.getSingleValue("annualTurnover", data.get(k)));
                                else if (data.get(k).contains("<type>"))
                                    type = OrganizationType.valueOf(parser.getSingleValue("type", data.get(k)));
                                else if (data.get(k).contains("<address>")) {
                                    for (int l = k + 1; l < data.size() && !data.get(l).contains("</address>"); l++) {
                                        if (data.get(l).contains("<zipCode>"))
                                            zipCode = parser.getSingleValue("zipCode", data.get(l));
                                        else if (data.get(l).contains("<location>")) {
                                            locationName = parser.getMultiValues("location", data, l).get("name");
                                            locationX = Integer.parseInt(parser.getMultiValues("location", data, l).get("x"));
                                            locationY = Integer.parseInt(parser.getMultiValues("location", data, l).get("y"));
                                        }
                                        k = l;
                                    }
                                }
                                j = k;
                            }
                        }
                        i = j;
                    }
                    location = new Location(locationX, locationY, locationName);
                    address = new Address(zipCode, location);
                    organization = new Organization(annualTurnover, type, address);
                    coordinates = new Coordinates(coordinatesX, coordinatesY);
                    result.add(new Worker(id, creationDate, workerName, coordinates, salary, startDate, endDate, position, organization));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return result;
    }

    public void parseObjectToXML(File file, Worker worker) throws IllegalAccessException, IOException {
        Field[] fields = worker.getClass().getDeclaredFields();
        String data = "";
        data += "Element name: " + worker.getClass() + "\n";
        String lineToAdd = "";
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
                lineToAdd = "Tag: " + field.getName() + ", value: " + field.get(worker) + '\n';
                if (lineToAdd.contains("town")) {
                    lineToAdd = lineToAdd.replace("town", "location");
                }
                data += lineToAdd;
            }
        }
        ObjectToXMLParser parser = new ObjectToXMLParser();
        parser.writeDataToXMLFile(file, data, "worker");
    }

    public LinkedList<Worker> getWorkersList(File file) throws Exception {
        Scanner scanner = new Scanner(file);
        XMLToObjectParser parser = new XMLToObjectParser();
        LinkedList<Worker> result = new LinkedList<>();
        ArrayList<String> fileData = new ArrayList<>();
        Date date = null;

        Field[] locationFields = Location.class.getDeclaredFields();
        HashMap<String, String> locationMap = new HashMap<>();
        Location location;

        Field[] addressFields = Address.class.getDeclaredFields();
        HashMap<String, String> addressMap = new HashMap<>();
        Address address;

        Field[] organizationFields = Organization.class.getDeclaredFields();
        HashMap<String, String> organizationMap = new HashMap<>();
        Organization organization;

        Field[] coordinatesFields = Coordinates.class.getDeclaredFields();
        HashMap<String, String> coordinatesMap = new HashMap<>();
        Coordinates coordinates;

        Field[] workerFields = Worker.class.getDeclaredFields();
        HashMap<String, String> workerMap = new HashMap<>();
        Worker worker;

        try {
            while (scanner.hasNextLine())
                fileData.add(scanner.nextLine());
        } catch (Error error) {
            System.out.println(error.getMessage());
        }

        try {
            for (int w = 2; !fileData.get(w).contains("</workers>"); ) {
                if (fileData.get(w).contains("worker")) {
                    fileData.remove(w);
                    for (int i = w; !fileData.get(w).contains("</worker>"); i++) {
                        if (fileData.get(i).contains("<location>")) {
                            for (int j = i + 1; !fileData.get(j).contains("</location>"); ) {
                                for (int k = 0; k < locationFields.length; k++) {
                                    if (fileData.get(j).contains('<' + locationFields[k].getName() + '>'))
                                        locationMap.put(locationFields[k].getName(),
                                                parser.getSingleValue(locationFields[k].getName(), fileData.get(j)));
                                }
                                fileData.remove(j);
                            }
                            fileData.remove(i);
                            fileData.remove(i);
                            break;
                        }
                    }

                    for (int i = w; !fileData.get(w).contains("</worker>"); i++) {
                        if (fileData.get(i).contains("<address>")) {
                            for (int j = i + 1; !fileData.get(j).contains("</address>"); ) {
                                for (int k = 0; k < addressFields.length; k++) {
                                    if (fileData.get(j).contains('<' + addressFields[k].getName() + '>'))
                                        addressMap.put(addressFields[k].getName(),
                                                parser.getSingleValue(addressFields[k].getName(), fileData.get(j)));
                                }
                                fileData.remove(j);
                            }
                            fileData.remove(i);
                            fileData.remove(i);
                            break;
                        }
                    }

                    for (int i = w; !fileData.get(w).contains("</worker>"); i++) {
                        if (fileData.get(i).contains("<organization>")) {
                            for (int j = i + 1; !fileData.get(j).contains("</organization>"); ) {
                                for (int k = 0; k < organizationFields.length; k++) {
                                    if (fileData.get(j).contains('<' + organizationFields[k].getName() + '>'))
                                        organizationMap.put(organizationFields[k].getName(),
                                                parser.getSingleValue(organizationFields[k].getName(), fileData.get(j)));
                                }
                                fileData.remove(j);
                            }
                            fileData.remove(i);
                            fileData.remove(i);
                            break;
                        }
                    }

                    for (int i = w; !fileData.get(w).contains("</worker>"); i++) {
                        if (fileData.get(i).contains("<coordinates>")) {
                            for (int j = i + 1; !fileData.get(j).contains("</coordinates>"); ) {
                                for (int k = 0; k < coordinatesFields.length; k++) {
                                    if (fileData.get(j).contains('<' + coordinatesFields[k].getName() + '>'))
                                        coordinatesMap.put(coordinatesFields[k].getName(),
                                                parser.getSingleValue(coordinatesFields[k].getName(), fileData.get(j)));
                                }
                                fileData.remove(j);
                            }
                            fileData.remove(i);
                            fileData.remove(i);
                            break;
                        }
                    }

                    for (int i = w; !fileData.get(w).contains("</worker>"); ) {
                        for (int k = 0; k < workerFields.length; k++) {
                            if (fileData.get(i).contains('<' + workerFields[k].getName() + '>')) {
                                workerMap.put(workerFields[k].getName(),
                                        parser.getSingleValue(workerFields[k].getName(), fileData.get(i)));
                            }
                        }
                        fileData.remove(i);
                    }

                    fileData.remove(w);

                    if (workerMap.get("endDate") != null)
                        date = new SimpleDateFormat("dd-MM-yyyy").parse(workerMap.get("endDate"));
                    else
                        date = null;

                    location = new Location(Integer.parseInt(locationMap.get("x")), Integer.parseInt(locationMap.get("y")),
                            locationMap.get("name"));
                    address = new Address(addressMap.get("zipCode"), location);
                    organization = new Organization(Integer.parseInt(organizationMap.get("annualTurnover")),
                            OrganizationType.valueOf(organizationMap.get("type")), address);
                    coordinates = new Coordinates(Long.parseLong(coordinatesMap.get("x")),
                            Integer.parseInt(coordinatesMap.get("y")));
                    worker = new Worker(Integer.parseInt(workerMap.get("id")),
                            ZonedDateTime.parse(workerMap.get("creationDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z")), workerMap.get("name"),
                            coordinates, Float.parseFloat(workerMap.get("salary")),
                            LocalDateTime.parse(workerMap.get("startDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            date, Position.valueOf(workerMap.get("position")), organization);
                    result.add(worker);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return result;
    }
}
