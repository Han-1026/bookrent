import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class Bookrental {

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    Connection con;
    PreparedStatement pstmt1; // log
    PreparedStatement pstmt2; // sign
    PreparedStatement pstmt3; // list
    PreparedStatement pstmt4; // brent
    PreparedStatement pstmt5; // add
    PreparedStatement pstmt6; // BRENT
    PreparedStatement pstmt7; // rent
    PreparedStatement pstmt8; // return
    PreparedStatement pstmt9; // return
    PreparedStatement pstmt10; // selectall
    PreparedStatement pstmt11; // extend
    PreparedStatement pstmt12; // rent
    PreparedStatement pstmt13; // delete
    PreparedStatement pstmt14;// selectrent

    ResultSet rs;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Bookrental bk = new Bookrental();
        bk.dolog();

    }

    private void connectDatabase() {

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dolog() {

        connectDatabase();
        logMenue();
        int choice;
        System.out.print("선택 : ");
        choice = sc.nextInt();
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                signup();
                break;

            case 3:
                System.out.println("시스템을 종료합니다.");
                return;

            default:
                break;
        }

    }

    public void logMenue() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("||              [로그인 화면]                   ||");
        System.out.println("||                                              ||");
        System.out.println("|| 1. [   로그인  ]                             ||");
        System.out.println("|| 2. [  회원가입 ]                             ||");
        System.out.println("|| 3. [    종료   ]                             ||");
        System.out.println("|| 원하는 메뉴 번호를 선택해 주십시요           ||");
        System.out.println("=================================================");

    }

    public void login() { // 블랙리스트 기능 추가 해버리기 bookrent db에 랜트데이트 보다 +7일이고 리턴데이트가 null인조건 sql만들기
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("ID와 PW를 입력해 주세요");
        sc.nextLine();
        System.out.print("ID : ");
        String ID = sc.nextLine();
        System.out.print("PW :");
        String PW = sc.nextLine();
        String sql = "select PW from USERINFO WHERE ID=?"; // and blacklist 여부 회원인포에 넣고 blacklist 여부 rentdate 9일차 보다 크면
                                                           // 블랙처리 블랙리스트는 로그인 불가
        try {
            pstmt2 = con.prepareStatement(sql);

            pstmt2.setString(1, ID);
            rs = pstmt2.executeQuery();
            while (true) {

                if (rs.next()) {
                    if (rs.getString(1).contentEquals(PW)) {
                        System.out.println("로그인 되었습니다.");
                        rentMenu();
                        continue;

                    } else {
                        System.out.println("비밀번호가 틀립니다.");
                        login();
                        continue;
                    }
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void signup() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[회원가입]");
        sc.nextLine();
        System.out.print("ID : ");
        String ID = sc.nextLine();
        System.out.print("PW :");
        String PW = sc.nextLine();
        System.out.print("NAME :");
        String NAME = sc.nextLine();

        String sql = "insert into USERINFO values(?,?,?)";
        // while (true) {
        try {
            pstmt1 = con.prepareStatement(sql);
            pstmt1.setString(1, ID);
            pstmt1.setString(2, PW);
            pstmt1.setString(3, NAME);
            int updateCount = pstmt1.executeUpdate();

            if (updateCount >= 1) {
                System.out.println("회원가입이 완료 되었습니다.");
                System.out.println("=================================================");
                dolog();

            }
        } catch (SQLIntegrityConstraintViolationException e) { // pk값 예외처리

            System.out.println("중복된 아이디 입니다. 다시 입력해 주세요");
            dolog();

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // }
    /////////

    public void showMenue() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("1. 대여");
        System.out.println("2. 반납");
        System.out.println("3. 연장");
        System.out.println("4. 도서 목록 조회");
        System.out.println("5. 대여 현황 조회");
        System.out.println("6. 도서 등록");
        System.out.println("7. 도서 삭제");
        System.out.println("8. 종료");
        System.out.println("원하는 메뉴 번호를 선택해 주십시요");
        System.out.println("=================================================");
    }

    public void rentMenu() {

        connectDatabase();
        showMenue();
        int choice;
        System.out.print("선택 : ");
        choice = sc.nextInt();
        while (true) {
            switch (choice) {
                case 1:
                    rent();
                    break;
                case 2:
                    returnBook();
                    break;

                case 3:
                    extend();
                    break;

                case 4:
                    selectAll();
                    break;

                case 5:
                    selectrent();
                    break;

                case 6:
                    addbook();
                    break;

                case 7:
                    deletebook();
                    break;

                case 8:
                    System.out.println("시스템을 종료합니다.");
                    return;

                default:
                    break;
            }
        }

    }

    public void rent() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[대여]");
        sc.nextLine();
        System.out.print("1. 책번호 : ");
        String bookno = sc.nextLine();
        System.out.print("2. ID : ");
        String rentID = sc.nextLine();

        String sql = "insert into BOOKRENT (bookno,rentid,rentdate) values(?,?,sysdate)"; // insert 대여정보 // where
                                                                                          // b.quantit >= 1 대여
        String sql2 = "Update bookinfo set quantity = quantity-1 where bookno=?"; // update 수량 감소
        String sql3 = "select quantity from bookinfo where bookno=? and quantity >= 1"; // 수량에 따른 대여제한

        try {
            pstmt12 = con.prepareStatement(sql3);
            pstmt12.setString(1, bookno);
            rs = pstmt12.executeQuery();

            pstmt6 = con.prepareStatement(sql2);
            pstmt6.setString(1, bookno);

            pstmt4 = con.prepareStatement(sql);
            pstmt4.setString(1, bookno);
            pstmt4.setString(2, rentID);

            if (rs.next()) {
                int updateCount = pstmt4.executeUpdate();
                if (updateCount >= 1) {
                    int updateCount2 = pstmt6.executeUpdate();
                    System.out.println("대여 되었습니다.");
                    rentMenu();

                }
            } else {
                System.out.println("해당 도서는 모두 대여중 입니다.");
                rentMenu();

            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            rentMenu();

        }
    }

    public void returnBook() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[반납]");
        sc.nextLine();
        System.out.print("1. 책번호 : ");
        String bookno = sc.nextLine();
        System.out.print("2. ID : ");
        String rentID = sc.nextLine();

        String sql = "Update bookinfo set quantity = quantity+1 where bookno= ?"; // 반납 북넘버가 같을 경우 수량 증가
        String sql2 = "Update bookrent set returndate =sysdate where bookno=? and rentID =?"; // 반납

        while (true) {
            try {
                pstmt8 = con.prepareStatement(sql);
                pstmt8.setString(1, bookno);
                pstmt9 = con.prepareStatement(sql2);
                pstmt9.setString(1, bookno);
                pstmt9.setString(2, rentID);
                int updateCount4 = pstmt9.executeUpdate(); // 리턴날짜
                if (updateCount4 >= 1) {
                    int updateCount3 = pstmt8.executeUpdate(); // 수량
                    System.out.println("반납 완료");
                    rentMenu();
                    continue;

                } else {
                    System.out.println("다시 시도해주세요");

                }

            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        }
    }

    public void extend() {
        System.out.println(""); // 업데이트 하이어데이트 기간 늘려
        System.out.println("=================================================");
        System.out.println("[대여 기간 연장]");
        sc.nextLine();
        System.out.print("1. 책번호 : ");
        String bookno = sc.nextLine();
        System.out.print("2. ID : ");
        String rentID = sc.nextLine();

        String sql = "Update bookrent set rentdate=rentdate+7 where bookno=? and rentID =?";
        while (true)
            try {
                pstmt11 = con.prepareStatement(sql);
                pstmt11.setString(1, bookno);
                pstmt11.setString(2, rentID);
                int updateCount = pstmt11.executeUpdate(); // 대여기간 연장
                System.out.println("대여기간 7일 연장 되었습니다");
                rentMenu();

            } catch (Exception e) {
                e.printStackTrace();

            }

    }

    public void selectAll() { // 완료
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[도서 목록]");
        String sql = "select*from bookinfo order by bookno";
        while (true) {
            try {

                pstmt14 = con.prepareStatement(sql);
                rs = pstmt14.executeQuery();

                while (rs.next()) {
                    String bookno = rs.getString("bookno");
                    String Author = rs.getString("Author");
                    String bookname = rs.getString("bookname");
                    String quantity = rs.getString("quantity");
                    System.out.println(
                            "[책번호] : " + bookno + " [저자] : " + Author + " [책이름] :" + bookname + " [수량] :" + quantity);

                }
                rentMenu();
                continue;
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }

    public void selectrent() { // 완료
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[대여 현황]");
        // sc.nextLine();
        // System.out.print("1.ID : ");
        // String rentid = sc.nextLine();

        String sql = "select*from bookrent";
        while (true) {
            try {
                // pstmt14.setString(1, rentid);
                pstmt14 = con.prepareStatement(sql);
                rs = pstmt14.executeQuery();

                while (rs.next()) {
                    String bookno = rs.getString("bookno");
                    String rentid = rs.getString("rentid");
                    String rentdate = rs.getString("rentdate");
                    String returndate = rs.getString("returndate");
                    System.out.println("[책번호] : " + bookno + " [대여ID] : " + rentid + " [대여날짜] :" + rentdate
                            + " [반납날짜] :" + returndate);

                }
                rentMenu();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void addbook() {

        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[도서등록]");
        sc.nextLine();
        System.out.print("책번호 : ");
        String bookno = sc.nextLine();
        System.out.print("지은이 :");
        String author = sc.nextLine();
        System.out.print("책이름 :");
        String bookname = sc.nextLine();
        System.out.print("수량 :");
        String quantity = sc.nextLine();

        String sql = "insert into BookINFO values(?,?,?,?)";

        try {
            pstmt5 = con.prepareStatement(sql);
            pstmt5.setString(1, bookno);
            pstmt5.setString(2, author);
            pstmt5.setString(3, bookname);
            pstmt5.setString(4, quantity);

            int updateCount = pstmt5.executeUpdate();
            System.out.println("도서등록이 완료 되었습니다.");
            rentMenu();

        } catch (SQLIntegrityConstraintViolationException e) { // pk값 예외처리

            System.out.println("중복된 책번호 입니다. 다시 등록해 주세요");
            rentMenu();

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletebook() {

        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[도서삭제]");
        sc.nextLine();
        System.out.print("책번호 : ");
        String bookno = sc.nextLine();

        String sql = "delete from bookinfo where bookno=?";

        try {
            pstmt13 = con.prepareStatement(sql);
            pstmt13.setString(1, bookno);

            int updateCount = pstmt13.executeUpdate();

            System.out.println("해당 도서가 삭제 되었습니다.");
            rentMenu();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
