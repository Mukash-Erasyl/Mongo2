package model

case class Teacher(
                    id: Int,
                    disciplinesIds: List[Int],
                    education: String,
                    qualification: String,
                    experience: Int,
                    scheduleId: Int,
                    studentsIds: List[Int],
                    salary: Int,
                    newsId: Int,
                    materialsId: Int,
                    position: String,
                    awards: String,
                    certificationId: Int,
                    attestationId: Int
                  )

