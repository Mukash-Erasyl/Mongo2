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
                        status: String
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


