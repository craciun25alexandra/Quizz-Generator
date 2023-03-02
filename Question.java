package com.example.project;
import java.util.List;

class Question {
    public static int ids = 0;
    private int id;
    String type;
    String question;
    List<Answer> Answers;


    public Question(String question, String type, List<Answer> answers) {
        this.id = ++ids;
        this.question = question;
        Answers = answers;
        this.type = type;
    }
    public Question(String id, String question, String type, List<Answer> answers) {
        this.id = Integer.parseInt(id);
        this.question = question;
        Answers = answers;
        this.type = type;
    }

    @Override
    public String toString() {
        String result = "Question " + "id='" + id +
                "', question='" + question + "', type = '" + type + "', Answers ";
        for(Answer a :Answers)
        {
            result = result + a.toString();
            //result += " ";
        }

        return result;
    }
    public static void setIds(int id) {

        ids = id;
    }
}
