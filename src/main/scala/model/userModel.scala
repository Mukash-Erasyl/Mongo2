package model

import java.util.Date
case class  User(
                    id: Int,
                    login: String,
                    password: String,
                    userType: String,
                    name: String,
                    birthDate: Date,
                    gender: String,
                    nationality: String,
                    address: String
                  )

