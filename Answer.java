package com.example.project;

class Answer {
    private static int ids = 0;
    public int id;

    String answer;
    boolean value;

    public Answer(String answer, boolean value) {
        this.id = ++ids;
        this.answer = answer;
        this.value = value;

    }

    public Answer(String id, String answer, String value) {
        this.id = Integer.parseInt(id);
        this.answer = answer;
        this.value =  Boolean.parseBoolean(value);
    }
    public String toString() {
        return "{" +
                "id='" + id +
                "', answer='" + answer +
                "', value='" + value +
                "'}";
    }

    public static void setIds(int id) {

        ids = id;
    }
}
