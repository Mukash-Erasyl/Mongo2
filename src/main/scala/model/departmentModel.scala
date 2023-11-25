package model

case class Department(
                       departmentId: Int,
                       name: String,
                       email: String,
                       number: String,
                       direction: String,
                       headId: Int,
                       studentsIds: List[Int],
                       disciplinesIds: List[Int],
                       publicationsIds: List[Int],
                       collaboration: String
                     )
