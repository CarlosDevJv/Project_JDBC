package jdbc.application;

import jdbc.model.entities.Department;
import jdbc.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {
        Department department = new Department(1, "Recursos Humanos");
        Seller seller = new Seller(1, "Kleber", "kleber@gmail.com", new Date(), 2500.0, department);

    }
}
