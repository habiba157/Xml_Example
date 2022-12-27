import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XmlFileManger {
    static Document booksDocument;

    public XmlFileManger() {
        booksDocument = creatXmlDocument();
    }

    private Document creatXmlDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        File f = new File("models/books.xml");
        try {
            if (!f.exists()) {
                dBuilder = factory.newDocumentBuilder();
                Document document = dBuilder.newDocument();
                // catalouge
                Element rooElement = document.createElement("Catalogue");
                document.appendChild(rooElement);
                // book
                Element bookElement = document.createElement("Book");
                rooElement.appendChild(bookElement);
                Attr idAttribute = document.createAttribute("ID");
                idAttribute.setValue("BK101");
                bookElement.setAttributeNode(idAttribute);
                // author
                Element autherElement = document.createElement("Author");
                autherElement.appendChild(document.createTextNode("Gambardella Matthew"));
                bookElement.appendChild(autherElement);
                // title
                Element titleElement = document.createElement("Title");
                titleElement.appendChild(document.createTextNode("XML Developers Guide"));
                bookElement.appendChild(titleElement);
                // gerne
                Element gerneElement = document.createElement("Genre");
                gerneElement.appendChild(document.createTextNode("Computer"));
                bookElement.appendChild(gerneElement);
                // price
                Element priceElement = document.createElement("Price");
                priceElement.appendChild(document.createTextNode("44.95"));
                bookElement.appendChild(priceElement);
                // Publish_Date
                Element publishDataElement = document.createElement("Publish_Date");
                publishDataElement.appendChild(document.createTextNode("2000-11-01"));
                bookElement.appendChild(publishDataElement);
                // description
                Element descriptionElement = document.createElement("Description");
                descriptionElement.appendChild(
                        document.createTextNode("A guide for developers on how to create applications using XML."));
                bookElement.appendChild(descriptionElement);
                document.getDocumentElement().normalize();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File("models/books.xml"));
                transformer.transform(source, result);
                return document;
            } else {
                dBuilder = factory.newDocumentBuilder();
                Document document = dBuilder.parse(f);
                document.getDocumentElement().normalize();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File("models/books.xml"));
                transformer.transform(source, result);
                return document;
            }
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ArrayList<Book> retriveBooks() {
        ArrayList<Book> books = new ArrayList<Book>();
        NodeList nList = booksDocument.getElementsByTagName("Book");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node bookNode = nList.item(temp);
            Book book = new Book();
            if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bookElement = (Element) bookNode;
                book.setId(bookElement.getAttribute("ID"));
                book.setAuthor(bookElement.getElementsByTagName("Author").item(0).getTextContent());
                book.setDescription(bookElement.getElementsByTagName("Description").item(0).getTextContent());
                book.setGenre(bookElement.getElementsByTagName("Genre").item(0).getTextContent());
                book.setTitle(bookElement.getElementsByTagName("Title").item(0).getTextContent());
                try {
                    // TODO::can have a problem
                    book.setPublish_Date(new SimpleDateFormat("yyyy-MM-dd")
                            .parse(bookElement.getElementsByTagName("Publish_Date").item(0).getTextContent()
                                    .replaceAll("(?m)^[ \t]*\r?\n", "").replaceAll("\\s", "")));
                } catch (DOMException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                book.setPrice(Double.parseDouble(bookElement.getElementsByTagName("Price").item(0).getTextContent()));
                books.add(book);

            }
        }
        return books;
    }

    public ArrayList<String> retriveIDs() {
        ArrayList<String> idList = new ArrayList<String>();
        NodeList nList = booksDocument.getElementsByTagName("Book");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node bookNode = nList.item(temp);
            if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bookElement = (Element) bookNode;
                idList.add(bookElement.getAttribute("ID"));
            }
        }
        return idList;
    }

    public void add(Book book) {
        try {
            Document document = booksDocument;
            Element root = document.getDocumentElement();
            // book
            Element bookElement = document.createElement("Book");
            Attr idAttribute = document.createAttribute("ID");
            idAttribute.setValue(book.getId());
            bookElement.setAttributeNode(idAttribute);
            // author
            Element autherElement = document.createElement("Author");
            autherElement.appendChild(document.createTextNode(book.getAuthor()));
            bookElement.appendChild(autherElement);
            // title
            Element titleElement = document.createElement("Title");
            titleElement.appendChild(document.createTextNode(book.getTitle()));
            bookElement.appendChild(titleElement);
            // genre
            Element gerneElement = document.createElement("Genre");
            gerneElement.appendChild(document.createTextNode(book.getGenre()));
            bookElement.appendChild(gerneElement);
            // price
            Element priceElement = document.createElement("Price");
            priceElement.appendChild(document.createTextNode(Double.toString(book.getPrice())));
            bookElement.appendChild(priceElement);
            // Publish_Date
            Element publishDataElement = document.createElement("Publish_Date");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(book.getPublish_Date());
            publishDataElement.appendChild(document.createTextNode(date));
            bookElement.appendChild(publishDataElement);
            // description
            Element descriptionElement = document.createElement("Description");
            descriptionElement.appendChild(
                    document.createTextNode(book.getDescription()));
            bookElement.appendChild(descriptionElement);
            root.appendChild(bookElement);
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("models/books.xml");
            transformer.transform(source, result);
            System.out.println("Book added successfully");
            System.out.println("===================================");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> searchByAuthor(String authorName) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(authorName.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public ArrayList<Book> searchByTitle(String title) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase()))
                results.add(book);
        }
        return results;
    }

    public ArrayList<Book> searchByGerne(String gerne) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getGenre().toLowerCase().contains(gerne.toLowerCase()))
                results.add(book);
        }
        return results;
    }

    public ArrayList<Book> searchByPrice(double Price) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getPrice() == Price)
                results.add(book);
        }
        return results;
    }

    public ArrayList<Book> searchByDate(Date date) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getPublish_Date().compareTo(date) == 0)
                results.add(book);
        }
        return results;
    }

    public ArrayList<Book> searchByID(String id) {
        ArrayList<Book> books = retriveBooks();
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getId().toLowerCase().contains(id.toLowerCase()))
                results.add(book);
        }
        return results;
    }

    public void updateBook(Book updatedBook) {
        NodeList nList = booksDocument.getElementsByTagName("Book");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node bookNode = nList.item(temp);
            if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bookElement = (Element) bookNode;
                if (bookElement.getAttribute("ID").toLowerCase().equals(updatedBook.getId().toLowerCase())) {
                    bookElement.getElementsByTagName("Author").item(0).setTextContent(updatedBook.getAuthor());
                    bookElement.getElementsByTagName("Description").item(0)
                            .setTextContent(updatedBook.getDescription());
                    bookElement.getElementsByTagName("Genre").item(0).setTextContent(updatedBook.getGenre());
                    bookElement.getElementsByTagName("Title").item(0).setTextContent(updatedBook.getTitle());
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(updatedBook.getPublish_Date());
                    bookElement.getElementsByTagName("Publish_Date").item(0).setTextContent(date);
                    bookElement.getElementsByTagName("Price").item(0)
                            .setTextContent(Double.toString(updatedBook.getPrice()));
                    System.out.println("Book Updated successfully");
                    try {
                        DOMSource source = new DOMSource(booksDocument);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        StreamResult result = new StreamResult("models/books.xml");
                        transformer.transform(source, result);
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }

    }

    public void remove(String id) {
        NodeList nList = booksDocument.getElementsByTagName("Book");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node bookNode = nList.item(temp);
            if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bookElement = (Element) bookNode;

                if (id.toLowerCase().equals(bookElement.getAttribute("ID").toLowerCase())) {
                    bookNode.getParentNode().removeChild(bookElement);
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer;
                    try {
                        transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(booksDocument);
                        StreamResult result = new StreamResult(new File("models/books.xml"));
                        transformer.transform(source, result);
                        System.out.println("book  " + id + " removed successfully");
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        System.out.println("book " + id + " not found");
    }

    public void sortById() {

      ArrayList ids = retriveIDs();
        List<String> sortedIDs = (List<String>) ids.stream().sorted().collect(Collectors.toList());

        ArrayList<Book> results = new ArrayList<>();
        ArrayList<Book> books = retriveBooks();

        for (String id : sortedIDs ){
            for (int i = 0 ;i<sortedIDs.size();i++){
                if(books.get(i).getId().equals(id)){
                    //System.out.println(books.get(i).title);
                    results.add(books.get(i));


                }

            }


        }
        for (Book book : results) {
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

}
