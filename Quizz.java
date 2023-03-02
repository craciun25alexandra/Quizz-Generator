package com.example.project;
import java.util.List;

class Quizz {
    private static int ids = 0;
    private int id;
    String name;
    String username;
    String isCompleted;
    List<Question> Questions;

    public Quizz(String name, String username, List<Question> questions) {
        this.id = ++ids;
        this.name = name;
        Questions = questions;
        isCompleted = "False";
        this.username = username;
    }

    @Override
    public String toString() {
        String result = "Quizz " + "id='" + id +
                "', name='" + name + "', Questions=[";
        for(Question q :Questions)
        {
            result = result + q.toString();
            result += " ";
        }
        result += "], username='" + username + "', isCompleted= '" + isCompleted + "'";
        return result;
    }

    public static void setIds(int ids) {

        Quizz.ids = ids;
    }
}