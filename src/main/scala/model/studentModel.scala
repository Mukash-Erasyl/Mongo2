package model

case class Student(
                           facultyId: Int,
                           courseId: Int,
                           specializationId: Int,
                           scheduleId: Int,
                           gradesIds: List[Int],
                           subjectsIds: List[Int],
                           teachersIds: List[Int],
                           status: String
                         )
