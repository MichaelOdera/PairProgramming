import dao.Sql2oPairsDao;
import dao.Sql2oStudentsDao;
import models.Students;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sql2o.Sql2o;
import org.sql2o.Connection;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oStudentsDao studentsDao;
        Sql2oPairsDao pairsDao;

        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/paired";
        Sql2o sql2o = new Sql2o(connectionString, "moringa", "access");

        studentsDao = new Sql2oStudentsDao(sql2o);
        pairsDao = new Sql2oPairsDao(sql2o);

        Map<String, Object> model = new HashMap<String, Object>();

        get("/", (req, res) -> {
            List<Students> allStudents = studentsDao.getAllStudents();
            model.put("students", allStudents);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/pairs", (req, res) -> {
            return new ModelAndView(model, "paired.hbs");
        }, new HandlebarsTemplateEngine());

        post("/students/new",(req,res) ->{
            String name = req.queryParams("studentName");
            Students student = new Students(name);
            studentsDao.addStudent(student);
            return new ModelAndView(model,"success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/pairs/list", (req, res) -> {
            List<Object> myPairs = pairsDao.getCreatedPairs();
            model.put("myPairs", myPairs);
            return new ModelAndView(model, "showing.hbs");
        }, new HandlebarsTemplateEngine());
    }

}
