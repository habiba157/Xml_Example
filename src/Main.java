import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static XmlFileManger xmlBooksFile;

    public Main() {
        xmlBooksFile = new XmlFileManger();
    }

    private boolean validateID(String id) {
        ArrayList<String> idList = xmlBooksFile.retriveIDs();
        if (id != "" || !id.isEmpty()) {
            if (!idList.contains(id))
                return true;
            else
                System.out.println("Id Already Exists ,try again");
            return false;
        } else {
            System.out.println("Id Can't be Empty try again");
            return false;
        }
    }

    private boolean validateAuther(String auther) {
        if (auther != "" || !auther.isEmpty()) {
            if (auther.matches("[a-zA-Z]+"))
                return true;
            else
                System.out.println("Auther Name should contains only charecters a-z / A-Z ,try again");
            return false;
        } else {
            System.out.println("Auther Can't be Empty ,try again");
            return false;
        }
    }

    private boolean validateTitle(String title) {
        if (title != "" || !title.isEmpty()) {
            return true;
        } else {
            System.out.println("Auther Can't be Empty ,try again");
            return false;
        }
    }

    private String getValidGerne() {
        int gerneChoice = 10;
        String gerne = "";
        while (gerneChoice != 0) {
            System.out.println("Choose Book Genre:");
            System.out.println(" 1- Science ");
            System.out.println(" 2- Fiction ");
            System.out.println(" 3- Drama ");
            gerneChoice = sc.nextInt();
            switch (gerneChoice) {
                case 1:
                    gerne = "Science";
                    gerneChoice = 0;
                    break;
                case 2:
                    gerne = "Fiction";
                    gerneChoice = 0;
                    break;
                case 3:
                    gerne = "Drama";
                    gerneChoice = 0;
                    break;
                default:
                    System.out.println("Invalid choice , try again");
                    gerneChoice = 10;
                    break;
            }
        }
        return gerne;
    }

    private Double getValidPrice() {
        double price;
        if (!sc.hasNextDouble()) {
            System.out.println("Invalid Input. Price is Required and Must be a number.");
            sc.nextLine().strip();
            sc.nextLine();
            return null;
        }
        price = sc.nextDouble();
        return price;
    }

    private Date getValidDate() {
        int day, month, year;
        System.out.print("Day: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid Input. Day is Required and Must be a number.");
            sc.nextLine().strip();
            sc.nextLine();
            return null;
        }
        day = sc.nextInt();
        System.out.print("Month: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid Input. Month is Required and Must be a number.");
            sc.nextLine().strip();
            sc.nextLine();
            return null;
        }
        month = sc.nextInt();
        System.out.print("Year: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid Input. Year is Required and Must be a number.");
            sc.nextLine().strip();
            sc.nextLine();
            return null;
        }
        year = sc.nextInt();
        if (day < 0 || day > 31 || month < 0 || month > 12 || year < 0 || year > Year.now().getValue()) {
            System.out.println("Invalid Date.valid date should be in range : ");
            System.out.println("day 1-31 , month 1-12 , year 1-" + Year.now().getValue());
            return null;
        }
        String date = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Book getBookData() {

        Book book = new Book();
        System.out.println("Enter Book ID:");
        String id = sc.nextLine().strip();
        if (validateID(id))
            book.setId(id);
        else
            return null;
        System.out.println("Enter Book Author:");
        String auther = sc.nextLine().strip();
        if (validateAuther(auther))
            book.setAuthor(auther);
        else
            return null;

        System.out.println("Enter Book Title:");
        String title = sc.nextLine().strip();
        if (validateTitle(title))
            book.setTitle(title);
        else
            return null;

        book.setGenre(getValidGerne());
        System.out.println("Enter Book Price:");
        Double price = getValidPrice();
        if (price != null)
            book.setPrice(price);
        else
            return null;

        System.out.println("Enter Book Publised Date :");
        Date date = getValidDate();
        if (date != null)
            book.setPublish_Date(date);
        else
            return null;
        System.out.println("Enter Book Description:");
        book.setDescription(sc.nextLine().strip());
        book.setDescription(sc.nextLine().strip());
        return book;

    }

    public void displaySearchResult(ArrayList<Book> books) {
        if (books.size() == 0) {
            System.out.println("No Books matches");
            return;
        }
        System.out.println(books.size() + " result found");
        System.out.println("-------------------------------");
        for (Book book : books) {
            System.out.println("Id : " + book.getId());
            System.out.println("Author : " + book.getAuthor());
            System.out.println("Title : " + book.getTitle());
            System.out.println("Genre : " + book.getGenre());
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(book.getPublish_Date());
            System.out.println("Publish Date : " + date);
            System.out.println("Price : " + book.getPrice());
            System.out.println("Description : " + book.getDescription());
            System.out.println("=========================================");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        int choice = 5;
        while (choice != 0) {
            System.out.println("1- add books");
            System.out.println("2- search for a book");
            System.out.println("3- delete book");
            System.out.println("4- sort books");
            System.out.println("5- Update book");
            System.out.println("0- exit");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter books number:");
                    int booksNum = sc.nextInt();
                    sc.nextLine();
                    for (int i = 0; i < booksNum; i++) {
                        Book book = main.getBookData();
                        if (book != null)
                            Main.xmlBooksFile.add(book);
                    }
                    break;
                case 2:
                    int searchChoice = 7;
                    while (searchChoice != 0) {
                        System.out.println("1- search by author");
                        System.out.println("2- search by title");
                        System.out.println("3- search by ID");
                        System.out.println("4- search by gerne");
                        System.out.println("5- search by price");
                        System.out.println("6- search by publised date");
                        System.out.println("0- back");
                        searchChoice = sc.nextInt();
                        sc.nextLine();
                        String searchKey;
                        switch (searchChoice) {
                            case 1:
                                System.out.println("Enter author name:");
                                searchKey = sc.nextLine().strip();
                                main.displaySearchResult(Main.xmlBooksFile.searchByAuthor(searchKey));
                                break;
                            case 2:
                                System.out.println("Enter book title:");
                                searchKey = sc.nextLine().strip();
                                main.displaySearchResult(Main.xmlBooksFile.searchByTitle(searchKey));
                                break;
                            case 3:
                                System.out.println("Enter book ID:");
                                searchKey = sc.nextLine().strip();
                                main.displaySearchResult(Main.xmlBooksFile.searchByID(searchKey));
                                break;
                            case 4:
                                System.out.println("Enter book gerne:");
                                searchKey = sc.nextLine().strip();
                                main.displaySearchResult(Main.xmlBooksFile.searchByGerne(searchKey));
                                break;
                            case 5:
                                System.out.println("Enter book price:");
                                double Key = sc.nextDouble();
                                main.displaySearchResult(Main.xmlBooksFile.searchByPrice(Key));
                                break;
                            case 6:
                                System.out.println("Enter book date:");
                                int day, month, year;
                                System.out.print("Day: ");
                                day = sc.nextInt();
                                System.out.print("Month: ");
                                month = sc.nextInt();
                                System.out.print("Year: ");
                                year = sc.nextInt();
                                String date = Integer.toString(day) + "/" + Integer.toString(month) + "/"
                                        + Integer.toString(year);
                                try {
                                    main.displaySearchResult(Main.xmlBooksFile
                                            .searchByDate(new SimpleDateFormat("dd/MM/yyyy").parse(date)));
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                break;

                            default:
                                break;
                        }

                    }
                    break;
                case 3:
                    System.out.println("Enter book ID:");
                    sc.nextLine();
                    String deleted = sc.nextLine().strip();
                    Main.xmlBooksFile.remove(deleted);
                    break;
                case 4:
                    int sortChoice = 10;
                    System.out.println("1- sort by ID");
                        //System.out.println("2- sort by auther");
                        //System.out.println("3- sort by title");
                        //System.out.println("4- sort by genre");
                        //System.out.println("4- sort by published date");
                        System.out.println("2- back");
                        sortChoice = sc.nextInt();
                        switch (sortChoice) {
                            //Main.xmlBooksFile.compare(Main.xmlBooksFile.retriveBooks());
                            case 1:
                                Main.xmlBooksFile.sortById();
                            case 2:
                                break;


                    }
                    break;
                case 5:
                    System.out.println("Enter Book ID:");
                    sc.nextLine();
                    String updatedBook = sc.nextLine().strip();
                    if (Main.xmlBooksFile.searchByID(updatedBook).size() < 0) {
                        System.out.println("No books was found");
                        break;
                    }
                    main.displaySearchResult(Main.xmlBooksFile.searchByID(updatedBook));
                    Book book = Main.xmlBooksFile.searchByID(updatedBook).get(0);
                    int updateChoice = 10;
                    while (updateChoice != 0) {
                        System.out.println("1- update auther");
                        System.out.println("2- update title");
                        System.out.println("3- update genre");
                        System.out.println("4- update price");
                        System.out.println("5- update published date");
                        System.out.println("6- update description");
                        System.out.println("0- back");
                        updateChoice = sc.nextInt();
                        switch (updateChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.print("Auther name : ");
                                String auther = sc.nextLine().strip();
                                if (main.validateAuther(auther))
                                    book.setAuthor(auther);
                                break;
                            case 2:
                                sc.nextLine();
                                System.out.print("Title : ");
                                String title = sc.nextLine().strip();
                                if (main.validateTitle(title))
                                    book.setTitle(title);
                                break;
                            case 3:
                                String gerne = main.getValidGerne();
                                book.setGenre(gerne);
                                break;
                            case 4:
                                Double price = main.getValidPrice();
                                if (price != null)
                                    book.setPrice(price);
                                break;
                            case 5:
                                Date date = main.getValidDate();
                                if (date != null)
                                    book.setPublish_Date(date);
                                break;
                            case 6:
                                System.out.print("Description : ");
                                String description = sc.nextLine().strip();
                                book.setDescription(description);
                                break;
                            default:
                                break;
                        }
                    }
                    Main.xmlBooksFile.updateBook(book);
                    break;

                default:
                    break;
            }
        }

    }

}
