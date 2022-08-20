package com.onlinejudge.springthymleaf.data;

import com.onlinejudge.springthymleaf.dto.ProblemDetailsDTO;
import com.onlinejudge.springthymleaf.dto.ProblemInfoDTO;
import com.onlinejudge.springthymleaf.dto.SubmissionCodeDTO;
import com.onlinejudge.springthymleaf.dto.SubmissionDTO;

import java.util.ArrayList;
import java.util.List;

public class Data {
    //    public List<ProblemDetailsDTO> problemDetailsList = List.of(
//            new ProblemDetailsDTO("A", "A cộng B", "/problem-pdf/A cộng B.pdf", 1.00, 256, 100),
//            new ProblemDetailsDTO("B", "Tìm số nguyên tố", "/problem-pdf/Tìm số nguyên tố.pdf", 1.00, 256, 100)
//    );
//    public List<ProblemInfoDTO> problems = List.of(
//            new ProblemInfoDTO("A", "A cộng B", 2, 50.00, true),
//            new ProblemInfoDTO("B", "Tìm số nguyên tố", 0, 0.00, false)
//    );
//
//    public List<SubmissionCodeDTO> submissionCodeList = List.of(
//            new SubmissionCodeDTO("qwertyuiop","""
//                #include <iostream>
//                using namespace std;
//                int main() {
//                  int a, b;
//                  cin >> a >> b;
//                  cout << a + b << "\\n";
//                  return 0;
//                }""", 15, 10, "N19DCCN190", "C++","Accepted"),
//            new SubmissionCodeDTO("asdfghjklz", """
//                #include <iostream>
//                using namespace std;
//                int main() {
//                  int a, b;
//                  cin >> a >> b;
//                  cout << a + b <<;
//                  return 0;
//                }""", 0, 0, "N19DCCN221", "C++","Compile Error")
//    );
//
//    public List<SubmissionDTO> submissionList = List.of(
//            new SubmissionDTO("N19DCCN190", "20/08/2022 01:10:45", "Accepted", "A", 15, "C++", "qwertyuiop"),
//            new SubmissionDTO("N19DCCN221", "19/08/2022 10:12:32", "Compile Error", "A", 0, "C++", "asdfghjklz")
//    );
    public static List<ProblemDetailsDTO> problemDetailsList = new ArrayList<>();

    static {
        problemDetailsList.add(new ProblemDetailsDTO("A", "A cộng B", "/problem-pdf/A cộng B.pdf", 1.00, 256, 100));
        problemDetailsList.add(new ProblemDetailsDTO("B", "Tìm số nguyên tố", "/problem-pdf/Tìm số nguyên tố.pdf", 1.00, 256, 100));
    }

    public static List<ProblemInfoDTO> problems = new ArrayList<>();

    static {
        problems.add(new ProblemInfoDTO("A", "A cộng B", 2, 50.00, true));
        problems.add(new ProblemInfoDTO("B", "Tìm số nguyên tố", 0, 0.00, false));
    }

    ;

    public static List<SubmissionCodeDTO> submissionCodeList = new ArrayList<>();

    static {
        submissionCodeList.add(new SubmissionCodeDTO("qwertyuiop", """
                #include <iostream>
                using namespace std;
                int main() {
                  int a, b;
                  cin >> a >> b;
                  cout << a + b << "\\n";
                  return 0;
                }""", 15, 10, "N19DCCN190", "C++", "Accepted"));
        submissionCodeList.add(new SubmissionCodeDTO("asdfghjklz", """
                #include <iostream>
                using namespace std;
                int main() {
                  int a, b;
                  cin >> a >> b;
                  cout << a + b <<;
                  return 0;
                }""", 0, 0, "N19DCCN221", "C++", "Compile Error"));
    }

    ;

    public static List<SubmissionDTO> submissionList = new ArrayList<>();

    static {
        submissionList.add(new SubmissionDTO("N19DCCN190", "20/08/2022 01:10:45", "Accepted", "A", 15, "C++", "qwertyuiop"));
        submissionList.add(new SubmissionDTO("N19DCCN221", "19/08/2022 10:12:32", "Compile Error", "A", 0, "C++", "asdfghjklz"));
    }

    ;
}
