## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).




public void rent() {
        System.out.println("");
        System.out.println("=================================================");
        System.out.println("[대여]");
        sc.nextLine();
        System.out.print("1. 책번호 : ");
        String bookno = sc.nextLine();
        System.out.print("2. ID : ");
        String rentID = sc.nextLine();

        String sql = "insert into BOOKRENT (bookno,rentid,rentdate) values(?,?,sysdate)"; // 중복ID 대여 추가해야할 기능 권수 쿼리 합쳐서
                                                                                          // where b.quantit >= 1 대여
        // String sql2 = "update BOOKinfo set
        // rentID=?,RENTDATE=SYSDATE,RETURNDATE=SYSDATE+7 WHERE Bookno=?";
        String sql2 = "Update bookinfo set quantity = quantity-1 where bookno= ?";
        // String sql3 = "select bookno from BOOKRENT where bookno=?";
        // while (true) {

        try {
            // pstmt7 = con.prepareStatement(sql3);
            // pstmt7.setString(1, bookno);

            pstmt6 = con.prepareStatement(sql2);
            pstmt6.setString(1, bookno);

            pstmt4 = con.prepareStatement(sql);
            pstmt4.setString(1, bookno);
            pstmt4.setString(2, rentID);
            int updateCount = pstmt4.executeUpdate(); // 북랜트에 대여 정보 인서트
            // rs = pstmt7.executeQuery();
            // while (rs.next()) {
            // 북인포에서 북넘버 조회
            if (updateCount >= 1) {
                int updateCount2 = pstmt6.executeUpdate(); // 북수량
                System.out.println("대여 되었습니다.");

            } else {
                System.out.println("대여 중인 책입니다.");

            }
            // }

            // else if (updateCount >= 1 {
            // System.out.println("대여 되었습니다."); // 대여 프린트 출력 나오도록 수정 그후 무한반복수정
            // rentMenu();
            // }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();

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

        String sql = "insert into BOOKRENT (bookno,rentid,rentdate) values(?,?,sysdate)"; // 중복ID 대여 추가해야할 기능 권수 쿼리 합쳐서
                                                                                          // where b.quantit >= 1 대여
        String sql2 = "Update bookinfo set quantity = quantity-1 where bookno=?";
        String sql3 = "select quantity from bookinfo where quantity > 1";
        while (true) {
            try {
                pstmt12 = con.prepareStatement(sql3);
                rs = pstmt12.executeQuery();

                pstmt6 = con.prepareStatement(sql2); // 수량 감소
                pstmt6.setString(1, bookno);

                pstmt4 = con.prepareStatement(sql);
                pstmt4.setString(1, bookno);
                pstmt4.setString(2, rentID);
                int updateCount = pstmt4.executeUpdate(); // 북랜트에 대여 정보 인서트
                if (rs.next()) {
                    if (updateCount >= 1) {
                        int updateCount2 = pstmt6.executeUpdate(); // 북수량
                        System.out.println("대여 되었습니다.");
                        rentMenu();
                        continue;
                    } else {
                        System.out.println("대여 중인 책입니다.");

                    }

                } else {
                    System.out.println("대여 중인 책입니다.");

                }

                rs.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }