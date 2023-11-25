package model

case class Syllabus(
                           planId: Int,
                           name: String,
                           description: String,
                           credits: Int,
                           hours: Int,
                           disciplineType: String,
                           teachersIds: List[Int],
                           departmentId: Int,
                           studentsIds: List[Int],
                           materialsIds: List[Int],
                           topics: List[String],
                           classrooms: List[Int],
                           language: String,
                           schedulesIds: List[Int]
                         )
