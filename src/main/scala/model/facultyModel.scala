package model

case class Faculty(
                    facultyId: Int,
                    name: String,
                    departmentsIds: List[Int],
                    studentsIds: List[Int],
                    teachersIds: List[Int],
                    curriculumPlansIds: List[Int],
                    specialization: String,
                    creationDate: String,
                    seats: Int
                  )
