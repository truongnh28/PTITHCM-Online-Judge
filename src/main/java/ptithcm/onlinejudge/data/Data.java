package ptithcm.onlinejudge.data;

import ptithcm.onlinejudge.dto.*;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<RoleDTO> roleList = new ArrayList<>();

    static {
        roleList.add(new RoleDTO(1, "ROLE_Admin"));
        roleList.add(new RoleDTO(2, "ROLE_Student"));
    }

    public static List<StudentDTO> studentList = new ArrayList<>();

    static {
        studentList.add(new StudentDTO("N19DCCN190", "26102001", "Thanh", "Nguyễn Nhật", true, roleList.get(1)));
        studentList.add(new StudentDTO("N19DCCN221", "28022001", "Trưởng", "Nguyễn Hữu", true, roleList.get(1)));
    }

    public static List<TeacherDTO> teacherList = new ArrayList<>();
    static {
        teacherList.add(new TeacherDTO("admin", "ptithcm", "Admin", "Admin", true, roleList.get(1)));
    }

    public static List<ProblemDTO> problemList = new ArrayList<>();

    static {
        problemList.add(new ProblemDTO("aplusb", "A cộng B", "/problem-pdf/A cộng B.pdf", 1000, 512, 100, teacherList.get(0), false, true));
        problemList.add(new ProblemDTO("findprime", "Tìm số nguyên tố", "/problem-pdf/Tìm số nguyên tố.pdf", 1000, 512, 100, teacherList.get(0), false, false));
    }

    public static List<SubmissionDetailDTO> submissionCodeList = new ArrayList<>();

    static {
        submissionCodeList.add(new SubmissionDetailDTO("qwertyuiop", """
                #include <iostream>
                using namespace std;
                int main() {
                    int a, b;
                    cin >> a >> b;
                    cout << a + b << "\\n";
                    return 0;
                }""", 15, 10, "N19DCCN190", "C++", "Accepted"));
        submissionCodeList.add(new SubmissionDetailDTO("asdfghjklz", """
                #include <iostream>
                using namespace std;
                int main() {
                    int a, b;
                    cin >> a >> b;
                    cout << a + b <<;
                    return 0;
                }""", 0, 0, "N19DCCN221", "C++", "Compile Error"));
    }

    public static List<SubmissionDTO> submissionList = new ArrayList<>();

    static {
        submissionList.add(new SubmissionDTO("N19DCCN190", "aplusb", "20/08/2022 01:10:45", "Accepted", 15, 1, "C++", "qwertyuiop", "/submit-code/1.cpp"));
        submissionList.add(new SubmissionDTO("N19DCCN221", "aplusb", "19/08/2022 10:12:32", "Compile Error", 0, 0, "C++", "asdfghjklz", "/submit-code/2.cpp"));
    }
}
