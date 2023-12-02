package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import repository.TeacherRepository
import model.{JsonFormatsTeaher, Teacher}

object TeacherRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = JsonFormatsTeaher.formats;

  val route =
    pathPrefix("teacher") {
      concat(
        get {
          parameter("param") { param =>
            complete(TeacherRepository.findTeacherByParams(param.toString))
          }
        },
        pathEnd {
          concat(
            get {
              complete(TeacherRepository.getAllTeachers())
            },
            post {
              entity(as[Teacher]) { teacher =>
                complete(TeacherRepository.addTeacher(teacher))
              }
            }
          )
        },
        path(Segment) { teacherId =>
          concat(
            get {
              complete(TeacherRepository.getTeacherById(teacherId))
            },
            put {
              entity(as[Teacher]) { updatedTeacher =>
                complete(TeacherRepository.updateTeacher(teacherId, updatedTeacher))
              }
            },
            delete {
              complete(TeacherRepository.deleteTeacher(teacherId))
            }
          )
        }
      )
    }
}
