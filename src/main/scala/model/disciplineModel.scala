package model

case class Discipline(
                       disciplineId: Int,
                       disciplineName: String,
                       description: String,
                       credits: Int,
                       hours: Int,
                       disciplineType: String,
                       teacherIds: List[Int],
                       department: String,
                       studentIds: List[Int],
                       topics: List[String],
                       classrooms: List[Int],
                       language: String,
                       scheduleIds: List[Int]
                     )

