package model

case class  User(
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

