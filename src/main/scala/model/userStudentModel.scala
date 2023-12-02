package model

abstract class  User1(
                  id: Int,
                  login: String,
                  password: String,
                  userType: String,
                  name: String,
                  birthDate: String,
                  gender: String,
                  nationality: String,
                  address: String
                )


case class UserStudent(
                        id: Int,
                        login: String,
                        password: String,
                        userType: String,
                        name: String,
                        birthDate: String,
                        gender: String,
                        nationality: String,
                        address: String,
                        faculty: Int,
                        course: Int,
                        specialization: Int,
                        scheduleId: Int,
                        gradesIds: List[Int],
                        subjectsIds: List[Int],
                        teachersIds: List[Int],
                        status: String ,

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





case class TeacherUser(
                        id: Int = 0,
                        login: String = "",
                        password: String=" ",
                        userType: String=" ",
                        name: String = "",
                        birthDate: String = "",
                        gender: String = "",
                        nationality: String = "",
                        address: String = "",
                        faculty: Int = 0,
                        course: Int = 0,
                        specialization: Int = 0,
                        scheduleId: Int = 0,
                        gradesIds: List[Int] = List.empty,
                        subjectsIds: List[Int] = List.empty,
                        teachersIds: List[Int] = List.empty,
                        status: String = "" ,
                        disciplinesIds: List[Int] = List.empty,
                        education: String = "",
                        qualification: String = "",
                        experience: Int = 0,
                        studentsIds: List[Int] = List.empty,
                        salary: Int = 0,
                        newsId: Int = 0,
                        materialsId: Int = 0,
                        position: String = "",
                        awards: String = "",
                        certificationId: Int = 0,
                        attestationId: Int = 0

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