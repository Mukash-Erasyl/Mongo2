package model

case class UserTeacher(
                    id: Int,
                    login: String,
                    password: String,
                    userType: String,
                    name: String,
                    birthDate: String,
                    gender: String,
                    nationality: String,
                    address: String ,
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
                  ) extends User1(
  id,
  login,
  password,
  userType,
  name,
  birthDate,
  gender,
  nationality,
  address
)

