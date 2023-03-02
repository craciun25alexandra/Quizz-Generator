package com.example.project;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.round;
public class Tema1 {

	public static void main(final String[] args) {

		File users = new File("users.txt");
		File questions = new File("questions.txt");
		File quizzes = new File("quizzes.txt");
		File solutions = new File("solutions.txt");

		while (true) {
			if (args == null) {
				System.out.println("Hello world!");
				break;
			} else if (args[0].equals("-create-user")) {
				if (args.length == 1)
					System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
				else if (args.length == 2)
					System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
				else {

					boolean exist = false;
					String[] usernameInput = args[1].split("'");
					String[] passwordInput = args[2].split("'");
					Scanner myReaderUsers;
					try {
						myReaderUsers = new Scanner(users);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
					while (myReaderUsers.hasNextLine()) {
						String userline = myReaderUsers.nextLine();
						String[] username = userline.split(" ");
						if (username[0].equals(usernameInput[1])) {
							System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
							exist = true;
							break;
						}
					}
					myReaderUsers.close();
					if (!exist) {
						System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
						FileWriter myWriter;
						try {
							myWriter = new FileWriter("users.txt", true);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							myWriter.append(usernameInput[1] + " " + passwordInput[1] + "\n");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							myWriter.close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}

				}

			} else if (args[0].contains("-create-question")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}

				exist = false;
				String[] text = args[3].split("'");
				Scanner myReaderQuestions;
				try {
					myReaderQuestions = new Scanner(questions);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderQuestions.hasNextLine()) {
					String ques = myReaderQuestions.nextLine();
					String[] quesText = ques.split("'");
					if (quesText[3].equals(text[1])) {
						System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
						exist = true;
						break;
					}
				}
				myReaderQuestions.close();
				if (!exist) {
					if (args.length == 5) {
						System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
						break;
					} else if (args.length == 7) {
						System.out.println("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
						break;
					} else if (args.length > 15) {
						System.out.println("{ 'status' : 'error', 'message' : 'More than 5 answers were submitted'}");
						break;
					} else if (!(args[3].contains("-text"))) {
						System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
						break;
					} else if (args[4].contains("single")) {
						int nr = 0;
						int poz = 0;
						//la a 6 a pozitie se afla primul flag de raspuns
						//se numara cate raspunsuri corecte au fost introduse
						while (poz + 6 < args.length && args[poz + 6].contains("-answer") && nr < 2) {
							String[] answer = args[poz + 6].split("'");
							if (answer[1].equals("1"))
								nr++;
							poz += 2;
						}
						if (nr > 1) {
							System.out.println("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
							break;
						}
					}
					List<Answer> answers = new ArrayList<>();
					//la a 5 a pozitie se afla primul nume de raspuns
					String[] valueTrue = args[6].split("'");
					String[] nameAnswer = args[5].split("'");
					if (valueTrue[1].equals("1"))
						answers.add(new Answer(nameAnswer[1], true));
					else
						answers.add(new Answer(nameAnswer[1], false));

					boolean ok = false;
					//primul raspuns a fost adaugat, se trece la urmatorul
					int poz = 2;
					while (poz + 6 < args.length && args[poz + 5].contains("-answer") && !ok) {
						nameAnswer = args[5 + poz].split("'");
						valueTrue = args[6 + poz].split("'");
						Answer a;
						if (valueTrue[1].equals("1"))
							a = new Answer(nameAnswer[1], true);
						else
							a = new Answer(nameAnswer[1], false);
						//este necesara retinerea size ului, deoarece se schimba la fiecare pas in care se adauga un raspuns
						//iar verificarea se face in functie de raspunsurile precedente
						int size = answers.size();
						for (int i = 0; i < size; i++)
							//daca se afla deja in lista cu raspunsuri
							if (answers.get(i).answer.equals(nameAnswer[1])) {
								ok = true;
								System.out.println("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
								break;
							}
						if(!ok)
							answers.add(a);
						//urmatorul raspuns se afla la 2 pozitii fata de cel precedent in args
						poz += 2;

					}
					if(!ok) {
						poz = 0;
						for (int i = 1; i <= 5 && 6 + poz < args.length; i++) {
							if (args[5 + poz].contains("is-correct")) {
								System.out.println("{ 'status' : 'error', 'message' : 'Answer " + i + " has no answer description'}");
								ok = true;
								break;
							}
							if (!(args[6 + poz].contains("is-correct"))) {
								System.out.println("{ 'status' : 'error', 'message' : 'Answer " + i + " has no answer correct flag'}");
								ok = true;
								break;
							}
							poz += 2;
						}
						if (!ok) {
							System.out.println("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
							FileWriter myWriter;
							try {
								myWriter = new FileWriter("questions.txt", true);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							String[] type = args[4].split("'");
							Question q = new Question(text[1], type[1], answers);
							try {
								myWriter.append(q.toString() + "\n");
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							try {
								myWriter.close();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
				myReaderQuestions.close();
			} else if (args[0].equals("-get-question-id-by-text")) {
				if (args.length == 1 || args.length == 2)
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
				else {
					boolean exist = false;
					String[] usernameInput = args[1].split("'");
					String[] passwordInput = args[2].split("'");
					Scanner myReaderUsers;
					try {
						myReaderUsers = new Scanner(users);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
					while (myReaderUsers.hasNextLine()) {
						String userline = myReaderUsers.nextLine();
						String[] username = userline.split(" ");
						if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
							exist = true;
							break;
						}
					}
					myReaderUsers.close();
					if (!exist)
						System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					else {
						exist = false;
						String[] text = args[3].split("'");
						Scanner myReaderQuestions;
						try {
							myReaderQuestions = new Scanner(questions);
						} catch (FileNotFoundException e) {
							throw new RuntimeException(e);
						}
						while (myReaderQuestions.hasNextLine()) {
							String ques = myReaderQuestions.nextLine();
							String[] quesText = ques.split("'");
							if (quesText[3].equals(text[1])) {
								System.out.println("{ 'status' : 'ok', 'message' : '" + quesText[1] + "'}");
								exist = true;
								break;
							}
						}
						myReaderQuestions.close();
						if (!exist)
							System.out.println("{ 'status' : 'error', 'message' : 'Question does not exist'}");
					}
				}
			} else if (args[0].equals("-get-all-questions")) {
				if (args.length == 1 || args.length == 2)
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
				else {
					boolean exist = false;
					String[] usernameInput = args[1].split("'");
					String[] passwordInput = args[2].split("'");
					Scanner myReaderUsers;
					try {
						myReaderUsers = new Scanner(users);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
					while (myReaderUsers.hasNextLine()) {
						String userline = myReaderUsers.nextLine();
						String[] username = userline.split(" ");
						if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
							exist = true;
							break;
						}
					}
					myReaderUsers.close();
					if (!exist)
						System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					else {
						System.out.print("{ 'status' : 'ok', 'message' :'[{");
						Scanner myReaderQuestions;
						try {
							myReaderQuestions = new Scanner(questions);
						} catch (FileNotFoundException e) {
							throw new RuntimeException(e);
						}
						myReaderQuestions.hasNextLine();
						String ques = myReaderQuestions.nextLine();
						String[] quesText = ques.split("'");
						System.out.print("\"question_id\" : \"" + quesText[1] + "\", \"question_name\" : \"" + quesText[3] + "\"}");
						while (myReaderQuestions.hasNextLine()) {
							System.out.print(", {");
							ques = myReaderQuestions.nextLine();
							quesText = ques.split("'");
							System.out.print("\"question_id\" : \"" + quesText[1] + "\", \"question_name\" : \"" + quesText[3] + "\"}");
						}
						myReaderQuestions.close();
						System.out.println("]'}");
					}
				}
			} else if (args[0].equals("-cleanup-all")) {
				PrintWriter writer;
				try {
					writer = new PrintWriter("users.txt");
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				writer.print("");
				writer.close();
				PrintWriter writer2;
				try {
					writer2 = new PrintWriter("questions.txt");
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				writer2.print("");
				writer2.close();
				PrintWriter writer3;
				try {
					writer3 = new PrintWriter("quizzes.txt");
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				writer3.print("");
				writer3.close();
				PrintWriter writer4;
				try {
					writer4 = new PrintWriter("solutions.txt");
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				writer4.print("");
				writer4.close();
				Answer.setIds(0);
				Question.setIds(0);
				Quizz.setIds(0);
				System.out.println("'status' : 'ok', 'message' : 'Cleanup finished successfully'");

			} else if (args[0].equals("-create-quizz")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				exist = false;
				String[] text = args[3].split("'");
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderQuizzes.hasNextLine()) {
					String ques = myReaderQuizzes.nextLine();
					String[] quesText = ques.split("'");
					if (quesText[3].equals(text[1])) {
						System.out.println("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
						exist = true;
						break;
					}
				}
				myReaderQuizzes.close();
				if (!exist) {
					boolean semafor = true;
					List<Question> questionList = new ArrayList<>();
					for (int i = 4; i < args.length; i++) {
						List<Answer> answerList = new ArrayList<>();
						exist = false;
						String[] idQuestion = args[i].split("'");
						Scanner myReaderQuestions;
						try {
							myReaderQuestions = new Scanner(questions);
						} catch (FileNotFoundException e) {
							throw new RuntimeException(e);
						}
						while (myReaderQuestions.hasNextLine()) {
							String ques = myReaderQuestions.nextLine();
							String[] questext = ques.split("'");
							if (questext[1].equals(idQuestion[1])) {
								exist = true;
								//din 6 in 6 pozitii se repeta raspunsurile
								for (int j = 7; j < questext.length; j += 6) {
									//campurile raspunsurilor
									answerList.add(new Answer(questext[j], questext[j + 2], questext[j + 4]));
								}
								questionList.add(new Question(questext[1], questext[3], questext[5], answerList));
							}
							if (exist)
								break;
						}
						myReaderQuestions.close();
						if (!exist) {
							System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question " + idQuestion[1] + " does not exist'}");
							semafor = false;
							break;
						}
					}
					if (semafor) {
						if (args.length > 14) {
							System.out.println("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
							break;
						}
						System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
						FileWriter myWriter;
						try {
							myWriter = new FileWriter("quizzes.txt", true);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						Quizz quizz = new Quizz(text[1], usernameInput[1], questionList);
						try {
							myWriter.append(quizz.toString() + "\n");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							myWriter.close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}

					}
				}
			} else if (args[0].equals("-get-quizz-by-name")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				exist = false;
				String[] text = args[3].split("'");
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderQuizzes.hasNextLine()) {
					String ques = myReaderQuizzes.nextLine();
					String[] quesText = ques.split("'");
					if (quesText[3].equals(text[1])) {
						System.out.println("{ 'status' : 'ok', 'message' : '" + quesText[1] + "' }");
						exist = true;
						break;
					}
				}
				myReaderQuizzes.close();
				if (!exist)
					System.out.println("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
			} else if (args[0].equals("-get-all-quizzes")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				System.out.print("{ 'status' : 'ok', 'message' : '[");
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderQuizzes.hasNextLine()) {
					String ques = myReaderQuizzes.nextLine();
					String[] quesText = ques.split("'");
					System.out.print("{\"quizz_id\" : \"" + quesText[1] + "\", \"quizz_name\" : \"" + quesText[3] + "\", \"is_completed\" : \"" +
							quesText[quesText.length - 1] + "\"}");
					//isCompeted este ultimul atribut scris in fisier, indiferent de cate raspunsuri are, deci valoarea va fi mereu gasita
					//la quesText.length - 1
					if (myReaderQuizzes.hasNextLine()) System.out.print(", ");

				}
				myReaderQuizzes.close();
				System.out.println("]'}");

			} else if (args[0].equals("-get-quizz-details-by-id")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				System.out.print("{ 'status' : 'ok', 'message' : '[");
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				String ques = myReaderQuizzes.nextLine();
				String[] quesText = ques.split("'");
				int poz1 = 4; //pozitia la care se afla prima intrebare(sarind de id, nume)
				//cat timp mai exista intrebari(nu s a ajuns la sfarsit)
				while (poz1 < quesText.length && quesText[poz1].contains("Question")) {
					System.out.print("{\"question-name\":\"" + quesText[poz1 + 3] + "\", \"question_index\":\"" + quesText[poz1 + 1] +
							"\", \"question_type\":\"" + quesText[poz1 + 5] + "\", \"answers\":\"[");
					while (poz1 < quesText.length && !(quesText[poz1].contains("answer")))
						poz1++; //itereaza pana ajunge la raspuns
					while (poz1 < quesText.length - 1 && quesText[poz1].contains("answer")) { //daca mai exista raspunsuri
						System.out.print("{\"answer_name\":\"" + quesText[poz1 + 1] + "\", \"answer_id\":\"" + quesText[poz1 - 1] + "\"}");
						poz1 += 6; //sare de campurile raspunsului curent si trece la urmatorul
						if (poz1 < quesText.length && quesText[poz1].contains("answer"))
							System.out.print(", ");
						else
							System.out.print("]\"");}

							if (poz1 < quesText.length) { //daca mai exista intrebare in quizz
								poz1 -= 2; //trecea cu 2 pozitii peste din incrementarea +6
								System.out.print("}");
								if (quesText[poz1].contains("Question"))
									System.out.print(", ");
							}
							else
								System.out.print("}");

				}
				myReaderQuizzes.close();
				System.out.println("]'}");
			}
			else if (args[0].equals("-submit-quizz")) {
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				if (args.length == 3) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
					break;
				}
				for(int i = 4; i < args.length; i++) {
					String[] answer = args[i].split("'");
					if(answer[1] == null || answer[1].equals("")){
						int numberAns = i-3; //numarul raspunsului
						System.out.println("{ 'status' : 'error', 'message' : 'Answer ID for answer" + numberAns +" does not exist'}");
						break;
					}
				}
				String[] quizzId = args[3].split("'");
				exist = false;
				boolean isByCreator = false;
				boolean isCompleted = false;
				Scanner myReaderSolutions;
				try {
					myReaderSolutions = new Scanner(solutions);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderSolutions.hasNextLine()) {
					String solText = myReaderSolutions.nextLine();
					String[] sol = solText.split("'");
					if (usernameInput[1].equals(sol[1]) && quizzId[1].equals(sol[3])){
						isCompleted = true;
						break;
					}
				}
				myReaderSolutions.close();
				if(isCompleted){
					System.out.println("{ 'status' : 'error', 'message' : 'You already submitted this quizz'}");
					break;
				}
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				//fisier temporar, intrucat isCompleted pentru quizzul gasit trebuie modificat din False in True
				//se va copia tot continutul din quizzes, mai putin valoarea False, care se va modifica cu True
				//iar apoi fisierul quizzes se va sterge, iar cel temporar va fi redenumit quizzes
				File tempFile = new File("myTempFile.txt");
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(tempFile));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				int poz1 = 7; //duce catre type ul primei intrebari
				while (myReaderQuizzes.hasNextLine()) {
					String ques = myReaderQuizzes.nextLine();
					String[] quesText = ques.split("'");
					if (quesText[1].equals(quizzId[1])) {
						if(quesText[quesText.length - 3].equals(usernameInput[1])){
							isByCreator = true;
							break;
						}
						//se copiaza toate datele quizzului, mai putin valoarea isCompleted
						for(int i = 0; i < quesText.length - 1; i++) {
							try {
								writer.write(quesText[i] + "'");
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
						//se pune true
						try {
							writer.write("True'"+ "\n");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						//se marcheaza faptul ca quizzul dat exista
						exist = true;
						double points;
						double numberQuestions = 0;
						List<Double> pointsPerQuestion = new ArrayList<>(); //se retin punctele pentru fiecare intrebare
						int pozAnswersInput = 4; //catre primul raspuns dat ca input
						while (pozAnswersInput < args.length) { //cat timp exista raspunsuri date ca input
							//cat timp exista intrebari in quizz
							while (poz1 < quesText.length && !(quesText[poz1].contains("type")))
								poz1++; //duce catre type
							if (poz1 < quesText.length) {
								numberQuestions++;
								if (quesText[poz1 + 1].equals("single")) {
									String[] ids = args[pozAnswersInput].split("'");
									while (!(quesText[poz1].equals(ids[1]))
											&& !(quesText[poz1].contains("Question") && poz1 + 1 < quesText.length))
										poz1++;
									//quesText a ajuns la id ul raspunsului dat ca input
									if(quesText[poz1 - 1].contains("id")) {
										pozAnswersInput ++; //urmatoare verificare este al urmatorului raspuns dat
										if(quesText[poz1 + 4].equals("true"))
											points = 100; //a ales raspunsul corect
										else points = -100;
									} //a ajuns la sfarsitul intrebarii
									else points = 0; //nu a raspuns deloc la intrebare
								} else {
									boolean answerIsUsed = false;
									double answersCorrect= 0;
									poz1 += 3; //trece la id ul primului raspuns al intrebarii
									double answersIncorrect = 0;
									double userCorrect = 0;
									double userIncorrect = 0;
									String[] ids = args[pozAnswersInput].split("'");
									//cat timp se itereaza prin lista de raspunsuri
									while (poz1 < quesText.length && !(quesText[poz1].contains("Question"))) {
										if (quesText[poz1].equals(ids[1])) {
											answerIsUsed = true;
											pozAnswersInput++;
											if(pozAnswersInput < args.length)
												ids = args[pozAnswersInput].split("'"); //se trece la raspunsul urmator
											if(quesText[poz1 + 4].equals("true"))
												userCorrect++;
											else userIncorrect ++;
										}
										if (quesText[poz1].equals("true"))
											answersCorrect++;
										else if (quesText[poz1].equals("false"))
											answersIncorrect++;

										poz1 ++;
									}
									if(!answerIsUsed) points = 0; //nu a raspuns la intrebare
										else{
										if(answersIncorrect != 0)
											points = userCorrect/answersCorrect - userIncorrect/answersIncorrect;
										else
											points = userCorrect/answersCorrect;
										points = points * 100;
									}
								}
								pointsPerQuestion.add(points);
							}
						}
						double pondere = 100 / numberQuestions; //ponderea pe intrebare
						double totalPoints = 0;
						for (Double i : pointsPerQuestion) {
							totalPoints = totalPoints + i * pondere;
							}
						long totalPointsFinal;
						if(totalPoints < 0)
							totalPointsFinal = 0;
						else totalPointsFinal = round(totalPoints/100);
						System.out.println("{'status' : 'ok', 'message' :'" + totalPointsFinal + " points'}");
						FileWriter myWriter;
						try {
							myWriter = new FileWriter("solutions.txt", true);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							myWriter.append( "username ='" + usernameInput[1] + "', id='"+ quizzId[1] + "' nume='"
									+ quesText[3] + "', with '" +totalPointsFinal + "' points\n");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						try {
							myWriter.close();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
					else try {
						//daca nu era quizz-ul cautat, nu trebuie sa se modifice isCompleted, se copiaza linia
						writer.write(ques + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				try {
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				myReaderQuizzes.close();
				if(isByCreator){
					System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer your own quizz'}");
					tempFile.delete(); //nu se poate da submit, se sterge fisierul temporar
					break;
				}
				else if(!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
					tempFile.delete(); //nu exista quizz-ul, nu s a dat submit; se sterge fisierul temporar
					break;
				}
				else{
					quizzes.delete();
					tempFile.renameTo(quizzes);
					break;
				}
				}
			else if(args[0].equals("-delete-quizz-by-id")){
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				exist = false;
				if(args.length == 3) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
					break;
				}
				String[] text = args[3].split("'");
				File tempFile = new File("myTempFile.txt");
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(tempFile));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				Scanner myReaderQuizzes;
				try {
					myReaderQuizzes = new Scanner(quizzes);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderQuizzes.hasNextLine()) {
					String ques = myReaderQuizzes.nextLine();
					String[] quesText = ques.split("'");
					if (!(quesText[1].equals(text[1]))) {
						try {
							writer.write(ques + "\n");
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}else {
						exist = true;
						}
					}
				myReaderQuizzes.close();
				try {
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				if (!exist){
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
					tempFile.delete();
				break;
				}
				System.out.println("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
				quizzes.delete();
				tempFile.renameTo(quizzes);
				}
			else if(args[0].equals("-get-my-solutions")){
				if (args.length == 1 || args.length == 2) {
					System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					break;
				}
				String[] usernameInput = args[1].split("'");
				String[] passwordInput = args[2].split("'");
				boolean exist = false;
				Scanner myReaderUsers;
				try {
					myReaderUsers = new Scanner(users);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderUsers.hasNextLine()) {
					String userline = myReaderUsers.nextLine();
					String[] username = userline.split(" ");
					if (username[0].equals(usernameInput[1]) && username[1].equals(passwordInput[1])) {
						exist = true;
						break;
					}
				}
				myReaderUsers.close();
				if (!exist) {
					System.out.print("{ 'status' : 'error', 'message' : 'Login failed'}");
					break;
				}
				System.out.print("{ 'status' : 'ok', 'message' : '[");
				int poz = 1;
				Scanner myReaderSolutions;
				try {
					myReaderSolutions = new Scanner(solutions);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				while (myReaderSolutions.hasNextLine()) {
					String ques = myReaderSolutions.nextLine();
					String[] quesText = ques.split("'");
					if (quesText[1].equals(usernameInput[1])) {
						if(poz > 1) System.out.println(",");
						System.out.print("{\"quiz-id\" : \"" + quesText[3] + "\", \"quiz-name\" : \"" + quesText[5] + "\", \"score\" : \""
								+ quesText[7] + "\", \"index_in_list\" : \"" + poz + "\"}");
						poz++;

					}
					System.out.println("]' }");
					}
				myReaderSolutions.close();


			}
			break;
		}

	}
}

