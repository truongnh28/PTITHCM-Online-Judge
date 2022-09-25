create table level
(
    level_id   tinyint     not null
        primary key,
    level_name varchar(20) null
);

create table problem_type
(
    problem_type_id   varchar(100) not null
        primary key,
    problem_type_name varchar(100) not null
);

create table role
(
    role_id   tinyint     not null
        primary key,
    role_name varchar(20) null
);

create table students
(
    student_id         varchar(10)  not null
        primary key,
    active             tinyint      null,
    password           varchar(100) not null,
    student_first_name varchar(100) not null,
    student_last_name  varchar(100) not null
);

create table subjects
(
    subject_id   varchar(10)  not null
        primary key,
    subject_name varchar(100) null
);

create table subject_classes
(
    subject_class_id   varchar(10)  not null
        primary key,
    subject_class_name varchar(100) null,
    subject_id         varchar(10)  not null,
    constraint FKitnnog6jm0qvg0fyivvfe64of
        foreign key (subject_id) references subjects (subject_id)
);

create table subject_class_groups
(
    subject_class_group_id   varchar(10)  not null
        primary key,
    subject_class_group_name varchar(100) null,
    subject_class_id         varchar(10)  not null,
    constraint FK2a0mnnsisml66hi9yui89e1uo
        foreign key (subject_class_id) references subject_classes (subject_class_id)
);

create table student_of_group
(
    student_id             varchar(255) not null,
    subject_class_group_id varchar(255) not null,
    primary key (student_id, subject_class_group_id),
    constraint FK3y3ef138uc2nfwd7s2mwdnd1j
        foreign key (subject_class_group_id) references subject_class_groups (subject_class_group_id),
    constraint FK6fsgrfeatuyel0bvdtsrfxli6
        foreign key (student_id) references students (student_id)
);

create table teachers
(
    teacher_id         varchar(10)  not null
        primary key,
    active             tinyint      null,
    password           varchar(100) not null,
    teacher_first_name varchar(100) not null,
    teacher_last_name  varchar(100) not null
);

create table contests
(
    contest_id    varchar(100) not null
        primary key,
    contest_end   datetime     not null,
    contest_name  varchar(100) not null,
    contest_start datetime     not null,
    hide          tinyint      null,
    teacher_id    varchar(10)  not null,
    constraint FKmgx9ac1f3n40pqevrkv7etwch
        foreign key (teacher_id) references teachers (teacher_id)
);

create table group_has_contest
(
    contest_id             varchar(255) not null,
    subject_class_group_id varchar(255) not null,
    primary key (contest_id, subject_class_group_id),
    constraint FK1la0r5fym36gjxutb1b7slxl6
        foreign key (contest_id) references contests (contest_id),
    constraint FKafxidmce8clqmgxr6fg1bp5tn
        foreign key (subject_class_group_id) references subject_class_groups (subject_class_group_id)
);

create table problems
(
    problem_id            varchar(100) not null
        primary key,
    problem_cloudinary_id varchar(100) null,
    problem_name          varchar(100) null,
    problem_score         int          null,
    problem_time_limit    int          null,
    problem_memory_limit  int          null,
    problem_url           longtext     null,
    hide                  tinyint      null,
    teacher_id            varchar(10)  not null,
    level_id              tinyint      not null,
    constraint FK_problems_level
        foreign key (level_id) references level (level_id),
    constraint FKbogtrdpnh3ei9selovxy1l3qb
        foreign key (teacher_id) references teachers (teacher_id)
);

create table contest_has_problem
(
    contest_id varchar(255) not null,
    problem_id varchar(255) not null,
    primary key (contest_id, problem_id),
    constraint FK8ny9fijhd0pyv2gbaorb3sh9t
        foreign key (problem_id) references problems (problem_id),
    constraint FKrq4fhxf06yft0hr06ujalcx7c
        foreign key (contest_id) references contests (contest_id)
);

create table problem_has_type
(
    problem_id      varchar(255) not null,
    problem_type_id varchar(255) not null,
    primary key (problem_id, problem_type_id),
    constraint FK4erjdrn95082n2v0d9y3aamlf
        foreign key (problem_type_id) references problem_type (problem_type_id),
    constraint FK85oikvlwn1igtxq5d7b4nf2yv
        foreign key (problem_id) references problems (problem_id)
);

create index FK_problems_level_idx
    on problems (level_id);

create table submissions
(
    submission_id    bigint       not null
        primary key,
    submission_score int          null,
    submission_time  datetime     null,
    verdict          tinyint      null,
    problem_id       varchar(100) not null,
    student_id       varchar(10)  not null,
    constraint FKhwebuw14r6lb2ja85w9mwa8vf
        foreign key (student_id) references students (student_id),
    constraint FKj5kbdqokftgx992cx24x3s583
        foreign key (problem_id) references problems (problem_id)
);

create table test_case
(
    test_case_id  varchar(100) not null
        primary key,
    problem_id    varchar(100) not null,
    test_case_in  longtext     not null,
    test_case_out longtext     not null,
    constraint FKmxvd9qtqfvanwpwxvxns4nqpq
        foreign key (problem_id) references problems (problem_id)
);

