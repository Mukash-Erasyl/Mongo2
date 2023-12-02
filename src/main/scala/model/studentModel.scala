package model

import org.json4s.JsonAST.JString
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JsonAST, MappingException}


object Statu extends Enumeration {
  type Statu = Value
  val Active, Inactive, Suspended = Value
}

object StatuOfWorkFormat extends CustomSerializer[Statu.Value](format => (
  {
    case JString(s) => Statu.withName(s)
    case value => throw new MappingException(s"Can't convert $value to Statu")
  },
  {
    case status: Statu.Value => JString(status.toString)
  }
))

object JsonFormats {
  implicit val formats: Formats = DefaultFormats + StatuOfWorkFormat
}


case class Student(
                           facultyId: Int,
                           courseId: Int,
                           specializationId: Int,
                           scheduleId: Int,
                           gradesIds: List[Int],
                           subjectsIds: List[Int],
                           teachersIds: List[Int],
                           status: Statu.Value
                         )