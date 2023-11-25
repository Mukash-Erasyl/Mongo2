package route

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import model.Faculty
import org.json4s.{DefaultFormats, jackson}
import repository._



object FacultyRoutes extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val route =
    pathPrefix("faculty") {
      concat(
        pathEnd {
          concat(
            get {
              complete(FacultyRepository.getAllFaculties())
            },
            post {
              entity(as[Faculty]) { faculty =>
                complete(FacultyRepository.addFaculty(faculty))
              }
            }
          )
        },
        path(Segment) { facultyId =>
          concat(
            get {
              complete(FacultyRepository.getFacultyById(facultyId))
            },
            put {
              entity(as[Faculty]) { updatedFaculty =>
                complete(FacultyRepository.updateFaculty(facultyId, updatedFaculty))
              }
            },
            delete {
              complete(FacultyRepository.deleteFaculty(facultyId))
            }
          )
        }
      )
    }
}
