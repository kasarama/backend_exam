/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.demo.DemoDTO;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;
import java.util.concurrent.Future;

/**
 *
 * @author magda
 */
public class DemoFetch {

    private static String DEMO_CAR_URL = "https://reqres.in/api/car/1";
    private static String DEMO_CHUCK_URL = "https://api.chucknorris.io/jokes/random";
    private static String DEMO_DAD_URL = "https://icanhazdadjoke.com";
    private static String DEMO_EMPLOYEE_URL = "http://dummy.restapiexample.com/api/v1/employee/1";
    private static String DEMO_TARGET_URL = "https://jsonplaceholder.typicode.com/comments/1";

    public static DemoDTO fetchDemo(Gson gson, ExecutorService threadPool) {
        Callable<CarDTO> carTask = new Callable<CarDTO>() {
            @Override
            public CarDTO call() throws IOException {
                String result = HttpUtils.fetchData(DEMO_CAR_URL);

//------------ here is json convrtet to DTO object--------//
                CarData data = gson.fromJson(result, CarData.class);

                return data.getData();
            }
        };

        Future<CarDTO> futureCar = threadPool.submit(carTask);

        Callable<String> chuckTask = new Callable<String>() {
            @Override
            public String call() throws IOException {
                String chuck = HttpUtils.fetchData(DEMO_CHUCK_URL);
                JsonObject json = JsonParser.parseString(chuck).getAsJsonObject();

//------------ here is the chosen property retrieviet from json object--------//
                String joke = json.get("value").getAsString();
                // ChuckDTO chuckDTO = gson.fromJson(chuck, ChuckDTO.class);

                return joke;
            }
        };

        Future<String> futureChuck = threadPool.submit(chuckTask);

        Callable<String> dadTask = new Callable<String>() {
            @Override
            public String call() throws IOException {
                String dad = HttpUtils.fetchData(DEMO_DAD_URL);
                JsonObject json = JsonParser.parseString(dad).getAsJsonObject();

                String joke = json.get("joke").getAsString();

                return joke;
            }
        };

        Future<String> futureDad = threadPool.submit(dadTask);

        Callable<EmployeeDTO> employeeTask = new Callable<EmployeeDTO>() {
            @Override
            public EmployeeDTO call() throws IOException {
                String employee = HttpUtils.fetchData(DEMO_EMPLOYEE_URL);
                JsonObject json = JsonParser.parseString(employee).getAsJsonObject();

//------------ here is the chosen element retrievet from json object--------//                
                JsonElement data = json.get("data");

                EmployeeDTO emplDTO = gson.fromJson(data, EmployeeDTO.class);

                return emplDTO;
            }
        };

        Future<EmployeeDTO> futureEmployee = threadPool.submit(employeeTask);

        Callable<String> commentTask = new Callable<String>() {
            @Override
            public String call() throws IOException {
                String comment = HttpUtils.fetchData(DEMO_TARGET_URL);

                JsonObject json = JsonParser.parseString(comment).getAsJsonObject();
                String data = json.get("body").getAsString();

                return data;
            }
        };

        Future<String> futureComment = threadPool.submit(commentTask);

        CarDTO car;
        try {
            car = futureCar.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            car = new CarDTO("No data for carname", 0);
            ex.printStackTrace();

        }

        String chuck;
        try {
            chuck = futureChuck.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            chuck = "No data for Chuck Norris joke";
        }

        String dad;
        try {
            dad = futureDad.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            dad = "No data for Dad joke";
            ex.printStackTrace();
        }

        EmployeeDTO employee;
        try {
            employee = futureEmployee.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            employee = new EmployeeDTO("No data for employee name", "No data for salary");
            ex.printStackTrace();
        }

        String comment;
        try {
            comment = futureComment.get(15, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            comment = "No Comment!";
            ex.printStackTrace();

        }
        DemoDTO demoDTO = new DemoDTO(car.getName(), car.getYear(), chuck, dad, employee.getName(), employee.getSalary(), comment);
        System.out.println(demoDTO.toString());
        return demoDTO;
    }

}

class CarData {

    private CarDTO data;

    public CarData(CarDTO data) {
        this.data = data;
    }

    public CarDTO getData() {
        return data;
    }

}

class CarDTO {

    private String name;
    private int year;

    public CarDTO(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "CarDTO{" + "name=" + name + ", year=" + year + '}';
    }

}
//employee_name":"Tiger Nixon","employee_salary":

class EmployeeDTO {

    private String employee_name;
    private String employee_salary;

    public EmployeeDTO(String name, String salary) {
        this.employee_name = name;
        this.employee_salary = salary;
    }

    public String getName() {
        return employee_name;
    }

    public String getSalary() {
        return employee_salary;
    }

}
