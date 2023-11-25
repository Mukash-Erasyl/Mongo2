package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import repository.StudentRepository
import model.Student

object StudentRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val route =
    pathPrefix("student") {
      concat(
        pathEnd {
          concat(
            // Define route for getting all students
            get {
              complete(StudentRepository.getAllStudents())
            },
            // Define route for adding a new student
            post {
              entity(as[Student]) { student =>
                complete(StudentRepository.addStudent(student))
              }
            }
          )
        },
        path(Segment) { studentId =>
          concat(
            // Define route for getting a student by ID
            get {
              complete(StudentRepository.getStudentById(studentId))
            },
            // Define route for updating a student by ID
            put {
              entity(as[Student]) { updatedStudent =>
                complete(StudentRepository.updateStudent(studentId, updatedStudent))
              }
            },
            // Define route for deleting a student by ID
            delete {
              complete(StudentRepository.deleteStudent(studentId))
            }
          )
        }
      )
    }
}
