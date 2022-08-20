package com.onlinejudge.springthymeleaf.data;

import com.onlinejudge.springthymeleaf.dto.*;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<UserLogin> userList = new ArrayList<>();
    static {
        userList.add(new UserLogin("N19DCCN190", "1"));
        userList.add(new UserLogin("N19DCCN221", "2"));
    }
    public static List<ProblemDetailsDTO> problemDetailsList = new ArrayList<>();

    static {
        problemDetailsList.add(new ProblemDetailsDTO("A", "A cộng B", "/problem-pdf/A cộng B.pdf", 1.00, 512, 100));
        problemDetailsList.add(new ProblemDetailsDTO("B", "Tìm số nguyên tố", "/problem-pdf/Tìm số nguyên tố.pdf", 1.00, 512, 100));
    }

    public static List<ProblemInfoDTO> problemList = new ArrayList<>();

    static {
        problemList.add(new ProblemInfoDTO("A", "A cộng B", true));
        problemList.add(new ProblemInfoDTO("B", "Tìm số nguyên tố", false));
    };

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
        submissionList.add(new SubmissionDTO("N19DCCN190", "20/08/2022 01:10:45", "Accepted", "A", 15, 1, "C++", "qwertyuiop"));
        submissionList.add(new SubmissionDTO("N19DCCN221", "19/08/2022 10:12:32", "Compile Error", "A", 0, 0, "C++", "asdfghjklz"));
    }

    ;
}
