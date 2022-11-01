package ptithcm.onlinejudge.data;

import ptithcm.onlinejudge.dto.*;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<RoleDTO> roleList = new ArrayList<>();

    static {
        roleList.add(new RoleDTO(1, "ROLE_Teacher"));
        roleList.add(new RoleDTO(2, "ROLE_Student"));
    }

    public static List<StudentDTO> studentList = new ArrayList<>();

//    static {
//        studentList.add(new StudentDTO("N19DCCN190", "26102001", "Thanh", "Nguyễn Nhật", true));
//        studentList.add(new StudentDTO("N19DCCN221", "28022001", "Trưởng", "Nguyễn Hữu", true));
//        studentList.add(new StudentDTO("N19DCCN132", "01012001", "Nhơn", "Trần Quốc", true));
//        studentList.add(new StudentDTO("N19DCCN031", "02012001", "Hân", "Trần Thái", true));
//    }

    public static List<TeacherDTO> teacherList = new ArrayList<>();

//    static {
//        teacherList.add(new TeacherDTO("admin", "admin", "Kỳ Thư", "Lưu Nguyễn Kỳ Thư", true, roleList.get(0)));
//    }

    public static List<ProblemTypeDTO> problemTypeList = new ArrayList<>();

    static {
        problemTypeList.add(new ProblemTypeDTO("array", "Array"));
        problemTypeList.add(new ProblemTypeDTO("basic", "Basic"));
        problemTypeList.add(new ProblemTypeDTO("greedy", "Greedy"));
        problemTypeList.add(new ProblemTypeDTO("binarysearch", "Binary Search"));
        problemTypeList.add(new ProblemTypeDTO("sorting", "Sorting"));
        problemTypeList.add(new ProblemTypeDTO("math", "Math"));
        problemTypeList.add(new ProblemTypeDTO("matrix", "Matrix"));
    }

    public static List<ProblemHasTypeDTO> problemHasTypeList = new ArrayList<>();

//    static {
//        problemHasTypeList.add(new ProblemHasTypeDTO("aplusb", "basic"));
//        problemHasTypeList.add(new ProblemHasTypeDTO("findprime", "array"));
//        problemHasTypeList.add(new ProblemHasTypeDTO("findprime", "math"));
//    }

    public static List<LevelDTO> levelList = new ArrayList<>();

    static {
        levelList.add(new LevelDTO(1, "Dễ"));
        levelList.add(new LevelDTO(2, "Trung bình"));
        levelList.add(new LevelDTO(3, "Khó"));
    }

    public static List<TestCaseDTO> testCaseList = new ArrayList<>();

    public static List<SubjectDTO> subjectList = new ArrayList<>();

//    static {
//        subjectList.add(new SubjectDTO("INT13162", "Lập trình với python"));
//        subjectList.add(new SubjectDTO("INT1332", "Lập trình hướng đối tượng"));
//    }

    public static List<SubjectClassDTO> subjectClassList = new ArrayList<>();

//    static {
//        subjectClassList.add(new SubjectClassDTO("INT13162-CN1", "Lập trình python lớp D19CN1", "INT13162"));
//        subjectClassList.add(new SubjectClassDTO("INT13162-CN2", "Lập trình python lớp D19CN2", "INT13162"));
//        subjectClassList.add(new SubjectClassDTO("INT1332-CN1", "Lập trình hướng đối tượng lớp D19CN1", "INT1332"));
//    }

    public static List<SubjectClassGroupDTO> subjectClassGroupList = new ArrayList<>();

//    static {
//        subjectClassGroupList.add(new SubjectClassGroupDTO("INT13162-CN1-1", "Lập trình python Nhóm 1 D19CN1", "INT13162-CN1"));
//        subjectClassGroupList.add(new SubjectClassGroupDTO("INT13162-CN1-2", "Lập trình python Nhóm 2 D19CN1", "INT13162-CN1"));
//        subjectClassGroupList.add(new SubjectClassGroupDTO("INT13162-CN2-1", "Lập trình python Nhóm 1 D19CN2", "INT13162-CN2"));
//    }

    public static List<StudentOfGroupDTO> studentOfGroupList = new ArrayList<>();

    public static List<ContestDTO> contestList = new ArrayList<>();

    public static List<GroupHasContestDTO> groupHasContestList = new ArrayList<>();

    public static List<ContestHasProblemDTO> contestHasProblemList = new ArrayList<>();

    public static List<ProblemDTO> problemList = new ArrayList<>();

//    static {
//        problemList.add(new ProblemDTO("aplusb", "A cộng B", "/problem-pdf/A cộng B.pdf", "", 1000, 512, 100, levelList.get(0), teacherList.get(0), false));
//        problemList.add(new ProblemDTO("findprime", "Tìm số nguyên tố", "/problem-pdf/Tìm số nguyên tố.pdf", "", 1000, 512, 100, levelList.get(1), teacherList.get(0), false));
//    }

    public static List<SubmissionDetailDTO> submissionCodeList = new ArrayList<>();

//    static {
//        submissionCodeList.add(new SubmissionDetailDTO("qwertyuiop", """
//                #include <iostream>
//                using namespace std;
//                int main() {
//                    int a, b;
//                    cin >> a >> b;
//                    cout << a + b << "\\n";
//                    return 0;
//                }""", 15, 10, "N19DCCN190", "C++", "Accepted"));
//        submissionCodeList.add(new SubmissionDetailDTO("asdfghjklz", """
//                #include <iostream>
//                using namespace std;
//                int main() {
//                    int a, b;
//                    cin >> a >> b;
//                    cout << a + b <<;
//                    return 0;
//                }""", 0, 0, "N19DCCN221", "C++", "Compile Error"));
//    }

    public static List<SubmissionDTO> submissionList = new ArrayList<>();

//    static {
//        submissionList.add(new SubmissionDTO("N19DCCN190", "aplusb", "20/08/2022 01:10:45", "Accepted", 15, 1, "C++", "qwertyuiop", "/submit-code/1.cpp"));
//        submissionList.add(new SubmissionDTO("N19DCCN221", "aplusb", "19/08/2022 10:12:32", "Compile Error", 0, 0, "C++", "asdfghjklz", "/submit-code/2.cpp"));
//    }
}
