package dto.demo;

/**
 *
 * @author magda
 */
public class DemoDTO {

    private String carName;
    private int carYear;
    private String norrisJoke;
    private String dadJoke;
    private String employeeName;
    private String employeeSalary;
    private String comment;

    public DemoDTO(String carName, int carYear, String chuckJoke, String dadJoke, String empl, String salary, String comment) {
        this.carName = carName;
        this.carYear = carYear;
        this.norrisJoke = chuckJoke;
        this.dadJoke = dadJoke;
        this.employeeName=empl;
        this.employeeSalary=salary;
        this.comment=comment;
    }

    public DemoDTO() {
    }

    @Override
    public String toString() {
        return "DemoDTO{" + "carName=" + carName + ", carYear=" + carYear + ", norrisJoke=" + norrisJoke + ", dadJoke=" + dadJoke + ", employeeName=" + employeeName + ", employeeSalary=" + employeeSalary + ", comment=" + comment + '}';
    }

    

   

}
