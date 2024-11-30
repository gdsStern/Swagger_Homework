select students.name, students.age, faculties.name from students
left join faculties on students.faculty_id = faculties.id
select students.name, students.age from students
inner join avatars on avatars.student_id = students.id