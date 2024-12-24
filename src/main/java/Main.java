import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/bookstore?user=postgres&password=root";
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver loaded");

			Connection con = DriverManager.getConnection(url);
			System.out.println("Connection created");

			boolean flag = true;
			do {
				System.out.println(
						"\nEnter the number of operation you want to perform: \n1. Add New Book \n2. Get Book Details \n3. Retrieve all Books \n4. Update Book Details \n5. Delete Existing Book \n6. Close");
				int input = sc.nextInt();
				sc.nextLine();

				switch (input) {
					case 1: {
						addBook(con);
						break;
					}
					case 2: {
						getBookById(con);
						break;
					}
					case 3: {
						getAllBooks(con);
						break;
					}
					case 4: {
						updateBookById(con);
						break;
					}
					case 5: {
						deleteBookById(con);
						break;
					}
					case 6:
						flag = false;
						break;
					default: {
						System.out.println("Wrong input, please enter a valid choice next time!");
						break;
					}
				}

			} while (flag);

			con.close();
			System.out.println("Connection closed");
		} catch (ClassNotFoundException | SQLException e) {
		}
	}

	public static void deleteBookById(Connection con) {
		try {
			System.out.println("Enter the id to delete the book: ");
			int id = sc.nextInt();
			int rs;
			PreparedStatement stm = con.prepareStatement("delete from book where id = ?");
			stm.setInt(1, id);
			rs = stm.executeUpdate();

			if (rs >= 1) {
				System.out.println("Book deleted successfully.");
			} else {
				System.out.println("Book not found!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateBookById(Connection con) {
		try {
			System.out.println(
					"Enter a number of attribute you want to update\n1. Bookname \n2. Authorname \n3. Publication \n4. Price \n");
			int input = sc.nextInt();
			System.out.println("Enter the book id to update: ");
			int id = sc.nextInt();
			sc.nextLine();
			int rs = 0;
			PreparedStatement stm;
			switch (input) {
				case 1: {
					stm = con.prepareStatement("update book set name = ? where id = ?");
					System.out.println("Enter the book name: ");
					String name = sc.nextLine();
					stm.setString(1, name);
					stm.setInt(2, id);
					rs = stm.executeUpdate();
					break;
				}
				case 2: {
					stm = con.prepareStatement("update book set author = ? where id = ?");
					System.out.println("Enter the book author: ");
					String author = sc.nextLine();
					stm.setString(1, author);
					stm.setInt(2, id);
					rs = stm.executeUpdate();
					break;
				}
				case 3: {
					stm = con.prepareStatement("update book set publication = ? where id = ?");
					System.out.println("Enter the book publication: ");
					String publication = sc.nextLine();
					stm.setString(1, publication);
					stm.setInt(2, id);
					rs = stm.executeUpdate();
					break;
				}
				case 4: {
					stm = con.prepareStatement("update book set price = ? where id = ?");
					System.out.println("Enter the book price: ");
					int price = sc.nextInt();
					stm.setInt(1, price);
					stm.setInt(2, id);
					rs = stm.executeUpdate();
					break;
				}
			}

			// check if book are updated in DB
			if (rs >= 1) {
				System.out.println("Book information updated!");
			} else {
				System.out.println("Book is not present in the database!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getAllBooks(Connection con) {
		try {
			PreparedStatement stm = con.prepareStatement("select * from book");
			ResultSet rs = stm.executeQuery();

			// check if books are present in DB
			if (!rs.isBeforeFirst()) {
				System.out.println("Books not found!");
				return;
			}

			while (rs.next()) {
				System.out.println("Book Id :    " + rs.getInt(1));
				System.out.println("Book Name:   " + rs.getString(2));
				System.out.println("Author:      " + rs.getString(3));
				System.out.println("Publication: " + rs.getString(4));
				System.out.println("Price:       " + rs.getInt(5));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getBookById(Connection con) {
		try {
			System.out.println("Enter the book id to search: ");
			int id = sc.nextInt();
			PreparedStatement stm = con.prepareStatement("select * from book where id = ?");
			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			// check if book is present in DB
			if (!rs.isBeforeFirst()) {
				System.out.println("Book not found!");
				return;
			}

			while (rs.next()) {
				System.out.println("Book Id :    " + rs.getInt(1));
				System.out.println("Book Name:   " + rs.getString(2));
				System.out.println("Author:      " + rs.getString(3));
				System.out.println("Publication: " + rs.getString(4));
				System.out.println("Price:       " + rs.getInt(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addBook(Connection con) {
		try {
			System.out.println("Enter the book id: ");
			int id = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter the book name: ");
			String name = sc.nextLine();
			System.out.println("Enter the book author name: ");
			String author = sc.nextLine();
			System.out.println("Enter the book publication: ");
			String publication = sc.nextLine();
			System.out.println("Enter the book price: ");
			int price = sc.nextInt();

			PreparedStatement stm = con.prepareStatement("insert into book values(?, ?, ?, ?, ?)");

			stm.setInt(1, id);
			stm.setInt(5, price);
			stm.setString(2, name);
			stm.setString(3, author);
			stm.setString(4, publication);

			stm.execute();
			System.out.println("Book added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

// Statement stm = con.createStatement();
// stm.execute("insert into book values(1, 'Rich dad poor dad', 'Robert
// Kiyosaki', 'Plata Publishing', 350), (2, 'Energize your mind', 'Gaur gopal
// das', 'Penguin Ananda', 249), (3, 'Master Your Emotions', 'Thibaut Meurisse',
// 'Wisdom Tree', 291), (4, 'Bhagavad Gita Marathi', 'Vyasa', 'The Bhaktivedanta
// Book Trust', 350), (5, 'The Secret', 'Rhonda Byrne', 'SIMON & SCHUSTER UK',
// 278), (6, 'The Subtle Art of Not Giving a F*ck', 'Mark Manson', 'Harper
// Collins', 149)");
// System.out.println("Books inserted");

// ResultSet rs = stm.executeQuery("select * from book");
//
// while(rs.next()) {
// System.out.print("Book Id : " + rs.getInt(1));
// System.out.print(" Book Name: " + rs.getString(2));
// System.out.print(" Author: " + rs.getString(3));
// System.out.print(" Publication: " + rs.getString(4));
// System.out.println(" Price: " + rs.getInt(5));
// }

// PreparedStatement stm = con.prepareStatement("update book set name = ?,
// author = ?, publication = ?, price = ? where id = ?");
// stm.setString(1, name);
// stm.setString(2, author);
// stm.setString(3, publication);
// stm.setInt(4, price);
// stm.setInt(5, id);
// rs = stm.execute();

// System.out.println("Do you want to continue operations ?\n1. Yes \n2. No");
// int ip = sc.nextInt();
// sc.nextLine();
// if(ip == 2) break;
// else if(ip == 1) continue;
// else System.out.print("Please provide correct input");
